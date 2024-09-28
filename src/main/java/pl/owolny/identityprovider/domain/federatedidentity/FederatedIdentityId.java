package pl.owolny.identityprovider.domain.federatedidentity;

import pl.owolny.identityprovider.domain.BaseId;

import java.util.UUID;

public record FederatedIdentityId(UUID value) implements BaseId<UUID> {

    public static FederatedIdentityId of(UUID value) {
        return new FederatedIdentityId(value);
    }

    public static FederatedIdentityId generate() {
        return new FederatedIdentityId(UUID.randomUUID());
    }

    @Override
    public UUID getValue() {
        return value;
    }
}
