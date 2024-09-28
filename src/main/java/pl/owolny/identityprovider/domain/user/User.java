package pl.owolny.identityprovider.domain.user;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.owolny.identityprovider.vo.Email;

import java.time.LocalDateTime;

@Entity(name = "users")
class User implements UserInfo {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private UserId id;

    private String username;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email"))
    private Email email;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean isActive;

    private boolean isEmailVerified;

    protected User() {}

    User(String username, Email email) {
        this.id = UserId.generate();
        this.username = username;
        this.email = email;
        this.isActive = false;
        this.isEmailVerified = false;
    }

    void verifyEmail() {
        this.isEmailVerified = true;
    }

    void deactivateUser() {
        this.isActive = false;
    }

    void activateUser() {
        this.isActive = true;
    }

    @Override
    public UserId getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Email getEmail() {
        return email;
    }

    @Override
    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    boolean isActive() {
        return isActive;
    }
}