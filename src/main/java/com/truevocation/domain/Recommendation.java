package com.truevocation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Recommendation.
 */
@Entity
@Table(name = "recommendation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Recommendation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "appUser", "recommendation", "answerUsers", "profTest" }, allowSetters = true)
    @OneToOne(mappedBy = "recommendation")
    private TestResult testResult;

    @ManyToOne
    @JsonIgnoreProperties(value = { "demandProfessionCities", "recommendations", "courses", "specialties" }, allowSetters = true)
    private Profession profession;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Recommendation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestResult getTestResult() {
        return this.testResult;
    }

    public void setTestResult(TestResult testResult) {
        if (this.testResult != null) {
            this.testResult.setRecommendation(null);
        }
        if (testResult != null) {
            testResult.setRecommendation(this);
        }
        this.testResult = testResult;
    }

    public Recommendation testResult(TestResult testResult) {
        this.setTestResult(testResult);
        return this;
    }

    public Profession getProfession() {
        return this.profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Recommendation profession(Profession profession) {
        this.setProfession(profession);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recommendation)) {
            return false;
        }
        return id != null && id.equals(((Recommendation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recommendation{" +
            "id=" + getId() +
            "}";
    }
}
