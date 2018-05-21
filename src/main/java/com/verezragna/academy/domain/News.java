package com.verezragna.academy.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A News.
 */
@Entity
@Table(name = "news")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class News implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Size(min = 1)
    @Column(name = "image_url", nullable = false)
    private String image_url;

    @NotNull
    @Size(min = 1)
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Size(min = 1)
    @Column(name = "text", nullable = false)
    private String text;

    @NotNull
    @Size(min = 1)
    @Column(name = "jhi_group", nullable = false)
    private String group;

    @ManyToOne(optional = false)
    @NotNull
    private User user_news;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public News title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public News image_url(String image_url) {
        this.image_url = image_url;
        return this;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public News description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public News text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGroup() {
        return group;
    }

    public News group(String group) {
        this.group = group;
        return this;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public User getUser_news() {
        return user_news;
    }

    public News user_news(User user) {
        this.user_news = user;
        return this;
    }

    public void setUser_news(User user) {
        this.user_news = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        News news = (News) o;
        if (news.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), news.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "News{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", image_url='" + getImage_url() + "'" +
            ", description='" + getDescription() + "'" +
            ", text='" + getText() + "'" +
            ", group='" + getGroup() + "'" +
            "}";
    }
}
