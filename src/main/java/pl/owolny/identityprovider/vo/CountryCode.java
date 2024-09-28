package pl.owolny.identityprovider.vo;

public record CountryCode(String value) {

    public CountryCode {
        if (value == null) {
            throw new IllegalArgumentException("Country code cannot be null");
        }
        if (!value.matches("^[A-Z]{2}$")) {
            throw new IllegalArgumentException("Country code is not valid");
        }
    }
}
