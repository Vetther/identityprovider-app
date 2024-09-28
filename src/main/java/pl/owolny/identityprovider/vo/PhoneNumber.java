package pl.owolny.identityprovider.vo;

public record PhoneNumber(String value) {

    public PhoneNumber {
        if (value == null) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }
        if (!value.matches("^\\+[0-9]{1,3}[0-9]{9}$")) {
            throw new IllegalArgumentException("Phone number must be in the format +[country code][phone number]");
        }
    }
}
