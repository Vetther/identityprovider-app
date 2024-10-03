package pl.owolny.identityprovider.infrastructure.authentication.oauth2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import pl.owolny.identityprovider.domain.federatedidentity.FederatedIdentityInfo;
import pl.owolny.identityprovider.domain.federatedidentity.FederatedIdentityService;
import pl.owolny.identityprovider.domain.role.RoleService;
import pl.owolny.identityprovider.domain.roleuser.RoleUserService;
import pl.owolny.identityprovider.domain.user.UserInfo;
import pl.owolny.identityprovider.domain.user.UserService;
import pl.owolny.identityprovider.domain.userprofile.UserProfileService;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.exception.OAuth2EmailUnverifiedException;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.map.OAuth2UserMapper;
import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.vo.IdentityProvider;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OAuth2AuthenticationProviderTest {

    private OAuth2AuthenticationProvider authenticationProvider;
    private UserService userService;
    private FederatedIdentityService federatedIdentityService;
    private RoleUserService roleUserService;
    private RoleService roleService;
    private UserProfileService userProfileService;
    private Map<String, OAuth2UserMapper> mappers;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        federatedIdentityService = mock(FederatedIdentityService.class);
        roleUserService = mock(RoleUserService.class);
        roleService = mock(RoleService.class);
        userProfileService = mock(UserProfileService.class);
        mappers = mock(Map.class);

        authenticationProvider = new OAuth2AuthenticationProvider(userService, federatedIdentityService, roleUserService, roleService, userProfileService, mappers);
    }

    @Test
    void authenticate_ShouldReturnAuthenticationWhenUserExists() {
        // Setup the mock objects
        OAuth2LoginAuthenticationToken token = mock(OAuth2LoginAuthenticationToken.class);
        OAuth2UserMapper mapper = mock(OAuth2UserMapper.class);
        OAuth2UserInfo oAuth2UserInfo = mock(OAuth2UserInfo.class);
        when(mappers.containsKey(anyString())).thenReturn(true);
        when(mappers.get(anyString())).thenReturn(mapper);
        when(mapper.map(any(OAuth2LoginAuthenticationToken.class))).thenReturn(oAuth2UserInfo);

        FederatedIdentityInfo federatedIdentityInfo = mock(FederatedIdentityInfo.class);
        when(federatedIdentityService.getByExternalId(anyString(), IdentityProvider.GOOGLE)).thenReturn(Optional.of(federatedIdentityInfo));

        UserInfo userInfo = mock(UserInfo.class);
        when(userService.getById(any())).thenReturn(userInfo);

        // Call the method under test
        Authentication result = authenticationProvider.authenticate(token);

        // Verify the behavior and the returned result
        assertNotNull(result);
        verify(userService, times(1)).getById(any());
    }

    @Test
    void authenticate_ShouldThrowExceptionWhenEmailNotVerified() {
        // Setup the mock objects
        OAuth2LoginAuthenticationToken token = mock(OAuth2LoginAuthenticationToken.class);
        OAuth2UserMapper mapper = mock(OAuth2UserMapper.class);
        OAuth2UserInfo oAuth2UserInfo = mock(OAuth2UserInfo.class);
        when(mappers.containsKey(anyString())).thenReturn(true);
        when(mappers.get(anyString())).thenReturn(mapper);
        when(mapper.map(any(OAuth2LoginAuthenticationToken.class))).thenReturn(oAuth2UserInfo);

        when(oAuth2UserInfo.isExternalEmailVerified()).thenReturn(false);
        when(oAuth2UserInfo.externalEmail()).thenReturn(new Email("test@invalid.com"));

        assertThrows(OAuth2EmailUnverifiedException.class, () -> authenticationProvider.authenticate(token));
    }

}
