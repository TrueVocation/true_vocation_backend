package com.truevocation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Favorite.
 */
@Entity
@Table(name = "favorite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Favorite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contacts", "favorites", "comments", "pictures", "faculties", "city" }, allowSetters = true)
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

    public Favorite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public Favorite type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public University getUniversity() {
        return this.university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Favorite university(University university) {
        this.setUniversity(university);
        return this;
    }

    public AppUser getUser() {
        return this.user;
    }

    public void setUser(AppUser appUser) {
        this.user = appUser;
    }

    public Favorite user(AppUser appUser) {
        this.setUser(appUser);
        return this;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Favorite post(Post post) {
        this.setPost(post);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Favorite)) {
            return false;
        }
        return id != null && id.equals(((Favorite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Favorite{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
