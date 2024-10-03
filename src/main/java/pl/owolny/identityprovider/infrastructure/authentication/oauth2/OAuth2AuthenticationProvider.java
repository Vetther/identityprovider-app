package pl.owolny.identityprovider.infrastructure.authentication.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;
import pl.owolny.identityprovider.domain.federatedidentity.FederatedIdentityInfo;
import pl.owolny.identityprovider.domain.federatedidentity.FederatedIdentityService;
import pl.owolny.identityprovider.domain.role.RoleId;
import pl.owolny.identityprovider.domain.role.RoleService;
import pl.owolny.identityprovider.domain.roleuser.RoleUserService;
import pl.owolny.identityprovider.domain.user.UserInfo;
import pl.owolny.identityprovider.domain.user.UserService;
import pl.owolny.identityprovider.infrastructure.authentication.AuthenticateUser;
import pl.owolny.identityprovider.infrastructure.authentication.AuthenticatedUser;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.map.OAuth2UserMapper;
import pl.owolny.identityprovider.vo.Email;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
class OAuth2AuthenticationProvider extends OAuth2LoginAuthenticationProvider implements AuthenticateUser {

    private final UserService userService;
    private final FederatedIdentityService federatedIdentityService;
    private final RoleUserService roleUserService;
    private final RoleService roleService;
    private final Map<String, OAuth2UserMapper> mappers;

    public OAuth2AuthenticationProvider(UserService userService, FederatedIdentityService federatedIdentityService, RoleUserService roleUserService, RoleService roleService, Map<String, OAuth2UserMapper> mappers) {
        super(new DefaultAuthorizationCodeTokenResponseClient(), new DefaultOAuth2UserService());
        this.userService = userService;
        this.federatedIdentityService = federatedIdentityService;
        this.roleUserService = roleUserService;
        this.roleService = roleService;
        this.mappers = mappers;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2LoginAuthenticationToken authenticated = (OAuth2LoginAuthenticationToken) super.authenticate(authentication);
        if (authenticated == null) {
            return null;
        }

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
            UserInfo userInfo = createNewUser(oAuth2UserInfo);
            log.info("[OAuth2] User with email didnt exists, created new: {}", userInfo.getId());
            return createSuccessAuthentication(new OAuth2AuthenticatedUser(userInfo.getId(), getUserAuthorities(userInfo), null), authenticated);
        }

        UserInfo userInfo = userByEmail.get();

        if (!userInfo.isEmailVerified() && oAuth2UserInfo.isExternalEmailVerified()) {
            log.info("[OAuth2] UserInfo or OAuth2UserInfo email was not verified: {}", userInfo.getId());
            throw new UsernameNotFoundException("Email not verified");
        }

        log.info("[OAuth2] UserInfo and OAuth2UserInfo emails were verified: {}/{}", userInfo.getId(), oAuth2UserInfo);
        log.info("[OAuth2] Linking account with federated account");
        federatedIdentityService.createNew(userInfo.getId(), oAuth2UserInfo.externalId(), oAuth2UserInfo.provider(), oAuth2UserInfo.externalUsername(), oAuth2UserInfo.externalEmail(), oAuth2UserInfo.isExternalEmailVerified());
        return createSuccessAuthentication(new OAuth2AuthenticatedUser(userInfo.getId(), getUserAuthorities(userInfo), null), authenticated);
    }

    @Override
    public <U extends AuthenticatedUser> Authentication createSuccessAuthentication(U user, Authentication authentication) {
        var authenticationResult = new OAuth2LoginAuthenticationToken(
                ((OAuth2LoginAuthenticationToken) authentication).getClientRegistration(),
                ((OAuth2LoginAuthenticationToken) authentication).getAuthorizationExchange(),
                (OAuth2User) user,
                user.getAuthorities(),
                ((OAuth2LoginAuthenticationToken) authentication).getAccessToken(),
                ((OAuth2LoginAuthenticationToken) authentication).getRefreshToken()
        );
        authenticationResult.setDetails(authentication.getDetails());
        log.info("[OAuth2] User authenticated: {}", user);
        return authenticationResult;
    }

    private UserInfo createNewUser(OAuth2UserInfo userInfo) {
        var user = userService.createNew(
                userInfo.externalUsername(),
                userInfo.externalEmail(),
                userInfo.isExternalEmailVerified(),
                true
        );
        federatedIdentityService.createNew(
                user.getId(),
                userInfo.externalId(),
                userInfo.provider(),
                userInfo.externalUsername(),
                userInfo.externalEmail(),
                userInfo.isExternalEmailVerified()
        );
        return user;
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
}
