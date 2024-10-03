package pl.owolny.identityprovider.infrastructure.authentication.oauth2.map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.OAuth2UserInfo;
import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.vo.IdentityProvider;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Component("facebook")
class FacebookUserMapper implements OAuth2UserMapper {

    @Override
    public OAuth2UserInfo map(OAuth2LoginAuthenticationToken authenticationToken) {
        OAuth2User oAuth2User = authenticationToken.getPrincipal();

        IdentityProvider identityProvider = IdentityProvider.FACEBOOK;
        String externalId = oAuth2User.getName();
        String externalUsername = oAuth2User.getAttribute("name");
        String externalAvatarUrl = getPictureUrl(oAuth2User).orElse(null);
        String externalEmail = oAuth2User.getAttribute("email");
        boolean isExternalEmailVerified = true;
        String firstName = oAuth2User.getAttribute("first_name");
        String lastName = oAuth2User.getAttribute("last_name");
        LocalDate birthday = convertToLocalDate(oAuth2User.getAttribute("birthday"));

        System.out.println(birthday);

        return new OAuth2UserInfo(
                identityProvider,
                externalId,
                externalUsername,
                new Email(externalEmail),
                isExternalEmailVerified,
                externalAvatarUrl,
                firstName,
                lastName,
                null,
                null,
                birthday,
                null
        );
    }

    private Optional<String> getPictureUrl(OAuth2User oAuth2User) {
        Map<String, Map<String, Object>> pictureMap = oAuth2User.getAttribute("picture");
        if (pictureMap == null) {
            return Optional.empty();
        }
        Map<String, Object> data = pictureMap.get("data");
        if (data == null) {
            return Optional.empty();
        }
        return Optional.ofNullable((String) data.get("url"));
    }

    private LocalDate convertToLocalDate(String birthday) {
        if (birthday == null) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            return LocalDate.parse(birthday, formatter);
        } catch (DateTimeParseException e) {
            log.error("Cannot parse birthday: {}", birthday);
            return null;
        }
    }
}
