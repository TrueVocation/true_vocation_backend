package com.truevocation.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.truevocation.domain.Achievement} entity.
 */
public class AchievementDTO implements Serializable {

    private Long id;

    private String name;

    private String type;

    private LocalDate receivedDate;

    private String orientation;

    private PortfolioDTO portfolio;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
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
        if (!(o instanceof AchievementDTO)) {
            return false;
        }

        AchievementDTO achievementDTO = (AchievementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, achievementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AchievementDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", receivedDate='" + getReceivedDate() + "'" +
            ", orientation='" + getOrientation() + "'" +
            ", portfolio=" + getPortfolio() +
            "}";
    }
}
