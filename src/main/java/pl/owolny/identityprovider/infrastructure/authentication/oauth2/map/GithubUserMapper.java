package pl.owolny.identityprovider.infrastructure.authentication.oauth2.map;

import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.OAuth2UserInfo;
import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.vo.IdentityProvider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component("github")
class GithubUserMapper implements OAuth2UserMapper {

    private final RestClient restClient = RestClient.builder().build();

    @Override
    public OAuth2UserInfo map(OAuth2LoginAuthenticationToken authenticationToken) {
        OAuth2User oAuth2User = authenticationToken.getPrincipal();

        List<Map<String, Object>> emails = getExternalEmails(
                authenticationToken.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri(),
                authenticationToken.getAccessToken());

        IdentityProvider identityProvider = IdentityProvider.GITHUB;
        String externalId = oAuth2User.getName();
        String externalUsername = oAuth2User.getAttribute("name");
        String externalAvatarUrl = oAuth2User.getAttribute("avatar_url");
        String externalEmail = getExternalPrimaryEmail(emails);
        boolean isExternalEmailVerified = isExternalEmailVerified(emails);

        return new OAuth2UserInfo(
                identityProvider,
                externalId,
                externalUsername,
                new Email(externalEmail),
                isExternalEmailVerified,
                externalAvatarUrl,
                null,
                null,
                null,
                null,
                null,
                null
        );

    }

    private List<Map<String, Object>> getExternalEmails(String userInfoEndpointUri, OAuth2AccessToken accessToken) {
        return this.restClient.get()
                .uri(userInfoEndpointUri + "/emails")
                .header(HttpHeaders.AUTHORIZATION, "token " + accessToken.getTokenValue())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    private String getExternalPrimaryEmail(List<Map<String, Object>> emails) {
        return (String) emails.stream()
                .filter(m -> (Boolean) m.get("primary"))
                .findFirst()
                .map(m -> m.get("email"))
                .orElse(emails.getFirst().get("email"));
    }

    private boolean isExternalEmailVerified(List<Map<String, Object>> emails) {
        return (Boolean) emails.stream()
                .filter(m -> (Boolean) m.get("primary"))
                .findFirst()
                .map(m -> m.get("verified"))
                .orElse(emails.getFirst().get("verified"));
    }
}
