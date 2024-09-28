package pl.owolny.identityprovider.domain.federatedidentity.exception;

import pl.owolny.identityprovider.domain.federatedidentity.FederatedIdentityId;

public class FederatedIdentityNotFound extends RuntimeException {

    public FederatedIdentityNotFound(FederatedIdentityId federatedIdentityId) {
        super("Federated identity with id " + federatedIdentityId.value() + " not found");
    }
}
