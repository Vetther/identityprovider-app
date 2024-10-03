package pl.owolny.identityprovider.infrastructure.authentication.oauth2;

import pl.owolny.identityprovider.domain.federatedidentity.FederatedIdentityInfo;
import pl.owolny.identityprovider.domain.user.UserId;
import pl.owolny.identityprovider.domain.userprofile.UserProfileInfo;
import pl.owolny.identityprovider.vo.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record OAuth2UserInfo(
        IdentityProvider provider,
        String externalId,
        String externalUsername,
        Email externalEmail,
        boolean isExternalEmailVerified,
        String pictureUrl,
        String firstName,
        String lastName,
        PhoneNumber phoneNumber,
        Gender gender,
        LocalDate birthDate,
        CountryCode countryCode
) implements FederatedIdentityInfo, UserProfileInfo {

    @Override
    public UserId getUserId() {
        return null;
    }

    @Override
    public String getPictureUrl() {
        return pictureUrl;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public LocalDate getBirthDate() {
        return birthDate;
    }

    @Override
    public CountryCode getCountryCode() {
        return countryCode;
    }

    @Override
    public IdentityProvider getProvider() {
        return provider;
    }

    @Override
    public String getExternalId() {
        return externalId;
    }

    @Override
    public String getExternalUsername() {
        return externalUsername;
    }

    @Override
    public Email getExternalEmail() {
        return externalEmail;
    }

    @Override
    public boolean isExternalEmailVerified() {
        return isExternalEmailVerified;
    }

    @Override
    public LocalDateTime getConnectedAt() {
        return null;
    }
}
