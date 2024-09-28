package pl.owolny.identityprovider.domain.userprofile;

import pl.owolny.identityprovider.vo.CountryCode;
import pl.owolny.identityprovider.vo.Gender;
import pl.owolny.identityprovider.vo.PhoneNumber;
import pl.owolny.identityprovider.domain.user.UserId;

import java.time.LocalDate;

public interface UserProfileInfo {

    UserId getUserId();

    String getPictureUrl();

    String getFirstName();

    String getLastName();

    PhoneNumber getPhoneNumber();

    Gender getGender();

    LocalDate getBirthDate();

    CountryCode getCountryCode();
}
