package com.truevocation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "question")
    private String question;

    @ManyToMany
    @JoinTable(
        name = "rel_question__answer",
        joinColumns = @JoinColumn(name = "question_id"),
        inverseJoinColumns = @JoinColumn(name = "answer_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "answerUser", "questions" }, allowSetters = true)
    private Set<Answer> answers = new HashSet<>();

    @OneToMany(mappedBy = "question")
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

    public Question id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return this.question;
    }

    public Question question(String question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Set<Answer> getAnswers() {
        return this.answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Question answers(Set<Answer> answers) {
        this.setAnswers(answers);
        return this;
    }

    public Question addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.getQuestions().add(this);
        return this;
    }

    public Question removeAnswer(Answer answer) {
        this.answers.remove(answer);
        answer.getQuestions().remove(this);
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
            answerUsers.forEach(i -> i.setQuestion(this));
        }
        this.answerUsers = answerUsers;
    }

    public Question answerUsers(Set<AnswerUser> answerUsers) {
        this.setAnswerUsers(answerUsers);
        return this;
    }

    public Question addAnswerUser(AnswerUser answerUser) {
        this.answerUsers.add(answerUser);
        answerUser.setQuestion(this);
        return this;
    }

    public Question removeAnswerUser(AnswerUser answerUser) {
        this.answerUsers.remove(answerUser);
        answerUser.setQuestion(null);
        return this;
    }
    public ProfTest getProfTest() {
        return this.profTest;
    }

    public void setProfTest(ProfTest profTest) {
        this.profTest = profTest;
    }

    public Question profTest(ProfTest profTest) {
        this.setProfTest(profTest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return id != null && id.equals(((Question) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            "}";
    }
}
