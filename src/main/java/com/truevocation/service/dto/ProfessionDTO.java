package com.truevocation.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.truevocation.domain.Profession} entity.
 */
public class ProfessionDTO implements Serializable {

    private Long id;

    private String name;

    @Size(max = 1000)
    private String description;

    private String employability;

    private Integer averageSalary;

    private String picture;

    private Set<CourseDTO> courses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmployability() {
        return employability;
    }

    public void setEmployability(String employability) {
        this.employability = employability;
    }

    public Integer getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(Integer averageSalary) {
        this.averageSalary = averageSalary;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<CourseDTO> getCourses() {
        return courses;
    }

    public void setCourses(Set<CourseDTO> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionDTO)) {
            return false;
        }

        ProfessionDTO professionDTO = (ProfessionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", employability='" + getEmployability() + "'" +
            ", averageSalary=" + getAverageSalary() +
            ", picture='" + getPicture() + "'" +
            ", courses=" + getCourses() +
            "}";
    }
}
