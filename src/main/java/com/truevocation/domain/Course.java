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
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "picture")
    private String picture;

    @OneToMany(mappedBy = "course")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "course", "university", "portfolio" }, allowSetters = true)
    private Set<Contact> contacts = new HashSet<>();

    @OneToMany(mappedBy = "course")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "course", "university", "portfolio" }, allowSetters = true)
    private Set<Pictures> pictures = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "demandProfessionCities", "schools", "universities", "courses" }, allowSetters = true)
    private City city;

    @ManyToMany(mappedBy = "courses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "demandProfessionCities", "recommendations", "courses", "specialties" }, allowSetters = true)
    private Set<Profession> professions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Course id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Course name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Course description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return this.picture;
    }

    public Course picture(String picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        if (this.contacts != null) {
            this.contacts.forEach(i -> i.setCourse(null));
        }
        if (contacts != null) {
            contacts.forEach(i -> i.setCourse(this));
        }
        this.contacts = contacts;
    }

    public Course contacts(Set<Contact> contacts) {
        this.setContacts(contacts);
        return this;
    }

    public Course addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setCourse(this);
        return this;
    }

    public Course removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.setCourse(null);
        return this;
    }

    public Set<Pictures> getPictures() {
        return this.pictures;
    }

    public void setPictures(Set<Pictures> pictures) {
        if (this.pictures != null) {
            this.pictures.forEach(i -> i.setCourse(null));
        }
        if (pictures != null) {
            pictures.forEach(i -> i.setCourse(this));
        }
        this.pictures = pictures;
    }

    public Course pictures(Set<Pictures> pictures) {
        this.setPictures(pictures);
        return this;
    }

    public Course addPictures(Pictures pictures) {
        this.pictures.add(pictures);
        pictures.setCourse(this);
        return this;
    }

    public Course removePictures(Pictures pictures) {
        this.pictures.remove(pictures);
        pictures.setCourse(null);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Course city(City city) {
        this.setCity(city);
        return this;
    }

    public Set<Profession> getProfessions() {
        return this.professions;
    }

    public void setProfessions(Set<Profession> professions) {
        if (this.professions != null) {
            this.professions.forEach(i -> i.removeCourse(this));
        }
        if (professions != null) {
            professions.forEach(i -> i.addCourse(this));
        }
        this.professions = professions;
    }

    public Course professions(Set<Profession> professions) {
        this.setProfessions(professions);
        return this;
    }

    public Course addProfession(Profession profession) {
        this.professions.add(profession);
        profession.getCourses().add(this);
        return this;
    }

    public Course removeProfession(Profession profession) {
        this.professions.remove(profession);
        profession.getCourses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", picture='" + getPicture() + "'" +
            "}";
    }
}
