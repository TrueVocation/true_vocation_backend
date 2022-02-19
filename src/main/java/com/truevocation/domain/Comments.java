package com.truevocation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Comments.
 */
@Entity
@Table(name = "comments")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Comments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "added_date")
    private LocalDate addedDate;

    @OneToMany(mappedBy = "comment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "comment", "user", "post" }, allowSetters = true)
    private Set<Likes> likes = new HashSet<>();

    @OneToMany(mappedBy = "comment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "comment", "user" }, allowSetters = true)
    private Set<CommentAnswer> commentAnswers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "contacts", "favorites", "comments", "faculties", "city" }, allowSetters = true)
    private University university;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "user", "comments", "favorites", "commentAnswers", "likes", "portfolio", "testResult" },
        allowSetters = true
    )
    private AppUser user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "likes", "favorites", "comments" }, allowSetters = true)
    private Post post;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Comments id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public Comments text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getAddedDate() {
        return this.addedDate;
    }

    public Comments addedDate(LocalDate addedDate) {
        this.setAddedDate(addedDate);
        return this;
    }

    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
    }

    public Set<Likes> getLikes() {
        return this.likes;
    }

    public void setLikes(Set<Likes> likes) {
        if (this.likes != null) {
            this.likes.forEach(i -> i.setComment(null));
        }
        if (likes != null) {
            likes.forEach(i -> i.setComment(this));
        }
        this.likes = likes;
    }

    public Comments likes(Set<Likes> likes) {
        this.setLikes(likes);
        return this;
    }

    public Comments addLikes(Likes likes) {
        this.likes.add(likes);
        likes.setComment(this);
        return this;
    }

    public Comments removeLikes(Likes likes) {
        this.likes.remove(likes);
        likes.setComment(null);
        return this;
    }

    public Set<CommentAnswer> getCommentAnswers() {
        return this.commentAnswers;
    }

    public void setCommentAnswers(Set<CommentAnswer> commentAnswers) {
        if (this.commentAnswers != null) {
            this.commentAnswers.forEach(i -> i.setComment(null));
        }
        if (commentAnswers != null) {
            commentAnswers.forEach(i -> i.setComment(this));
        }
        this.commentAnswers = commentAnswers;
    }

    public Comments commentAnswers(Set<CommentAnswer> commentAnswers) {
        this.setCommentAnswers(commentAnswers);
        return this;
    }

    public Comments addCommentAnswer(CommentAnswer commentAnswer) {
        this.commentAnswers.add(commentAnswer);
        commentAnswer.setComment(this);
        return this;
    }

    public Comments removeCommentAnswer(CommentAnswer commentAnswer) {
        this.commentAnswers.remove(commentAnswer);
        commentAnswer.setComment(null);
        return this;
    }

    public University getUniversity() {
        return this.university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Comments university(University university) {
        this.setUniversity(university);
        return this;
    }

    public AppUser getUser() {
        return this.user;
    }

    public void setUser(AppUser appUser) {
        this.user = appUser;
    }

    public Comments user(AppUser appUser) {
        this.setUser(appUser);
        return this;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comments post(Post post) {
        this.setPost(post);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comments)) {
            return false;
        }
        return id != null && id.equals(((Comments) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comments{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", addedDate='" + getAddedDate() + "'" +
            "}";
    }
}
