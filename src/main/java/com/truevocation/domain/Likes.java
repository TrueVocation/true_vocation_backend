package com.truevocation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Likes.
 */
@Entity
@Table(name = "likes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Likes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = { "likes", "commentAnswers", "university", "user", "post" }, allowSetters = true)
    private Comments comment;

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

    public Likes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Comments getComment() {
        return this.comment;
    }

    public void setComment(Comments comments) {
        this.comment = comments;
    }

    public Likes comment(Comments comments) {
        this.setComment(comments);
        return this;
    }

    public AppUser getUser() {
        return this.user;
    }

    public void setUser(AppUser appUser) {
        this.user = appUser;
    }

    public Likes user(AppUser appUser) {
        this.setUser(appUser);
        return this;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Likes post(Post post) {
        this.setPost(post);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Likes)) {
            return false;
        }
        return id != null && id.equals(((Likes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Likes{" +
            "id=" + getId() +
            "}";
    }
}
