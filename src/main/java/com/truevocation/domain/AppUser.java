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
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "likes", "commentAnswers", "university", "user", "post" }, allowSetters = true)
    private Set<Comments> comments = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "university", "user", "post" }, allowSetters = true)
    private Set<Favorite> favorites = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "comment", "user" }, allowSetters = true)
    private Set<CommentAnswer> commentAnswers = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "comment", "user", "post" }, allowSetters = true)
    private Set<Likes> likes = new HashSet<>();

    @JsonIgnoreProperties(value = { "appUser", "contacts", "achievements", "languages", "schools" }, allowSetters = true)
    @OneToOne(mappedBy = "appUser")
    private Portfolio portfolio;

    @JsonIgnoreProperties(value = { "appUser", "recommendation", "answerUsers", "profTest" }, allowSetters = true)
    @OneToOne(mappedBy = "appUser")
    private TestResult testResult;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public AppUser phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthdate() {
        return this.birthdate;
    }

    public AppUser birthdate(LocalDate birthdate) {
        this.setBirthdate(birthdate);
        return this;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AppUser user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Comments> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comments> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setUser(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setUser(this));
        }
        this.comments = comments;
    }

    public AppUser comments(Set<Comments> comments) {
        this.setComments(comments);
        return this;
    }

    public AppUser addComments(Comments comments) {
        this.comments.add(comments);
        comments.setUser(this);
        return this;
    }

    public AppUser removeComments(Comments comments) {
        this.comments.remove(comments);
        comments.setUser(null);
        return this;
    }

    public Set<Favorite> getFavorites() {
        return this.favorites;
    }

    public void setFavorites(Set<Favorite> favorites) {
        if (this.favorites != null) {
            this.favorites.forEach(i -> i.setUser(null));
        }
        if (favorites != null) {
            favorites.forEach(i -> i.setUser(this));
        }
        this.favorites = favorites;
    }

    public AppUser favorites(Set<Favorite> favorites) {
        this.setFavorites(favorites);
        return this;
    }

    public AppUser addFavorite(Favorite favorite) {
        this.favorites.add(favorite);
        favorite.setUser(this);
        return this;
    }

    public AppUser removeFavorite(Favorite favorite) {
        this.favorites.remove(favorite);
        favorite.setUser(null);
        return this;
    }

    public Set<CommentAnswer> getCommentAnswers() {
        return this.commentAnswers;
    }

    public void setCommentAnswers(Set<CommentAnswer> commentAnswers) {
        if (this.commentAnswers != null) {
            this.commentAnswers.forEach(i -> i.setUser(null));
        }
        if (commentAnswers != null) {
            commentAnswers.forEach(i -> i.setUser(this));
        }
        this.commentAnswers = commentAnswers;
    }

    public AppUser commentAnswers(Set<CommentAnswer> commentAnswers) {
        this.setCommentAnswers(commentAnswers);
        return this;
    }

    public AppUser addCommentAnswer(CommentAnswer commentAnswer) {
        this.commentAnswers.add(commentAnswer);
        commentAnswer.setUser(this);
        return this;
    }

    public AppUser removeCommentAnswer(CommentAnswer commentAnswer) {
        this.commentAnswers.remove(commentAnswer);
        commentAnswer.setUser(null);
        return this;
    }

    public Set<Likes> getLikes() {
        return this.likes;
    }

    public void setLikes(Set<Likes> likes) {
        if (this.likes != null) {
            this.likes.forEach(i -> i.setUser(null));
        }
        if (likes != null) {
            likes.forEach(i -> i.setUser(this));
        }
        this.likes = likes;
    }

    public AppUser likes(Set<Likes> likes) {
        this.setLikes(likes);
        return this;
    }

    public AppUser addLikes(Likes likes) {
        this.likes.add(likes);
        likes.setUser(this);
        return this;
    }

    public AppUser removeLikes(Likes likes) {
        this.likes.remove(likes);
        likes.setUser(null);
        return this;
    }

    public Portfolio getPortfolio() {
        return this.portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        if (this.portfolio != null) {
            this.portfolio.setAppUser(null);
        }
        if (portfolio != null) {
            portfolio.setAppUser(this);
        }
        this.portfolio = portfolio;
    }

    public AppUser portfolio(Portfolio portfolio) {
        this.setPortfolio(portfolio);
        return this;
    }

    public TestResult getTestResult() {
        return this.testResult;
    }

    public void setTestResult(TestResult testResult) {
        if (this.testResult != null) {
            this.testResult.setAppUser(null);
        }
        if (testResult != null) {
            testResult.setAppUser(this);
        }
        this.testResult = testResult;
    }

    public AppUser testResult(TestResult testResult) {
        this.setTestResult(testResult);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return id != null && id.equals(((AppUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", birthdate='" + getBirthdate() + "'" +
            "}";
    }
}
