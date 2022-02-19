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
 * A Faculty.
 */
@Entity
@Table(name = "faculty")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Faculty implements Serializable {

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

    @OneToMany(mappedBy = "faculty")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subjects", "professions", "faculty" }, allowSetters = true)
    private Set<Specialty> specialties = new HashSet<>();

    @ManyToMany(mappedBy = "faculties")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contacts", "favorites", "comments", "faculties", "city" }, allowSetters = true)
    private Set<University> universities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Faculty id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Faculty name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Faculty description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Specialty> getSpecialties() {
        return this.specialties;
    }

    public void setSpecialties(Set<Specialty> specialties) {
        if (this.specialties != null) {
            this.specialties.forEach(i -> i.setFaculty(null));
        }
        if (specialties != null) {
            specialties.forEach(i -> i.setFaculty(this));
        }
        this.specialties = specialties;
    }

    public Faculty specialties(Set<Specialty> specialties) {
        this.setSpecialties(specialties);
        return this;
    }

    public Faculty addSpecialty(Specialty specialty) {
        this.specialties.add(specialty);
        specialty.setFaculty(this);
        return this;
    }

    public Faculty removeSpecialty(Specialty specialty) {
        this.specialties.remove(specialty);
        specialty.setFaculty(null);
        return this;
    }

    public Set<University> getUniversities() {
        return this.universities;
    }

    public void setUniversities(Set<University> universities) {
        if (this.universities != null) {
            this.universities.forEach(i -> i.removeFaculty(this));
        }
        if (universities != null) {
            universities.forEach(i -> i.addFaculty(this));
        }
        this.universities = universities;
    }

    public Faculty universities(Set<University> universities) {
        this.setUniversities(universities);
        return this;
    }

    public Faculty addUniversity(University university) {
        this.universities.add(university);
        university.getFaculties().add(this);
        return this;
    }

    public Faculty removeUniversity(University university) {
        this.universities.remove(university);
        university.getFaculties().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Faculty)) {
            return false;
        }
        return id != null && id.equals(((Faculty) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Faculty{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
