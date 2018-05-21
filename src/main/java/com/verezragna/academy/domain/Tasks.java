package com.verezragna.academy.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Tasks.
 */
@Entity
@Table(name = "tasks")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tasks implements Serializable {

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
    @Column(name = "task", nullable = false)
    private String task;

    @Column(name = "image_url")
    private String image_url;

    @NotNull
    @Size(min = 1)
    @Column(name = "answer", nullable = false)
    private String answer;

    @NotNull
    @Size(min = 1)
    @Column(name = "jhi_group", nullable = false)
    private String group;

    @ManyToOne(optional = false)
    @NotNull
    private User user_tasks;

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

    public Tasks title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTask() {
        return task;
    }

    public Tasks task(String task) {
        this.task = task;
        return this;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getImage_url() {
        return image_url;
    }

    public Tasks image_url(String image_url) {
        this.image_url = image_url;
        return this;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getAnswer() {
        return answer;
    }

    public Tasks answer(String answer) {
        this.answer = answer;
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getGroup() {
        return group;
    }

    public Tasks group(String group) {
        this.group = group;
        return this;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public User getUser_tasks() {
        return user_tasks;
    }

    public Tasks user_tasks(User user) {
        this.user_tasks = user;
        return this;
    }

    public void setUser_tasks(User user) {
        this.user_tasks = user;
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
        Tasks tasks = (Tasks) o;
        if (tasks.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tasks.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tasks{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", task='" + getTask() + "'" +
            ", image_url='" + getImage_url() + "'" +
            ", answer='" + getAnswer() + "'" +
            ", group='" + getGroup() + "'" +
            "}";
    }
}
