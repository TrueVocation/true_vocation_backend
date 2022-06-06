package com.truevocation.service.dto;

import com.truevocation.web.rest.vm.UserAccountDto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.truevocation.domain.Comments} entity.
 */
public class CommentsDTO implements Serializable {

    private Long id;

    private String text;

    private LocalDate addedDate;

    private UniversityDTO university;

    private AppUserDTO user;

    private PostDTO post;

    private UserDTO userDTO;

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

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

    public UniversityDTO getUniversity() {
        return university;
    }

    public void setUniversity(UniversityDTO university) {
        this.university = university;
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
        if (!(o instanceof CommentsDTO)) {
            return false;
        }

        CommentsDTO commentsDTO = (CommentsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commentsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentsDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", addedDate='" + getAddedDate() + "'" +
            ", university=" + getUniversity() +
            ", user=" + getUser() +
            ", post=" + getPost() +
            "}";
    }
}
