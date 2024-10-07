package pl.owolny.identityprovider.domain.token;

import java.util.Optional;

interface TokenService<T extends Token> {

    void createToken(T token);

    Optional<T> getToken(String id);

    boolean isTokenValid(String id);

    void deleteToken(String id);
}