package com.truevocation.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.truevocation.domain.Specialty} entity.
 */
public class SpecialtyDTO implements Serializable {

    private Long id;

    private String name;

    @Size(max = 1000)
    private String description;

    private Integer totalGrants;

    private Integer minScoreGeneral;

    private Integer minScoreQuota;

    private String picture;

    private Set<SubjectDTO> subjects = new HashSet<>();

    private Set<ProfessionDTO> professions = new HashSet<>();

    private FacultyDTO faculty;

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

    public Integer getTotalGrants() {
        return totalGrants;
    }

    public void setTotalGrants(Integer totalGrants) {
        this.totalGrants = totalGrants;
    }

    public Integer getMinScoreGeneral() {
        return minScoreGeneral;
    }

    public void setMinScoreGeneral(Integer minScoreGeneral) {
        this.minScoreGeneral = minScoreGeneral;
    }

    public Integer getMinScoreQuota() {
        return minScoreQuota;
    }

    public void setMinScoreQuota(Integer minScoreQuota) {
        this.minScoreQuota = minScoreQuota;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<SubjectDTO> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<SubjectDTO> subjects) {
        this.subjects = subjects;
    }

    public Set<ProfessionDTO> getProfessions() {
        return professions;
    }

    public void setProfessions(Set<ProfessionDTO> professions) {
        this.professions = professions;
    }

    public FacultyDTO getFaculty() {
        return faculty;
    }

    public void setFaculty(FacultyDTO faculty) {
        this.faculty = faculty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpecialtyDTO)) {
            return false;
        }

        SpecialtyDTO specialtyDTO = (SpecialtyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, specialtyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpecialtyDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", totalGrants=" + getTotalGrants() +
            ", minScoreGeneral=" + getMinScoreGeneral() +
            ", minScoreQuota=" + getMinScoreQuota() +
            ", picture='" + getPicture() + "'" +
            ", subjects=" + getSubjects() +
            ", professions=" + getProfessions() +
            ", faculty=" + getFaculty() +
            "}";
    }
}
