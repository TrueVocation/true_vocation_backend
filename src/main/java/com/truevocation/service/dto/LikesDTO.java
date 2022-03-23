package com.truevocation.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.truevocation.domain.Likes} entity.
 */
public class LikesDTO implements Serializable {

    private Long id;

    private CommentsDTO comment;

    private AppUserDTO user;

    private PostDTO post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public PostDTO getPost() {
        return post;
    }

    public void setPost(PostDTO post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LikesDTO)) {
            return false;
        }

        LikesDTO likesDTO = (LikesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, likesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LikesDTO{" +
            "id=" + getId() +
            ", comment=" + getComment() +
            ", user=" + getUser() +
            ", post=" + getPost() +
            "}";
    }
}
