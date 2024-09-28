package pl.owolny.identityprovider.domain.credentials;

import org.springframework.data.jpa.repository.JpaRepository;

interface CredentialsRepositoryJpa extends CredentialsRepository, JpaRepository<Credentials, CredentialsId> {
}
