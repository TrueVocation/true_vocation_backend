package com.truevocation.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.truevocation.domain.CommentAnswer} entity.
 */
public class CommentAnswerDTO implements Serializable {

    private Long id;

    private String text;

    private LocalDate addedDate;

    private CommentsDTO comment;

    private AppUserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
    }

    public CommentsDTO getComment() {
        return comment;
    }

    public void setComment(CommentsDTO comment) {
        this.comment = comment;
    }

    public AppUserDTO getUser() {
        return user;
    }

    public void setUser(AppUserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentAnswerDTO)) {
            return false;
        }

        CommentAnswerDTO commentAnswerDTO = (CommentAnswerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commentAnswerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentAnswerDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", addedDate='" + getAddedDate() + "'" +
            ", comment=" + getComment() +
            ", user=" + getUser() +
            "}";
    }
}
