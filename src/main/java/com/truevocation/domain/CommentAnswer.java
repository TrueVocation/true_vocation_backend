package com.truevocation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CommentAnswer.
 */
@Entity
@Table(name = "comment_answer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommentAnswer implements Serializable {

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

    @ManyToOne
    @JsonIgnoreProperties(value = { "likes", "commentAnswers", "university", "user", "post" }, allowSetters = true)
    private Comments comment;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "user", "comments", "favorites", "commentAnswers", "likes", "portfolio", "testResult" },
        allowSetters = true
    )
    private AppUser user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CommentAnswer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public CommentAnswer text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getAddedDate() {
        return this.addedDate;
    }

    public CommentAnswer addedDate(LocalDate addedDate) {
        this.setAddedDate(addedDate);
        return this;
    }

    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
    }

    public Comments getComment() {
        return this.comment;
    }

    public void setComment(Comments comments) {
        this.comment = comments;
    }

    public CommentAnswer comment(Comments comments) {
        this.setComment(comments);
        return this;
    }

    public AppUser getUser() {
        return this.user;
    }

    public void setUser(AppUser appUser) {
        this.user = appUser;
    }

    public CommentAnswer user(AppUser appUser) {
        this.setUser(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentAnswer)) {
            return false;
        }
        return id != null && id.equals(((CommentAnswer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentAnswer{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", addedDate='" + getAddedDate() + "'" +
            "}";
    }
}
