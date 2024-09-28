package pl.owolny.identityprovider.domain.federatedidentity;

import org.springframework.transaction.annotation.Transactional;
import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.vo.IdentityProvider;
import pl.owolny.identityprovider.domain.federatedidentity.exception.FederatedIdentityNotFound;
import pl.owolny.identityprovider.domain.user.UserId;

import java.util.Set;

class FederatedIdentityServiceImpl implements FederatedIdentityService {

    private final FederatedIdentityRepository federatedIdentityRepository;

    public FederatedIdentityServiceImpl(FederatedIdentityRepository federatedIdentityRepository) {
        this.federatedIdentityRepository = federatedIdentityRepository;
    }

    @Override
    @Transactional
    public FederatedIdentity createNew(UserId userId, String externalId, IdentityProvider provider, String username, Email email, boolean isEmailVerified) {
        FederatedIdentity federatedIdentity = new FederatedIdentity(userId, externalId, provider, username, email, null, isEmailVerified);
        federatedIdentityRepository.save(federatedIdentity);
        return federatedIdentity;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<FederatedIdentity> getByUserId(UserId userId) {
        return federatedIdentityRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void verifyEmail(FederatedIdentityId federatedIdentityId) {
        FederatedIdentity federatedIdentity = federatedIdentityRepository.findById(federatedIdentityId)
                .orElseThrow(() -> new FederatedIdentityNotFound(federatedIdentityId));
        federatedIdentity.verifyEmail();
    }
}
