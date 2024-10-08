package pl.owolny.identityprovider;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.owolny.identityprovider.domain.credentials.CredentialsService;
import pl.owolny.identityprovider.domain.role.RoleInfo;
import pl.owolny.identityprovider.domain.role.RoleService;
import pl.owolny.identityprovider.domain.roleuser.RoleUserService;
import pl.owolny.identityprovider.domain.token.OAuth2LinkingTokenInfo;
import pl.owolny.identityprovider.domain.token.OAuth2LinkingTokenService;
import pl.owolny.identityprovider.domain.user.UserInfo;
import pl.owolny.identityprovider.domain.user.UserService;
import pl.owolny.identityprovider.domain.userprofile.UserProfileInfo;
import pl.owolny.identityprovider.domain.userprofile.UserProfileService;
import pl.owolny.identityprovider.infrastructure.authentication.AuthenticatedUser;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.OAuth2UserInfo;
import pl.owolny.identityprovider.vo.*;

import java.time.LocalDate;
import java.util.Optional;

@Log4j2
@RestController
@SpringBootApplication
public class IdentityProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentityProviderApplication.class, args);
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal Object user) {

        if (user instanceof AuthenticatedUser) {
            return """
                    <h1>Welcome to Identity Provider</h1>
                    <p>Your ID: %s</p>
                    <p>Your authorities: %s</p>
                    """.formatted(((AuthenticatedUser) user).getUserId().value(), ((AuthenticatedUser) user).getAuthorities());
        }
        return """
                <h1>Welcome to Identity Provider</h1>
                <p>Your user: %s</p>
                """.formatted(user);
    }

    @Bean
    public String test(OAuth2LinkingTokenService tokenService, UserService userService, UserProfileService userProfileService, RoleService roleService, RoleUserService roleUserService, CredentialsService credentialsService) throws InterruptedException {

        RoleInfo roleInfo = roleService.createNew(RoleName.ROLE_USER);
        log.warn("Created role: {}", roleInfo.getName());

        roleService.addAuthority(roleInfo.getName(), "USER_READ");
        roleService.addAuthority(roleInfo.getName(), "USER_WRITE");
        roleService.addAuthority(roleInfo.getName(), "USER_ELSE");

        roleInfo = roleService.getByName(RoleName.ROLE_USER);
        log.warn("Role: {}", roleInfo.getName());
        log.warn("Authorities: {}", roleInfo.getAuthorities());

        UserInfo vetther = userService.createNew("test", new Email("test@email.com"), false, true);
        log.warn("Created user: {}, {}", vetther.getUsername(), vetther.getEmail());

        userService.verifyEmail(vetther.getId());
        log.warn("Verified email for user: {}", vetther.getUsername());
        vetther = userService.getById(vetther.getId());
        log.warn("Is email verified? {}", vetther.isEmailVerified());

        roleUserService.assignRoleToUser(vetther.getId(), roleInfo.getId());
        log.warn("Assigned role: {} to user: {}", roleInfo.getName(), vetther.getUsername());
        boolean hasRole = roleUserService.hasRole(vetther.getId(), roleInfo.getId());
        log.warn("User has role: {}? {}", roleInfo.getName(), hasRole);

        UserProfileInfo profileInfo = userProfileService.createNew(
                vetther.getId(),
                "Vetther",
                "Vetther",
                "123456789",
                new PhoneNumber("+48123456789"),
                Gender.MALE, LocalDate.now(),
                new CountryCode("PL"));
        log.warn("Created profile: {}, {} ({})", profileInfo.getFirstName(), profileInfo.getLastName(), profileInfo.getPhoneNumber());

        credentialsService.createNew(vetther.getId(), "test");
        log.warn("Created credentials for user: {}", vetther.getUsername());
        boolean checkPassword = credentialsService.checkPassword(vetther.getId(), "test");
        log.warn("Password check: {}", checkPassword);

        OAuth2UserInfo oAuth2UserInfo = new OAuth2UserInfo(IdentityProvider.GOOGLE, "test", "test", vetther.getEmail(), true, "test", "test", "test", null, null, null, null);
        tokenService.saveLinkData(vetther, oAuth2UserInfo);

        Optional<OAuth2LinkingTokenInfo> linkData = tokenService.getLinkData(vetther, oAuth2UserInfo);
        log.warn("Link data exists: {}", linkData.isPresent());
        linkData.ifPresent(oAuth2LinkingToken -> log.warn("Link data: {}", oAuth2LinkingToken));

        return "TEST";
    }

}
