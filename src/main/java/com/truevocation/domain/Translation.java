package com.truevocation.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Translation.
 */
@Entity
@Table(name = "translation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Translation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "en")
    private String en;

    @Column(name = "ru")
    private String ru;

    @Column(name = "kk")
    private String kk;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Translation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Translation code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEn() {
        return this.en;
    }

    public Translation en(String en) {
        this.setEn(en);
        return this;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getRu() {
        return this.ru;
    }

    public Translation ru(String ru) {
        this.setRu(ru);
        return this;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    public String getKk() {
        return this.kk;
    }

    public Translation kk(String kk) {
        this.setKk(kk);
        return this;
    }

    public void setKk(String kk) {
        this.kk = kk;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Translation)) {
            return false;
        }
        return id != null && id.equals(((Translation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Translation{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", en='" + getEn() + "'" +
            ", ru='" + getRu() + "'" +
            ", kk='" + getKk() + "'" +
            "}";
    }
}
