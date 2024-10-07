package pl.owolny.identityprovider.domain.token;

import pl.owolny.identityprovider.domain.user.UserId;
import pl.owolny.identityprovider.domain.user.UserInfo;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.OAuth2UserInfo;

import java.util.Optional;

public class OAuth2LinkingTokenService extends TokenServiceImpl<OAuth2LinkingToken> {

    OAuth2LinkingTokenService(TokenRepository<OAuth2LinkingToken> tokenRepository) {
        super(tokenRepository);
    }

    public void saveLinkData(UserInfo userInfo, OAuth2UserInfo oAuth2UserInfo) {
        OAuth2LinkingToken token = new OAuth2LinkingToken(userInfo, oAuth2UserInfo);
        super.createToken(token);
    }

    public Optional<OAuth2LinkingToken> getLinkData(UserId userId, String externalId) {
        return super.getToken(OAuth2LinkingToken.getKey(userId, externalId));
    }
}
