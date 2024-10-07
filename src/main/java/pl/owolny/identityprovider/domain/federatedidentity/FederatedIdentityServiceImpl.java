package pl.owolny.identityprovider.domain.federatedidentity;

import org.springframework.transaction.annotation.Transactional;
import pl.owolny.identityprovider.vo.Email;
import pl.owolny.identityprovider.vo.IdentityProvider;
import pl.owolny.identityprovider.domain.federatedidentity.exception.FederatedIdentityNotFound;
import pl.owolny.identityprovider.domain.user.UserId;

import java.time.LocalDateTime;
import java.util.*;

class FederatedIdentityServiceImpl implements FederatedIdentityService {

    private final FederatedIdentityRepository federatedIdentityRepository;

    public FederatedIdentityServiceImpl(FederatedIdentityRepository federatedIdentityRepository) {
        this.federatedIdentityRepository = federatedIdentityRepository;
    }

    @Override
    @Transactional
    public FederatedIdentityInfo createNew(UserId userId, String externalId, IdentityProvider provider, String username, Email email, boolean isEmailVerified) {
        FederatedIdentity federatedIdentity = new FederatedIdentity(userId, externalId, provider, username, email, LocalDateTime.now(), isEmailVerified);
        federatedIdentityRepository.save(federatedIdentity);
        return federatedIdentity;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FederatedIdentityInfo> getByExternalId(String externalId, IdentityProvider provider) {
        return federatedIdentityRepository.findByExternalIdAndProvider(externalId, provider).map(fi -> fi);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FederatedIdentityInfo> getAll() {
        return Collections.unmodifiableList(federatedIdentityRepository.findAll());
    }

    @Override
    @Transactional
    public void verifyEmail(FederatedIdentityId federatedIdentityId) {
        FederatedIdentity federatedIdentity = federatedIdentityRepository.findById(federatedIdentityId)
                .orElseThrow(() -> new FederatedIdentityNotFound(federatedIdentityId));
        federatedIdentity.verifyEmail();
    }
}
