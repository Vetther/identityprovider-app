package pl.owolny.identityprovider.domain.token;

import org.springframework.data.redis.core.RedisHash;
import pl.owolny.identityprovider.domain.user.UserId;
import pl.owolny.identityprovider.domain.user.UserInfo;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.OAuth2UserInfo;
import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.vo.IdentityProvider;

import java.util.UUID;

@RedisHash(value = "OAuth2LinkingToken", timeToLive = 60L * 5)
public class OAuth2LinkingToken extends Token {

    private final static String KEY_PREFIX = "OAuth2LinkingToken";

    private final UUID userId;
    private final String userEmail;
    private final String userName;
    private final String externalId;
    private final String externalEmail;
    private final String externalUsername;
    private final IdentityProvider identityProvider;

    OAuth2LinkingToken(UserInfo userInfo, OAuth2UserInfo oAuth2UserInfo) {
        super(getKey(userInfo.getId(), oAuth2UserInfo.externalId()));
        this.userId = userInfo.getId().value();
        this.userEmail = userInfo.getEmail().value();
        this.userName = userInfo.getUsername();
        this.externalId = oAuth2UserInfo.externalId();
        this.externalEmail = oAuth2UserInfo.externalEmail().value();
        this.externalUsername = oAuth2UserInfo.externalUsername();
        this.identityProvider = oAuth2UserInfo.provider();
    }

    public static String getKey(UserId userId, String externalId) {
        return KEY_PREFIX + ":" + userId.value() + ":" + externalId;
    }

    public UserId getUserId() {
        return new UserId(userId);
    }

    public Email getUserEmail() {
        return new Email(userEmail);
    }

    public String getUserName() {
        return userName;
    }

    public String getExternalId() {
        return externalId;
    }

    public Email getExternalEmail() {
        return new Email(externalEmail);
    }

    public String getExternalUsername() {
        return externalUsername;
    }

    public IdentityProvider getIdentityProvider() {
        return identityProvider;
    }
}