package com.truevocation.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.truevocation.domain.Portfolio} entity.
 */
public class PortfolioDTO implements Serializable {

    private Long id;

    private String picture;

    private String gender;

    @Size(max = 1000)
    private String hobby;

    @Size(max = 1000)
    private String aboutMe;

    private AppUserDTO appUser;

    private Set<LanguageDTO> languages = new HashSet<>();

    private Set<SchoolDTO> schools = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    public Set<LanguageDTO> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<LanguageDTO> languages) {
        this.languages = languages;
    }

    public Set<SchoolDTO> getSchools() {
        return schools;
    }

    public void setSchools(Set<SchoolDTO> schools) {
        this.schools = schools;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PortfolioDTO)) {
            return false;
        }

        PortfolioDTO portfolioDTO = (PortfolioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, portfolioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PortfolioDTO{" +
            "id=" + getId() +
            ", picture='" + getPicture() + "'" +
            ", gender='" + getGender() + "'" +
            ", hobby='" + getHobby() + "'" +
            ", aboutMe='" + getAboutMe() + "'" +
            ", appUser=" + getAppUser() +
            ", languages=" + getLanguages() +
            ", schools=" + getSchools() +
            "}";
    }
}
