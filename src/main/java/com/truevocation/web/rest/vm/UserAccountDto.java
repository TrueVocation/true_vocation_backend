package com.truevocation.web.rest.vm;

import java.time.LocalDate;

public class UserAccountDto extends ManagedUserVM {
    private String phoneNumber;
    private LocalDate birthdate;

    public UserAccountDto() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "UserRegisterDto{" +
            "phoneNumber='" + phoneNumber + '\'' +
            ", birthdate=" + birthdate +
            '}';
    }
}
