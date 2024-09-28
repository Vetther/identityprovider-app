package pl.owolny.identityprovider.domain;

public interface BaseId<T> {
    T getValue();

    default boolean sameValueAs(BaseId<T> other) {
        return other != null && this.getValue().equals(other.getValue());
    }

    static <T> BaseId<T> of(T value) {
        return () -> value;
    }
}
