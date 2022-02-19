package com.truevocation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Language.
 */
@Entity
@Table(name = "language")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "language")
    private String language;

    @Column(name = "level")
    private String level;

    @ManyToMany(mappedBy = "languages")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "contacts", "achievements", "languages", "schools" }, allowSetters = true)
    private Set<Portfolio> portfolios = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Language id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguage() {
        return this.language;
    }

    public Language language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLevel() {
        return this.level;
    }

    public Language level(String level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Set<Portfolio> getPortfolios() {
        return this.portfolios;
    }

    public void setPortfolios(Set<Portfolio> portfolios) {
        if (this.portfolios != null) {
            this.portfolios.forEach(i -> i.removeLanguage(this));
        }
        if (portfolios != null) {
            portfolios.forEach(i -> i.addLanguage(this));
        }
        this.portfolios = portfolios;
    }

    public Language portfolios(Set<Portfolio> portfolios) {
        this.setPortfolios(portfolios);
        return this;
    }

    public Language addPortfolio(Portfolio portfolio) {
        this.portfolios.add(portfolio);
        portfolio.getLanguages().add(this);
        return this;
    }

    public Language removePortfolio(Portfolio portfolio) {
        this.portfolios.remove(portfolio);
        portfolio.getLanguages().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Language)) {
            return false;
        }
        return id != null && id.equals(((Language) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Language{" +
            "id=" + getId() +
            ", language='" + getLanguage() + "'" +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
