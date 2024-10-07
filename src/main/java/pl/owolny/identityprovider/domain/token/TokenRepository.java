package pl.owolny.identityprovider.domain.token;

import org.springframework.data.keyvalue.repository.KeyValueRepository;

interface TokenRepository<T extends Token> extends KeyValueRepository<T, String> {
}
