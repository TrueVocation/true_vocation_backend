package com.truevocation.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A DTO for the {@link com.truevocation.domain.TestResult} entity.
 */
public class TestResultDTO implements Serializable {

    private Long id;

    private AppUserDTO appUser;

    private RecommendationDTO recommendation;

    private ProfTestDTO profTest;

    private List<UserAptitudesDTO> userAptitudes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    public RecommendationDTO getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(RecommendationDTO recommendation) {
        this.recommendation = recommendation;
    }

    public ProfTestDTO getProfTest() {
        return profTest;
    }

    public void setProfTest(ProfTestDTO profTest) {
        this.profTest = profTest;
    }

    public List<UserAptitudesDTO> getUserAptitudes() {
        return userAptitudes;
    }

    public void setUserAptitudes(List<UserAptitudesDTO> userAptitudes) {
        this.userAptitudes = userAptitudes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestResultDTO)) {
            return false;
        }

        TestResultDTO testResultDTO = (TestResultDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, testResultDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestResultDTO{" +
            "id=" + getId() +
            ", appUser=" + getAppUser() +
            ", recommendation=" + getRecommendation() +
            ", profTest=" + getProfTest() +
            "}";
    }
}
