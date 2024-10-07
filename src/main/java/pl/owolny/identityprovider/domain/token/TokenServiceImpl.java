package pl.owolny.identityprovider.domain.token;

import java.util.Optional;

abstract class TokenServiceImpl<T extends Token> implements TokenService<T> {

    private final TokenRepository<T> tokenRepository;

    public TokenServiceImpl(TokenRepository<T> tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void createToken(T token) {
        this.tokenRepository.save(token);
    }

    public Optional<T> getToken(String key) {
        return this.tokenRepository.findById(key);
    }

    public boolean isTokenValid(String key) {
        return this.tokenRepository.existsById(key);
    }

    public void deleteToken(String key) {
        this.tokenRepository.deleteById(key);
    }
}
