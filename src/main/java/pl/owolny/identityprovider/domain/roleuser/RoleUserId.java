package pl.owolny.identityprovider.domain.roleuser;

import pl.owolny.identityprovider.domain.BaseId;

import java.util.UUID;

record RoleUserId(UUID value) implements BaseId<UUID> {

    public static RoleUserId of(UUID value) {
        return new RoleUserId(value);
    }

    public static RoleUserId generate() {
        return new RoleUserId(UUID.randomUUID());
    }

    @Override
    public UUID getValue() {
        return value;
    }
}
