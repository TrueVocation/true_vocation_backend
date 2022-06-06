package com.truevocation.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.truevocation.domain.AnswerUser} entity.
 */
public class AnswerUserDTO implements Serializable {

    private Long id;

    private QuestionDTO question;

    private AnswerDTO answer;

    private TestResultDTO testResult;

    private AppUserDTO appUserDTO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    public AnswerDTO getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerDTO answer) {
        this.answer = answer;
    }

    public TestResultDTO getTestResult() {
        return testResult;
    }

    public void setTestResult(TestResultDTO testResult) {
        this.testResult = testResult;
    }

    public AppUserDTO getAppUserDTO() {
        return appUserDTO;
    }

    public void setAppUserDTO(AppUserDTO appUserDTO) {
        this.appUserDTO = appUserDTO;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnswerUserDTO)) {
            return false;
        }

        AnswerUserDTO answerUserDTO = (AnswerUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, answerUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnswerUserDTO{" +
            "id=" + getId() +
            ", question=" + getQuestion() +
            ", answer=" + getAnswer() +
            ", testResult=" + getTestResult() +
            "}";
    }
}
