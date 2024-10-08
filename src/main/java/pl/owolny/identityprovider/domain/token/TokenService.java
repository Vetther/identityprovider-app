package pl.owolny.identityprovider.domain.token;

import java.util.Optional;

abstract class TokenService<T extends Token> {

    private final TokenRepository<T> tokenRepository;

    public TokenService(TokenRepository<T> tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    protected void createToken(T token) {
        this.tokenRepository.save(token);
    }

    protected Optional<T> getToken(String key) {
        return this.tokenRepository.findById(key);
    }

    protected boolean isTokenValid(String key) {
        return this.tokenRepository.existsById(key);
    }

    protected void deleteToken(String key) {
        this.tokenRepository.deleteById(key);
    }
}
