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
 * A Specialty.
 */
@Entity
@Table(name = "specialty")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Specialty implements Serializable {

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

    @Column(name = "total_grants")
    private Integer totalGrants;

    @Column(name = "min_score_general")
    private Integer minScoreGeneral;

    @Column(name = "min_score_quota")
    private Integer minScoreQuota;

    @Column(name = "picture")
    private String picture;

    @Column(name = "type")
    private String type;

    @Column(name = "price")
    private Integer price;

    @Column(name = "employment")
    private String employment;

    @ManyToMany
    @JoinTable(
        name = "rel_specialty__subject",
        joinColumns = @JoinColumn(name = "specialty_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "specialties" }, allowSetters = true)
    private Set<Subject> subjects = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_specialty__profession",
        joinColumns = @JoinColumn(name = "specialty_id"),
        inverseJoinColumns = @JoinColumn(name = "profession_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "demandProfessionCities", "recommendations", "courses", "specialties" }, allowSetters = true)
    private Set<Profession> professions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "specialties", "universities" }, allowSetters = true)
    private Faculty faculty;

    @OneToMany(mappedBy = "specialty")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "university", "user", "post", "specialty", "profession" }, allowSetters = true)
    private Set<Favorite> favorites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here


    public Set<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Favorite> favorites) {
        if (this.favorites != null) {
            this.favorites.forEach(i -> i.setSpecialty(null));
        }
        if (favorites != null) {
            favorites.forEach(i -> i.setSpecialty(this));
        }
        this.favorites = favorites;
    }

    public Specialty favorites(Set<Favorite> favorites) {
        this.setFavorites(favorites);
        return this;
    }

    public Specialty addFavorite(Favorite favorite) {
        this.favorites.add(favorite);
        favorite.setSpecialty(this);
        return this;
    }

    public Specialty removeFavorite(Favorite favorite) {
        this.favorites.remove(favorite);
        favorite.setSpecialty(null);
        return this;
    }

    public String getEmployment() {
        return employment;
    }

    public void setEmployment(String employment) {
        this.employment = employment;
    }

    public Long getId() {
        return this.id;
    }

    public Specialty id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Specialty name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Specialty description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalGrants() {
        return this.totalGrants;
    }

    public Specialty totalGrants(Integer totalGrants) {
        this.setTotalGrants(totalGrants);
        return this;
    }

    public void setTotalGrants(Integer totalGrants) {
        this.totalGrants = totalGrants;
    }

    public Integer getMinScoreGeneral() {
        return this.minScoreGeneral;
    }

    public Specialty minScoreGeneral(Integer minScoreGeneral) {
        this.setMinScoreGeneral(minScoreGeneral);
        return this;
    }

    public void setMinScoreGeneral(Integer minScoreGeneral) {
        this.minScoreGeneral = minScoreGeneral;
    }

    public Integer getMinScoreQuota() {
        return this.minScoreQuota;
    }

    public Specialty minScoreQuota(Integer minScoreQuota) {
        this.setMinScoreQuota(minScoreQuota);
        return this;
    }

    public void setMinScoreQuota(Integer minScoreQuota) {
        this.minScoreQuota = minScoreQuota;
    }

    public String getPicture() {
        return this.picture;
    }

    public Specialty picture(String picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType() {
        return this.type;
    }

    public Specialty type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Subject> getSubjects() {
        return this.subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public Specialty subjects(Set<Subject> subjects) {
        this.setSubjects(subjects);
        return this;
    }

    public Specialty addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.getSpecialties().add(this);
        return this;
    }

    public Specialty removeSubject(Subject subject) {
        this.subjects.remove(subject);
        subject.getSpecialties().remove(this);
        return this;
    }

    public Set<Profession> getProfessions() {
        return this.professions;
    }

    public void setProfessions(Set<Profession> professions) {
        this.professions = professions;
    }

    public Specialty professions(Set<Profession> professions) {
        this.setProfessions(professions);
        return this;
    }

    public Specialty addProfession(Profession profession) {
        this.professions.add(profession);
        profession.getSpecialties().add(this);
        return this;
    }

    public Specialty removeProfession(Profession profession) {
        this.professions.remove(profession);
        profession.getSpecialties().remove(this);
        return this;
    }

    public Faculty getFaculty() {
        return this.faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Specialty faculty(Faculty faculty) {
        this.setFaculty(faculty);
        return this;
    }

    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Specialty)) {
            return false;
        }
        return id != null && id.equals(((Specialty) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Specialty{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", totalGrants=" + getTotalGrants() +
            ", minScoreGeneral=" + getMinScoreGeneral() +
            ", minScoreQuota=" + getMinScoreQuota() +
            ", picture='" + getPicture() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
