package com.truevocation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DemandProfessionCity.
 */
@Entity
@Table(name = "demand_profession_city")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DemandProfessionCity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "actual_in_percent")
    private Double actualInPercent;

    @ManyToOne
    @JsonIgnoreProperties(value = { "demandProfessionCities", "recommendations", "courses", "specialties" }, allowSetters = true)
    private Profession profession;

    @ManyToOne
    @JsonIgnoreProperties(value = { "demandProfessionCities", "schools", "universities", "courses" }, allowSetters = true)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DemandProfessionCity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getActualInPercent() {
        return this.actualInPercent;
    }

    public DemandProfessionCity actualInPercent(Double actualInPercent) {
        this.setActualInPercent(actualInPercent);
        return this;
    }

    public void setActualInPercent(Double actualInPercent) {
        this.actualInPercent = actualInPercent;
    }

    public Profession getProfession() {
        return this.profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public DemandProfessionCity profession(Profession profession) {
        this.setProfession(profession);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public DemandProfessionCity city(City city) {
        this.setCity(city);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemandProfessionCity)) {
            return false;
        }
        return id != null && id.equals(((DemandProfessionCity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandProfessionCity{" +
            "id=" + getId() +
            ", actualInPercent=" + getActualInPercent() +
            "}";
    }
}
