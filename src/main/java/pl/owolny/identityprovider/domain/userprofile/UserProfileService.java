package pl.owolny.identityprovider.domain.userprofile;

import pl.owolny.identityprovider.vo.CountryCode;
import pl.owolny.identityprovider.vo.Gender;
import pl.owolny.identityprovider.vo.PhoneNumber;
import pl.owolny.identityprovider.domain.user.UserId;

import java.time.LocalDate;

public interface UserProfileService {

    UserProfileInfo createNew(UserId userId, String firstName, String lastName, String pictureUrl, PhoneNumber phoneNumber, Gender gender, LocalDate birthDate, CountryCode countryCode);

    UserProfileInfo getByUserId(UserId userId);

    void updateFirstName(UserId userId, String firstName);

    void updateLastName(UserId userId, String lastName);

    void updatePictureUrl(UserId userId, String pictureUrl);

    void updatePhoneNumber(UserId userId, PhoneNumber phoneNumber);

    void updateGender(UserId userId, Gender gender);

    void updateBirthDate(UserId userId, LocalDate birthDate);

    void updateCountryCode(UserId userId, CountryCode countryCode);
}
