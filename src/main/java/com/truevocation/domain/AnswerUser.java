package com.truevocation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AnswerUser.
 */
@Entity
@Table(name = "answer_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AnswerUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "answers", "answerUser", "profTest" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Question question;

    @JsonIgnoreProperties(value = { "answerUser", "questions" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Answer answer;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appUser", "recommendation", "answerUsers", "profTest" }, allowSetters = true)
    private TestResult testResult;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "favorite", "commentAnswer", "like", "portfolio", "testResult"}, allowSetters = true)
    private AppUser appUser;


    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AnswerUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public AnswerUser question(Question question) {
        this.setQuestion(question);
        return this;
    }

    public Answer getAnswer() {
        return this.answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public AnswerUser answer(Answer answer) {
        this.setAnswer(answer);
        return this;
    }

    public TestResult getTestResult() {
        return this.testResult;
    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }

    public AnswerUser testResult(TestResult testResult) {
        this.setTestResult(testResult);
        return this;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public AnswerUser appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnswerUser)) {
            return false;
        }
        return id != null && id.equals(((AnswerUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnswerUser{" +
            "id=" + getId() +
            "}";
    }
}
