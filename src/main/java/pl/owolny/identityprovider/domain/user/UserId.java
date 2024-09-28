package pl.owolny.identityprovider.domain.user;

import pl.owolny.identityprovider.domain.BaseId;

import java.util.UUID;

public record UserId(UUID value) implements BaseId<UUID> {

    public static UserId of(UUID value) {
        return new UserId(value);
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    @Override
    public UUID getValue() {
        return value;
    }
}
