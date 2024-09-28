package pl.owolny.identityprovider.domain.federatedidentity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.vo.IdentityProvider;
import pl.owolny.identityprovider.domain.user.UserId;

import java.time.LocalDateTime;

@Entity
public class FederatedIdentity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private FederatedIdentityId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserId userId;

    @Enumerated(EnumType.STRING)
    private IdentityProvider provider;

    private String externalId;

    private String externalUsername;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email"))
    private Email externalEmail;

    private boolean isExternalEmailVerified;

    @CreationTimestamp
    private LocalDateTime connectedAt;

    FederatedIdentity(UserId userId, String externalId, IdentityProvider provider, String externalUsername, Email externalEmail, LocalDateTime connectedAt, boolean isExternalEmailVerified) {
        this.id = FederatedIdentityId.generate();
        this.userId = userId;
        this.externalId = externalId;
        this.provider = provider;
        this.externalUsername = externalUsername;
        this.externalEmail = externalEmail;
        this.connectedAt = connectedAt;
        this.isExternalEmailVerified = isExternalEmailVerified;
    }

    protected FederatedIdentity() {}

    void verifyEmail() {
        this.isExternalEmailVerified = true;
    }
}
