package com.truevocation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A City.
 */
@Entity
@Table(name = "city")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "city")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "profession", "city" }, allowSetters = true)
    private Set<DemandProfessionCity> demandProfessionCities = new HashSet<>();

    @OneToMany(mappedBy = "city")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "city", "portfolios" }, allowSetters = true)
    private Set<School> schools = new HashSet<>();

    @OneToMany(mappedBy = "city")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contacts", "favorites", "comments", "faculties", "city" }, allowSetters = true)
    private Set<University> universities = new HashSet<>();

    @OneToMany(mappedBy = "city")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contacts", "city", "professions" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public City id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public City name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<DemandProfessionCity> getDemandProfessionCities() {
        return this.demandProfessionCities;
    }

    public void setDemandProfessionCities(Set<DemandProfessionCity> demandProfessionCities) {
        if (this.demandProfessionCities != null) {
            this.demandProfessionCities.forEach(i -> i.setCity(null));
        }
        if (demandProfessionCities != null) {
            demandProfessionCities.forEach(i -> i.setCity(this));
        }
        this.demandProfessionCities = demandProfessionCities;
    }

    public City demandProfessionCities(Set<DemandProfessionCity> demandProfessionCities) {
        this.setDemandProfessionCities(demandProfessionCities);
        return this;
    }

    public City addDemandProfessionCity(DemandProfessionCity demandProfessionCity) {
        this.demandProfessionCities.add(demandProfessionCity);
        demandProfessionCity.setCity(this);
        return this;
    }

    public City removeDemandProfessionCity(DemandProfessionCity demandProfessionCity) {
        this.demandProfessionCities.remove(demandProfessionCity);
        demandProfessionCity.setCity(null);
        return this;
    }

    public Set<School> getSchools() {
        return this.schools;
    }

    public void setSchools(Set<School> schools) {
        if (this.schools != null) {
            this.schools.forEach(i -> i.setCity(null));
        }
        if (schools != null) {
            schools.forEach(i -> i.setCity(this));
        }
        this.schools = schools;
    }

    public City schools(Set<School> schools) {
        this.setSchools(schools);
        return this;
    }

    public City addSchool(School school) {
        this.schools.add(school);
        school.setCity(this);
        return this;
    }

    public City removeSchool(School school) {
        this.schools.remove(school);
        school.setCity(null);
        return this;
    }

    public Set<University> getUniversities() {
        return this.universities;
    }

    public void setUniversities(Set<University> universities) {
        if (this.universities != null) {
            this.universities.forEach(i -> i.setCity(null));
        }
        if (universities != null) {
            universities.forEach(i -> i.setCity(this));
        }
        this.universities = universities;
    }

    public City universities(Set<University> universities) {
        this.setUniversities(universities);
        return this;
    }

    public City addUniversity(University university) {
        this.universities.add(university);
        university.setCity(this);
        return this;
    }

    public City removeUniversity(University university) {
        this.universities.remove(university);
        university.setCity(null);
        return this;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        if (this.courses != null) {
            this.courses.forEach(i -> i.setCity(null));
        }
        if (courses != null) {
            courses.forEach(i -> i.setCity(this));
        }
        this.courses = courses;
    }

    public City courses(Set<Course> courses) {
        this.setCourses(courses);
        return this;
    }

    public City addCourse(Course course) {
        this.courses.add(course);
        course.setCity(this);
        return this;
    }

    public City removeCourse(Course course) {
        this.courses.remove(course);
        course.setCity(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof City)) {
            return false;
        }
        return id != null && id.equals(((City) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "City{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
