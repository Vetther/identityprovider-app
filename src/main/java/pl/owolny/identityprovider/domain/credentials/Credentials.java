package pl.owolny.identityprovider.domain.credentials;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.owolny.identityprovider.vo.PasswordHash;
import pl.owolny.identityprovider.domain.user.UserId;

import java.time.LocalDateTime;

@Entity
class Credentials {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private CredentialsId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id", unique = true))
    private UserId userId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password_hash"))
    private PasswordHash password;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    protected Credentials() {}

    Credentials(UserId userId, PasswordHash password) {
        this.id = CredentialsId.generate();
        this.userId = userId;
        this.password = password;
    }

    void updatePassword(PasswordHash password) {
        this.password = password;
    }

    CredentialsId getId() {
        return id;
    }

    UserId getUserId() {
        return userId;
    }

    PasswordHash getPassword() {
        return password;
    }
}
