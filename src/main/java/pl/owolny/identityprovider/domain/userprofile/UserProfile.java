package pl.owolny.identityprovider.domain.userprofile;

import jakarta.persistence.*;
import pl.owolny.identityprovider.vo.CountryCode;
import pl.owolny.identityprovider.vo.Gender;
import pl.owolny.identityprovider.vo.PhoneNumber;
import pl.owolny.identityprovider.domain.user.UserId;

import java.time.LocalDate;

@Entity
class UserProfile implements UserProfileInfo {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private UserProfileId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id", unique = true))
    private UserId userId;

    private String pictureUrl;

    private String firstName;

    private String lastName;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "phone_number"))
    private PhoneNumber phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "country_code"))
    private CountryCode countryCode;

    protected UserProfile() {}

    public UserProfile(UserId userId, String pictureUrl, String firstName, String lastName, PhoneNumber phoneNumber, Gender gender, LocalDate birthDate, CountryCode countryCode) {
        this.id = UserProfileId.generate();
        this.userId = userId;
        this.pictureUrl = pictureUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
        this.countryCode = countryCode;
    }

    void updateFirstName(String firstName) {
        this.firstName = firstName;
    }

    void updateLastName(String lastName) {
        this.lastName = lastName;
    }

    void updatePictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    void updatePhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    void updateGender(Gender gender) {
        this.gender = gender;
    }

    void updateBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    void updateCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public UserId getUserId() {
        return this.userId;
    }

    @Override
    public String getPictureUrl() {
        return this.pictureUrl;
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    @Override
    public PhoneNumber getPhoneNumber() {
        return this.phoneNumber;
    }

    @Override
    public Gender getGender() {
        return this.gender;
    }

    @Override
    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    @Override
    public CountryCode getCountryCode() {
        return this.countryCode;
    }
}
