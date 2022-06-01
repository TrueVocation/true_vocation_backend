package com.truevocation.service.dto;

import com.truevocation.domain.AppUser;
import com.truevocation.domain.User;

import java.time.LocalDate;

/**
 * A DTO representing a user, with only the public attributes.
 */
public class UserDTO {

    private Long id;

    private String login;

    private String firstName;

    private String lastName;

    private String email;

    private String imageUrl;

    private String phoneNumber;

    private LocalDate birthdate;


    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user) {
        this.id = user.getId();
        // Customize it here if you need, or not, firstName/lastName/etc
        this.login = user.getLogin();
    }

    public UserDTO(AppUser appUser) {
        this.id = appUser.getUser().getId();
        this.login = appUser.getUser().getLogin();
        this.firstName = appUser.getUser().getFirstName();
        this.lastName = appUser.getUser().getLastName();
        this.email = appUser.getUser().getEmail();
        this.imageUrl = appUser.getUser().getImageUrl();
        this.phoneNumber = appUser.getPhoneNumber();
        this.birthdate = appUser.getBirthdate();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDTO{" +
            "id='" + id + '\'' +
            ", login='" + login + '\'' +
            "}";
    }
}
