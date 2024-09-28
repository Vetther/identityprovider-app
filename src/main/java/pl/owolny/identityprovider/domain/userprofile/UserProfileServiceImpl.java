package pl.owolny.identityprovider.domain.userprofile;

import org.springframework.transaction.annotation.Transactional;
import pl.owolny.identityprovider.vo.CountryCode;
import pl.owolny.identityprovider.vo.Gender;
import pl.owolny.identityprovider.vo.PhoneNumber;
import pl.owolny.identityprovider.domain.user.UserId;

import java.time.LocalDate;

class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserProfileInfo createNew(UserId userId, String firstName, String lastName, String pictureUrl, PhoneNumber phoneNumber, Gender gender, LocalDate birthDate, CountryCode countryCode) {
        UserProfile userProfile = new UserProfile(userId, pictureUrl, firstName, lastName, phoneNumber, gender, birthDate, countryCode);
        return userProfileRepository.save(userProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileInfo getByUserId(UserId userId) {
        return userProfileRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("User profile with id " + userId + " not found"));
    }

    @Override
    @Transactional
    public void updateFirstName(UserId userId, String firstName) {
        UserProfile userProfile = getUserProfile(userId);
        userProfile.updateFirstName(firstName);
    }

    @Override
    @Transactional
    public void updateLastName(UserId userId, String lastName) {
        UserProfile userProfile = getUserProfile(userId);
        userProfile.updateLastName(lastName);
    }

    @Override
    @Transactional
    public void updatePictureUrl(UserId userId, String pictureUrl) {
        UserProfile userProfile = getUserProfile(userId);
        userProfile.updatePictureUrl(pictureUrl);
    }

    @Override
    @Transactional
    public void updatePhoneNumber(UserId userId, PhoneNumber phoneNumber) {
        UserProfile userProfile = getUserProfile(userId);
        userProfile.updatePhoneNumber(phoneNumber);
    }

    @Override
    @Transactional
    public void updateGender(UserId userId, Gender gender) {
        UserProfile userProfile = getUserProfile(userId);
        userProfile.updateGender(gender);
    }

    @Override
    @Transactional
    public void updateBirthDate(UserId userId, LocalDate birthDate) {
        UserProfile userProfile = getUserProfile(userId);
        userProfile.updateBirthDate(birthDate);
    }

    @Override
    @Transactional
    public void updateCountryCode(UserId userId, CountryCode countryCode) {
        UserProfile userProfile = getUserProfile(userId);
        userProfile.updateCountryCode(countryCode);
    }

    @Transactional(readOnly = true)
    protected UserProfile getUserProfile(UserId userId) {
        return userProfileRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("User profile with id " + userId + " not found"));
    }
}
