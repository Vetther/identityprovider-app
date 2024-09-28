package pl.owolny.identityprovider.domain.userprofile;

import pl.owolny.identityprovider.domain.BaseId;

import java.util.UUID;

record UserProfileId(UUID value) implements BaseId<UUID> {

    public static UserProfileId of(UUID value) {
        return new UserProfileId(value);
    }

    public static UserProfileId generate() {
        return new UserProfileId(UUID.randomUUID());
    }

    @Override
    public UUID getValue() {
        return value;
    }
}