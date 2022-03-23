package com.truevocation.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.truevocation.domain.Recommendation} entity.
 */
public class RecommendationDTO implements Serializable {

    private Long id;

    private ProfessionDTO profession;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfessionDTO getProfession() {
        return profession;
    }

    public void setProfession(ProfessionDTO profession) {
        this.profession = profession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecommendationDTO)) {
            return false;
        }

        RecommendationDTO recommendationDTO = (RecommendationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recommendationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecommendationDTO{" +
            "id=" + getId() +
            ", profession=" + getProfession() +
            "}";
    }
}
