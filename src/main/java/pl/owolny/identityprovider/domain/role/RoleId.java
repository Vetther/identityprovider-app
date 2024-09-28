package pl.owolny.identityprovider.domain.role;

import pl.owolny.identityprovider.domain.BaseId;

import java.util.UUID;

public record RoleId(UUID value) implements BaseId<UUID> {

    public static RoleId of(UUID value) {
        return new RoleId(value);
    }

    public static RoleId generate() {
        return new RoleId(UUID.randomUUID());
    }

    @Override
    public UUID getValue() {
        return value;
    }
}
