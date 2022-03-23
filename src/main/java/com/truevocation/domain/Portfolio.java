package com.truevocation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Portfolio.
 */
@Entity
@Table(name = "portfolio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Portfolio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "picture")
    private String picture;

    @Column(name = "gender")
    private String gender;

    @Size(max = 1000)
    @Column(name = "hobby", length = 1000)
    private String hobby;

    @Size(max = 1000)
    @Column(name = "about_me", length = 1000)
    private String aboutMe;

    @JsonIgnoreProperties(
        value = { "user", "comments", "favorites", "commentAnswers", "likes", "portfolio", "testResult" },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private AppUser appUser;

    @OneToMany(mappedBy = "portfolio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "course", "university", "portfolio" }, allowSetters = true)
    private Set<Contact> contacts = new HashSet<>();

    @OneToMany(mappedBy = "portfolio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "portfolio" }, allowSetters = true)
    private Set<Achievement> achievements = new HashSet<>();

    @OneToMany(mappedBy = "portfolio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "course", "university", "portfolio" }, allowSetters = true)
    private Set<Pictures> pictures = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_portfolio__language",
        joinColumns = @JoinColumn(name = "portfolio_id"),
        inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "portfolios" }, allowSetters = true)
    private Set<Language> languages = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_portfolio__school",
        joinColumns = @JoinColumn(name = "portfolio_id"),
        inverseJoinColumns = @JoinColumn(name = "school_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "city", "portfolios" }, allowSetters = true)
    private Set<School> schools = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Portfolio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicture() {
        return this.picture;
    }

    public Portfolio picture(String picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getGender() {
        return this.gender;
    }

    public Portfolio gender(String gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHobby() {
        return this.hobby;
    }

    public Portfolio hobby(String hobby) {
        this.setHobby(hobby);
        return this;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getAboutMe() {
        return this.aboutMe;
    }

    public Portfolio aboutMe(String aboutMe) {
        this.setAboutMe(aboutMe);
        return this;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Portfolio appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        if (this.contacts != null) {
            this.contacts.forEach(i -> i.setPortfolio(null));
        }
        if (contacts != null) {
            contacts.forEach(i -> i.setPortfolio(this));
        }
        this.contacts = contacts;
    }

    public Portfolio contacts(Set<Contact> contacts) {
        this.setContacts(contacts);
        return this;
    }

    public Portfolio addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setPortfolio(this);
        return this;
    }

    public Portfolio removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.setPortfolio(null);
        return this;
    }

    public Set<Achievement> getAchievements() {
        return this.achievements;
    }

    public void setAchievements(Set<Achievement> achievements) {
        if (this.achievements != null) {
            this.achievements.forEach(i -> i.setPortfolio(null));
        }
        if (achievements != null) {
            achievements.forEach(i -> i.setPortfolio(this));
        }
        this.achievements = achievements;
    }

    public Portfolio achievements(Set<Achievement> achievements) {
        this.setAchievements(achievements);
        return this;
    }

    public Portfolio addAchievement(Achievement achievement) {
        this.achievements.add(achievement);
        achievement.setPortfolio(this);
        return this;
    }

    public Portfolio removeAchievement(Achievement achievement) {
        this.achievements.remove(achievement);
        achievement.setPortfolio(null);
        return this;
    }

    public Set<Pictures> getPictures() {
        return this.pictures;
    }

    public void setPictures(Set<Pictures> pictures) {
        if (this.pictures != null) {
            this.pictures.forEach(i -> i.setPortfolio(null));
        }
        if (pictures != null) {
            pictures.forEach(i -> i.setPortfolio(this));
        }
        this.pictures = pictures;
    }

    public Portfolio pictures(Set<Pictures> pictures) {
        this.setPictures(pictures);
        return this;
    }

    public Portfolio addPictures(Pictures pictures) {
        this.pictures.add(pictures);
        pictures.setPortfolio(this);
        return this;
    }

    public Portfolio removePictures(Pictures pictures) {
        this.pictures.remove(pictures);
        pictures.setPortfolio(null);
        return this;
    }

    public Set<Language> getLanguages() {
        return this.languages;
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    public Portfolio languages(Set<Language> languages) {
        this.setLanguages(languages);
        return this;
    }

    public Portfolio addLanguage(Language language) {
        this.languages.add(language);
        language.getPortfolios().add(this);
        return this;
    }

    public Portfolio removeLanguage(Language language) {
        this.languages.remove(language);
        language.getPortfolios().remove(this);
        return this;
    }

    public Set<School> getSchools() {
        return this.schools;
    }

    public void setSchools(Set<School> schools) {
        this.schools = schools;
    }

    public Portfolio schools(Set<School> schools) {
        this.setSchools(schools);
        return this;
    }

    public Portfolio addSchool(School school) {
        this.schools.add(school);
        school.getPortfolios().add(this);
        return this;
    }

    public Portfolio removeSchool(School school) {
        this.schools.remove(school);
        school.getPortfolios().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Portfolio)) {
            return false;
        }
        return id != null && id.equals(((Portfolio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Portfolio{" +
            "id=" + getId() +
            ", picture='" + getPicture() + "'" +
            ", gender='" + getGender() + "'" +
            ", hobby='" + getHobby() + "'" +
            ", aboutMe='" + getAboutMe() + "'" +
            "}";
    }
}
