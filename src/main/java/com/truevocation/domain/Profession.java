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
 * A Profession.
 */
@Entity
@Table(name = "profession")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Profession implements Serializable {

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

    @Column(name = "employability")
    private String employability;

    @Column(name = "average_salary")
    private Integer averageSalary;

    @Column(name = "picture")
    private String picture;

    @OneToMany(mappedBy = "profession")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "profession", "city" }, allowSetters = true)
    private Set<DemandProfessionCity> demandProfessionCities = new HashSet<>();

    @OneToMany(mappedBy = "profession")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "testResult", "profession" }, allowSetters = true)
    private Set<Recommendation> recommendations = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_profession__course",
        joinColumns = @JoinColumn(name = "profession_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contacts", "city", "professions" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    @ManyToMany(mappedBy = "professions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subjects", "professions", "faculty" }, allowSetters = true)
    private Set<Specialty> specialties = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Profession id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Profession name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Profession description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmployability() {
        return this.employability;
    }

    public Profession employability(String employability) {
        this.setEmployability(employability);
        return this;
    }

    public void setEmployability(String employability) {
        this.employability = employability;
    }

    public Integer getAverageSalary() {
        return this.averageSalary;
    }

    public Profession averageSalary(Integer averageSalary) {
        this.setAverageSalary(averageSalary);
        return this;
    }

    public void setAverageSalary(Integer averageSalary) {
        this.averageSalary = averageSalary;
    }

    public String getPicture() {
        return this.picture;
    }

    public Profession picture(String picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<DemandProfessionCity> getDemandProfessionCities() {
        return this.demandProfessionCities;
    }

    public void setDemandProfessionCities(Set<DemandProfessionCity> demandProfessionCities) {
        if (this.demandProfessionCities != null) {
            this.demandProfessionCities.forEach(i -> i.setProfession(null));
        }
        if (demandProfessionCities != null) {
            demandProfessionCities.forEach(i -> i.setProfession(this));
        }
        this.demandProfessionCities = demandProfessionCities;
    }

    public Profession demandProfessionCities(Set<DemandProfessionCity> demandProfessionCities) {
        this.setDemandProfessionCities(demandProfessionCities);
        return this;
    }

    public Profession addDemandProfessionCity(DemandProfessionCity demandProfessionCity) {
        this.demandProfessionCities.add(demandProfessionCity);
        demandProfessionCity.setProfession(this);
        return this;
    }

    public Profession removeDemandProfessionCity(DemandProfessionCity demandProfessionCity) {
        this.demandProfessionCities.remove(demandProfessionCity);
        demandProfessionCity.setProfession(null);
        return this;
    }

    public Set<Recommendation> getRecommendations() {
        return this.recommendations;
    }

    public void setRecommendations(Set<Recommendation> recommendations) {
        if (this.recommendations != null) {
            this.recommendations.forEach(i -> i.setProfession(null));
        }
        if (recommendations != null) {
            recommendations.forEach(i -> i.setProfession(this));
        }
        this.recommendations = recommendations;
    }

    public Profession recommendations(Set<Recommendation> recommendations) {
        this.setRecommendations(recommendations);
        return this;
    }

    public Profession addRecommendation(Recommendation recommendation) {
        this.recommendations.add(recommendation);
        recommendation.setProfession(this);
        return this;
    }

    public Profession removeRecommendation(Recommendation recommendation) {
        this.recommendations.remove(recommendation);
        recommendation.setProfession(null);
        return this;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Profession courses(Set<Course> courses) {
        this.setCourses(courses);
        return this;
    }

    public Profession addCourse(Course course) {
        this.courses.add(course);
        course.getProfessions().add(this);
        return this;
    }

    public Profession removeCourse(Course course) {
        this.courses.remove(course);
        course.getProfessions().remove(this);
        return this;
    }

    public Set<Specialty> getSpecialties() {
        return this.specialties;
    }

    public void setSpecialties(Set<Specialty> specialties) {
        if (this.specialties != null) {
            this.specialties.forEach(i -> i.removeProfession(this));
        }
        if (specialties != null) {
            specialties.forEach(i -> i.addProfession(this));
        }
        this.specialties = specialties;
    }

    public Profession specialties(Set<Specialty> specialties) {
        this.setSpecialties(specialties);
        return this;
    }

    public Profession addSpecialty(Specialty specialty) {
        this.specialties.add(specialty);
        specialty.getProfessions().add(this);
        return this;
    }

    public Profession removeSpecialty(Specialty specialty) {
        this.specialties.remove(specialty);
        specialty.getProfessions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profession)) {
            return false;
        }
        return id != null && id.equals(((Profession) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Profession{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", employability='" + getEmployability() + "'" +
            ", averageSalary=" + getAverageSalary() +
            ", picture='" + getPicture() + "'" +
            "}";
    }
}
