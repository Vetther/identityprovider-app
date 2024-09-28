package pl.owolny.identityprovider.domain.credentials;

import pl.owolny.identityprovider.domain.BaseId;

import java.util.UUID;

record CredentialsId(UUID value) implements BaseId<UUID> {

    public static CredentialsId of(UUID value) {
        return new CredentialsId(value);
    }

    public static CredentialsId generate() {
        return new CredentialsId(UUID.randomUUID());
    }

    @Override
    public UUID getValue() {
        return value;
    }
}
