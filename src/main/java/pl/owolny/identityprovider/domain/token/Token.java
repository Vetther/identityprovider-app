package pl.owolny.identityprovider.domain.token;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

abstract class Token implements Serializable {

    @Id
    private final String id;
    private final LocalDateTime createdAt;

    protected Token(String key) {
        this.id = key;
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
