package com.truevocation.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.truevocation.domain.DemandProfessionCity} entity.
 */
public class DemandProfessionCityDTO implements Serializable {

    private Long id;

    private Double actualInPercent;

    private ProfessionDTO profession;

    private CityDTO city;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getActualInPercent() {
        return actualInPercent;
    }

    public void setActualInPercent(Double actualInPercent) {
        this.actualInPercent = actualInPercent;
    }

    public ProfessionDTO getProfession() {
        return profession;
    }

    public void setProfession(ProfessionDTO profession) {
        this.profession = profession;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemandProfessionCityDTO)) {
            return false;
        }

        DemandProfessionCityDTO demandProfessionCityDTO = (DemandProfessionCityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, demandProfessionCityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandProfessionCityDTO{" +
            "id=" + getId() +
            ", actualInPercent=" + getActualInPercent() +
            ", profession=" + getProfession() +
            ", city=" + getCity() +
            "}";
    }
}
