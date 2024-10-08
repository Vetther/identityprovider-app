package pl.owolny.identityprovider.domain.token;

import org.springframework.data.redis.core.RedisHash;
import pl.owolny.identityprovider.domain.user.UserId;
import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.vo.IdentityProvider;

import java.time.LocalDateTime;

@RedisHash(value = "OAuth2LinkingToken", timeToLive = 60L * 5)
class OAuth2LinkingToken extends Token implements OAuth2LinkingTokenInfo {

    private final UserId userId;
    private final Email userEmail;
    private final String userName;
    private final String externalId;
    private final Email externalEmail;
    private final String externalUsername;
    private final IdentityProvider identityProvider;

    OAuth2LinkingToken(String id, LocalDateTime createdAt, UserId userId, Email userEmail, String userName, String externalId, Email externalEmail, String externalUsername, IdentityProvider identityProvider) {
        super(id, createdAt);
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.externalId = externalId;
        this.externalEmail = externalEmail;
        this.externalUsername = externalUsername;
        this.identityProvider = identityProvider;
    }

    @Override
    public UserId getUserId() {
        return userId;
    }

    @Override
    public Email getUserEmail() {
        return userEmail;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getExternalId() {
        return externalId;
    }

    @Override
    public Email getExternalEmail() {
        return externalEmail;
    }

    @Override
    public String getExternalUsername() {
        return externalUsername;
    }

    @Override
    public IdentityProvider getIdentityProvider() {
        return identityProvider;
    }
}