package com.truevocation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TestResult.
 */
@Entity
@Table(name = "test_result")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TestResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(
        value = { "user", "comments", "favorites", "commentAnswers", "likes", "portfolio", "testResult" },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private AppUser appUser;

    @JsonIgnoreProperties(value = { "testResult", "profession" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Recommendation recommendation;

    @OneToMany(mappedBy = "testResult")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "question", "answer", "testResult" }, allowSetters = true)
    private Set<AnswerUser> answerUsers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "questions", "testResults" }, allowSetters = true)
    private ProfTest profTest;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestResult id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public TestResult appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    public Recommendation getRecommendation() {
        return this.recommendation;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public TestResult recommendation(Recommendation recommendation) {
        this.setRecommendation(recommendation);
        return this;
    }

    public Set<AnswerUser> getAnswerUsers() {
        return this.answerUsers;
    }

    public void setAnswerUsers(Set<AnswerUser> answerUsers) {
        if (this.answerUsers != null) {
            this.answerUsers.forEach(i -> i.setTestResult(null));
        }
        if (answerUsers != null) {
            answerUsers.forEach(i -> i.setTestResult(this));
        }
        this.answerUsers = answerUsers;
    }

    public TestResult answerUsers(Set<AnswerUser> answerUsers) {
        this.setAnswerUsers(answerUsers);
        return this;
    }

    public TestResult addAnswerUser(AnswerUser answerUser) {
        this.answerUsers.add(answerUser);
        answerUser.setTestResult(this);
        return this;
    }

    public TestResult removeAnswerUser(AnswerUser answerUser) {
        this.answerUsers.remove(answerUser);
        answerUser.setTestResult(null);
        return this;
    }

    public ProfTest getProfTest() {
        return this.profTest;
    }

    public void setProfTest(ProfTest profTest) {
        this.profTest = profTest;
    }

    public TestResult profTest(ProfTest profTest) {
        this.setProfTest(profTest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestResult)) {
            return false;
        }
        return id != null && id.equals(((TestResult) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestResult{" +
            "id=" + getId() +
            "}";
    }
}
