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
 * A University.
 */
@Entity
@Table(name = "university")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class University implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "dormitory")
    private Boolean dormitory;

    @Column(name = "military")
    private Boolean military;

    @Column(name = "status")
    private String status;

    @Column(name = "code")
    private String code;

    @Column(name = "logo")
    private String logo;

    @OneToMany(mappedBy = "university")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "course", "university", "portfolio" }, allowSetters = true)
    private Set<Contact> contacts = new HashSet<>();

    @OneToMany(mappedBy = "university")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "university", "user", "post" }, allowSetters = true)
    private Set<Favorite> favorites = new HashSet<>();

    @OneToMany(mappedBy = "university")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "likes", "commentAnswers", "university", "user", "post" }, allowSetters = true)
    private Set<Comments> comments = new HashSet<>();

    @OneToMany(mappedBy = "university")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "course", "university", "portfolio" }, allowSetters = true)
    private Set<Pictures> pictures = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_university__faculty",
        joinColumns = @JoinColumn(name = "university_id"),
        inverseJoinColumns = @JoinColumn(name = "faculty_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "specialties", "universities" }, allowSetters = true)
    private Set<Faculty> faculties = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "demandProfessionCities", "schools", "universities", "courses" }, allowSetters = true)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public University id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public University name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public University address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return this.description;
    }

    public University description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDormitory() {
        return this.dormitory;
    }

    public University dormitory(Boolean dormitory) {
        this.setDormitory(dormitory);
        return this;
    }

    public void setDormitory(Boolean dormitory) {
        this.dormitory = dormitory;
    }

    public Boolean getMilitary() {
        return this.military;
    }

    public University military(Boolean military) {
        this.setMilitary(military);
        return this;
    }

    public void setMilitary(Boolean military) {
        this.military = military;
    }

    public String getStatus() {
        return this.status;
    }

    public University status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return this.code;
    }

    public University code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLogo() {
        return this.logo;
    }

    public University logo(String logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        if (this.contacts != null) {
            this.contacts.forEach(i -> i.setUniversity(null));
        }
        if (contacts != null) {
            contacts.forEach(i -> i.setUniversity(this));
        }
        this.contacts = contacts;
    }

    public University contacts(Set<Contact> contacts) {
        this.setContacts(contacts);
        return this;
    }

    public University addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setUniversity(this);
        return this;
    }

    public University removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.setUniversity(null);
        return this;
    }

    public Set<Favorite> getFavorites() {
        return this.favorites;
    }

    public void setFavorites(Set<Favorite> favorites) {
        if (this.favorites != null) {
            this.favorites.forEach(i -> i.setUniversity(null));
        }
        if (favorites != null) {
            favorites.forEach(i -> i.setUniversity(this));
        }
        this.favorites = favorites;
    }

    public University favorites(Set<Favorite> favorites) {
        this.setFavorites(favorites);
        return this;
    }

    public University addFavorite(Favorite favorite) {
        this.favorites.add(favorite);
        favorite.setUniversity(this);
        return this;
    }

    public University removeFavorite(Favorite favorite) {
        this.favorites.remove(favorite);
        favorite.setUniversity(null);
        return this;
    }

    public Set<Comments> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comments> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setUniversity(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setUniversity(this));
        }
        this.comments = comments;
    }

    public University comments(Set<Comments> comments) {
        this.setComments(comments);
        return this;
    }

    public University addComments(Comments comments) {
        this.comments.add(comments);
        comments.setUniversity(this);
        return this;
    }

    public University removeComments(Comments comments) {
        this.comments.remove(comments);
        comments.setUniversity(null);
        return this;
    }

    public Set<Pictures> getPictures() {
        return this.pictures;
    }

    public void setPictures(Set<Pictures> pictures) {
        if (this.pictures != null) {
            this.pictures.forEach(i -> i.setUniversity(null));
        }
        if (pictures != null) {
            pictures.forEach(i -> i.setUniversity(this));
        }
        this.pictures = pictures;
    }

    public University pictures(Set<Pictures> pictures) {
        this.setPictures(pictures);
        return this;
    }

    public University addPictures(Pictures pictures) {
        this.pictures.add(pictures);
        pictures.setUniversity(this);
        return this;
    }

    public University removePictures(Pictures pictures) {
        this.pictures.remove(pictures);
        pictures.setUniversity(null);
        return this;
    }

    public Set<Faculty> getFaculties() {
        return this.faculties;
    }

    public void setFaculties(Set<Faculty> faculties) {
        this.faculties = faculties;
    }

    public University faculties(Set<Faculty> faculties) {
        this.setFaculties(faculties);
        return this;
    }

    public University addFaculty(Faculty faculty) {
        this.faculties.add(faculty);
        faculty.getUniversities().add(this);
        return this;
    }

    public University removeFaculty(Faculty faculty) {
        this.faculties.remove(faculty);
        faculty.getUniversities().remove(this);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public University city(City city) {
        this.setCity(city);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof University)) {
            return false;
        }
        return id != null && id.equals(((University) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "University{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", description='" + getDescription() + "'" +
            ", dormitory='" + getDormitory() + "'" +
            ", military='" + getMilitary() + "'" +
            ", status='" + getStatus() + "'" +
            ", code='" + getCode() + "'" +
            ", logo='" + getLogo() + "'" +
            "}";
    }
}
