package pl.owolny.identityprovider.vo;

public record Email(String value) {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    public Email {
        validate(value);
    }

    public static Email of(String value) {
        validate(value);
        return new Email(value);
    }

    private static void validate(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (!value.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Email is not valid");
        }
    }

    public static boolean isValid(String value) {
        return value.matches(EMAIL_REGEX);
    }
}
