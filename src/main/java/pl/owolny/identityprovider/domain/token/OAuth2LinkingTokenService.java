package pl.owolny.identityprovider.domain.token;

import pl.owolny.identityprovider.domain.user.UserInfo;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.OAuth2UserInfo;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class OAuth2LinkingTokenService extends TokenService<OAuth2LinkingToken> {

    OAuth2LinkingTokenService(OAuth2LinkingTokenRepository tokenRepository) {
        super(tokenRepository);
    }

    public void saveLinkData(UserInfo userInfo, OAuth2UserInfo oAuth2UserInfo) {
        OAuth2LinkingToken token = new OAuth2LinkingToken(
                getKey(userInfo.getId().value(), oAuth2UserInfo.externalId()),
                LocalDateTime.now(),
                userInfo.getId(),
                userInfo.getEmail(),
                userInfo.getUsername(),
                oAuth2UserInfo.externalId(),
                oAuth2UserInfo.externalEmail(),
                oAuth2UserInfo.externalUsername(),
                oAuth2UserInfo.provider()
        );
        super.createToken(token);
    }

    public Optional<OAuth2LinkingTokenInfo> getLinkData(UserInfo userInfo, OAuth2UserInfo oAuth2UserInfo) {
        return super.getToken(getKey(userInfo.getId().value(), oAuth2UserInfo.externalId())).map(token -> token);
    }

    private String getKey(UUID userId, String externalId) {
        return userId.toString() + ":" + externalId;
    }
}
