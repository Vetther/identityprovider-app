package pl.owolny.identityprovider.infrastructure.authentication.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.oidc.authentication.OidcAuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;
import pl.owolny.identityprovider.domain.federatedidentity.FederatedIdentityInfo;
import pl.owolny.identityprovider.domain.federatedidentity.FederatedIdentityService;
import pl.owolny.identityprovider.domain.role.RoleId;
import pl.owolny.identityprovider.domain.role.RoleService;
import pl.owolny.identityprovider.domain.roleuser.RoleUserService;
import pl.owolny.identityprovider.domain.token.OAuth2LinkingTokenService;
import pl.owolny.identityprovider.domain.user.UserInfo;
import pl.owolny.identityprovider.domain.user.UserService;
import pl.owolny.identityprovider.domain.userprofile.UserProfileService;
import pl.owolny.identityprovider.infrastructure.authentication.AuthenticationFactory;
import pl.owolny.identityprovider.infrastructure.authentication.AuthenticatedUser;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.exception.OAuth2EmailUnverifiedException;
import pl.owolny.identityprovider.vo.Email;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
class OAuth2AuthenticationProvider implements AuthenticationProvider, AuthenticationFactory {

    private final OidcAuthorizationCodeAuthenticationProvider oidcProvider;
    private final OAuth2LoginAuthenticationProvider oauth2Provider;
    private final UserService userService;
    private final FederatedIdentityService federatedIdentityService;
    private final RoleUserService roleUserService;
    private final RoleService roleService;
    private final UserProfileService userProfileService;
    private final OAuth2LinkingTokenService tokenService;
    private final Map<String, OAuth2UserMapper> mappers;

    public OAuth2AuthenticationProvider(UserService userService, FederatedIdentityService federatedIdentityService, RoleUserService roleUserService, RoleService roleService, UserProfileService userProfileService, OAuth2LinkingTokenService tokenService, Map<String, OAuth2UserMapper> mappers) {
        this.tokenService = tokenService;
        this.oauth2Provider = new OAuth2LoginAuthenticationProvider(new DefaultAuthorizationCodeTokenResponseClient(), new DefaultOAuth2UserService());
        this.oidcProvider = new OidcAuthorizationCodeAuthenticationProvider(new DefaultAuthorizationCodeTokenResponseClient(), new OidcUserService());
        this.userProfileService = userProfileService;
        this.userService = userService;
        this.federatedIdentityService = federatedIdentityService;
        this.roleUserService = roleUserService;
        this.roleService = roleService;
        this.mappers = mappers;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2LoginAuthenticationToken authenticated = isOidcProvider((OAuth2LoginAuthenticationToken) authentication)
                ? (OAuth2LoginAuthenticationToken) oidcProvider.authenticate(authentication)
                : (OAuth2LoginAuthenticationToken) oauth2Provider.authenticate(authentication);

        if (authenticated == null) return null;

        OAuth2UserMapper mapper = getMapper(authenticated.getClientRegistration().getRegistrationId());
        OAuth2UserInfo oAuth2UserInfo = mapper.map(authenticated);

        Optional<FederatedIdentityInfo> federatedIdentity = federatedIdentityService.getByExternalId(oAuth2UserInfo.externalId(), oAuth2UserInfo.provider());
        boolean connectedUserExists = federatedIdentity.isPresent();

        if (connectedUserExists) {
            UserInfo userInfo = userService.getById(federatedIdentity.get().getUserId());
            log.info("[OAuth2] User already existed: {}", userInfo.getId());
            return createSuccessAuthentication(new OAuth2AuthenticatedUser(userInfo.getId(), getUserAuthorities(userInfo), null), authenticated);
        }

        Email email = oAuth2UserInfo.externalEmail();
        Optional<UserInfo> userByEmail = userService.findByEmail(email);
        boolean userWithEmailExists = userByEmail.isPresent();

        if (!userWithEmailExists) {
            if (!oAuth2UserInfo.isExternalEmailVerified()) {
                log.info("[OAuth2] User with email didnt exists, but external email was not verified: {}", email);
                throw new OAuth2EmailUnverifiedException(null, oAuth2UserInfo);
            }
            UserInfo userInfo = createNewUser(oAuth2UserInfo);
            linkUserWithFederatedAccount(userInfo, oAuth2UserInfo);
            log.info("[OAuth2] User with email didnt exists, created new: {}", userInfo.getId());
            return createSuccessAuthentication(new OAuth2AuthenticatedUser(userInfo.getId(), getUserAuthorities(userInfo), null), authenticated);
        }

        UserInfo userInfo = userByEmail.get();

        if ((userInfo.isEmailVerified() && oAuth2UserInfo.isExternalEmailVerified())) {
            log.info("[OAuth2] UserInfo or OAuth2UserInfo email was not verified: {}", userInfo.getId());
            throw new OAuth2EmailUnverifiedException(userInfo, oAuth2UserInfo);
        }

        log.info("[OAuth2] UserInfo and OAuth2UserInfo emails were verified: {}/{}", userInfo.getId(), oAuth2UserInfo);
        log.info("[OAuth2] Linking account with federated account");
        linkUserWithFederatedAccount(userInfo, oAuth2UserInfo);
        return createSuccessAuthentication(new OAuth2AuthenticatedUser(userInfo.getId(), getUserAuthorities(userInfo), null), authenticated);
    }

    private boolean isOidcProvider(OAuth2LoginAuthenticationToken authentication) {
        return authentication.getAuthorizationExchange().getAuthorizationRequest().getScopes().contains("openid");
    }

    @Override
    public Authentication createSuccessAuthentication(AuthenticatedUser user, Authentication authentication) {
        OAuth2LoginAuthenticationToken result = (OAuth2LoginAuthenticationToken) authentication;
        var authenticationResult = new OAuth2LoginAuthenticationToken(
                result.getClientRegistration(),
                result.getAuthorizationExchange(),
                (OAuth2User) user,
                user.getAuthorities(),
                result.getAccessToken(),
                result.getRefreshToken()
        );
        authenticationResult.setDetails(authentication.getDetails());
        log.info("[OAuth2] User authenticated: {}", user);
        return authenticationResult;
    }

    private UserInfo createNewUser(OAuth2UserInfo oAuth2UserInfo) {
        var user = userService.createNew(
                oAuth2UserInfo.externalUsername(),
                oAuth2UserInfo.externalEmail(),
                oAuth2UserInfo.isExternalEmailVerified(),
                true
        );
        userProfileService.createNew(
                user.getId(),
                oAuth2UserInfo.firstName(),
                oAuth2UserInfo.lastName(),
                oAuth2UserInfo.pictureUrl(),
                oAuth2UserInfo.phoneNumber(),
                oAuth2UserInfo.gender(),
                oAuth2UserInfo.birthDate(),
                oAuth2UserInfo.countryCode()
        );
        return user;
    }

    private void linkUserWithFederatedAccount(UserInfo userInfo, OAuth2UserInfo oAuth2UserInfo) {
        federatedIdentityService.createNew(
                userInfo.getId(),
                oAuth2UserInfo.externalId(),
                oAuth2UserInfo.provider(),
                oAuth2UserInfo.externalUsername(),
                oAuth2UserInfo.externalEmail(),
                oAuth2UserInfo.isExternalEmailVerified()
        );
    }

    private Set<SimpleGrantedAuthority> getUserAuthorities(UserInfo userInfo) {
        Set<RoleId> userRoles = this.roleUserService.getUserRoles(userInfo.getId());
        return userRoles.stream()
                .map(this.roleService::getById)
                .flatMap(role -> role.getAuthorities().stream())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    private OAuth2UserMapper getMapper(String registrationId) {
        Assert.isTrue(mappers.containsKey(registrationId), "No mapper defined for registrationId " + registrationId);
        return mappers.get(registrationId);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2LoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
