package com.verezragna.academy.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Messages.
 */
@Entity
@Table(name = "messages")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Messages implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "jhi_from", nullable = false)
    private Integer from;

    @NotNull
    @Min(value = 1)
    @Column(name = "jhi_to", nullable = false)
    private Integer to;

    @NotNull
    @Size(min = 1)
    @Column(name = "message", nullable = false)
    private String message;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "messages_user_messages",
               joinColumns = @JoinColumn(name="messages_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="user_messages_id", referencedColumnName="id"))
    private Set<User> user_messages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFrom() {
        return from;
    }

    public Messages from(Integer from) {
        this.from = from;
        return this;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public Messages to(Integer to) {
        this.to = to;
        return this;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public Messages message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<User> getUser_messages() {
        return user_messages;
    }

    public Messages user_messages(Set<User> users) {
        this.user_messages = users;
        return this;
    }

    public Messages addUser_messages(User user) {
        this.user_messages.add(user);
        //user.getMessages().add(this);
        return this;
    }

    public Messages removeUser_messages(User user) {
        this.user_messages.remove(user);
        //user.getMessages().remove(this);
        return this;
    }

    public void setUser_messages(Set<User> users) {
        this.user_messages = users;
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
        Messages messages = (Messages) o;
        if (messages.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), messages.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Messages{" +
            "id=" + getId() +
            ", from=" + getFrom() +
            ", to=" + getTo() +
            ", message='" + getMessage() + "'" +
            "}";
    }
}
