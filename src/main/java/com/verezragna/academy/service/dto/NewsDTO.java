package com.verezragna.academy.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the News entity.
 */
public class NewsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1)
    private String title;

    @NotNull
    @Size(min = 1)
    private String image_url;

    @NotNull
    @Size(min = 1)
    private String description;

    @NotNull
    @Size(min = 1)
    private String text;

    @NotNull
    @Size(min = 1)
    private String group;

    private Long user_newsId;

    private String user_newsLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Long getUser_newsId() {
        return user_newsId;
    }

    public void setUser_newsId(Long userId) {
        this.user_newsId = userId;
    }

    public String getUser_newsLogin() {
        return user_newsLogin;
    }

    public void setUser_newsLogin(String userLogin) {
        this.user_newsLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NewsDTO newsDTO = (NewsDTO) o;
        if(newsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), newsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NewsDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", image_url='" + getImage_url() + "'" +
            ", description='" + getDescription() + "'" +
            ", text='" + getText() + "'" +
            ", group='" + getGroup() + "'" +
            "}";
    }
}
