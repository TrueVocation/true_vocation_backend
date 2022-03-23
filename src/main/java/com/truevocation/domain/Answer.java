package com.truevocation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Answer.
 */
@Entity
@Table(name = "answer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "answer")
    private String answer;

    @JsonIgnoreProperties(value = { "question", "answer", "testResult" }, allowSetters = true)
    @OneToOne(mappedBy = "answer")
    private AnswerUser answerUser;

    @ManyToMany(mappedBy = "answers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "answers", "answerUser", "profTest" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Answer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return this.answer;
    }

    public Answer answer(String answer) {
        this.setAnswer(answer);
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public AnswerUser getAnswerUser() {
        return this.answerUser;
    }

    public void setAnswerUser(AnswerUser answerUser) {
        if (this.answerUser != null) {
            this.answerUser.setAnswer(null);
        }
        if (answerUser != null) {
            answerUser.setAnswer(this);
        }
        this.answerUser = answerUser;
    }

    public Answer answerUser(AnswerUser answerUser) {
        this.setAnswerUser(answerUser);
        return this;
    }

    public Set<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<Question> questions) {
        if (this.questions != null) {
            this.questions.forEach(i -> i.removeAnswer(this));
        }
        if (questions != null) {
            questions.forEach(i -> i.addAnswer(this));
        }
        this.questions = questions;
    }

    public Answer questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public Answer addQuestion(Question question) {
        this.questions.add(question);
        question.getAnswers().add(this);
        return this;
    }

    public Answer removeQuestion(Question question) {
        this.questions.remove(question);
        question.getAnswers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Answer)) {
            return false;
        }
        return id != null && id.equals(((Answer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Answer{" +
            "id=" + getId() +
            ", answer='" + getAnswer() + "'" +
            "}";
    }
}
