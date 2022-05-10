package com.truevocation.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.truevocation.domain.Pictures} entity.
 */
public class PicturesDTO implements Serializable {

    private Long id;

    @Size(max = 1000)
    private String picture;

    private String url;

    private CourseDTO course;

    private UniversityDTO university;

    private PortfolioDTO portfolio;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public UniversityDTO getUniversity() {
        return university;
    }

    public void setUniversity(UniversityDTO university) {
        this.university = university;
    }

    public PortfolioDTO getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(PortfolioDTO portfolio) {
        this.portfolio = portfolio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PicturesDTO)) {
            return false;
        }

        PicturesDTO picturesDTO = (PicturesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, picturesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PicturesDTO{" +
            "id=" + getId() +
            ", picture='" + getPicture() + "'" +
            ", url='" + getUrl() + "'" +
            ", course=" + getCourse() +
            ", university=" + getUniversity() +
            ", portfolio=" + getPortfolio() +
            "}";
    }
}
