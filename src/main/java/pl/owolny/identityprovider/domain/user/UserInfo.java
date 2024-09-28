package pl.owolny.identityprovider.domain.user;

import pl.owolny.identityprovider.vo.Email;

public interface UserInfo {

    UserId getId();

    Email getEmail();

    String getUsername();

    boolean isEmailVerified();

}
