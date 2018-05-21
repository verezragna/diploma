package com.verezragna.academy.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Tasks entity.
 */
public class TasksDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1)
    private String title;

    @NotNull
    @Size(min = 1)
    private String task;

    private String image_url;

    @NotNull
    @Size(min = 1)
    private String answer;

    @NotNull
    @Size(min = 1)
    private String group;

    private Long user_tasksId;

    private String user_tasksLogin;

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

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Long getUser_tasksId() {
        return user_tasksId;
    }

    public void setUser_tasksId(Long userId) {
        this.user_tasksId = userId;
    }

    public String getUser_tasksLogin() {
        return user_tasksLogin;
    }

    public void setUser_tasksLogin(String userLogin) {
        this.user_tasksLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TasksDTO tasksDTO = (TasksDTO) o;
        if(tasksDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tasksDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TasksDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", task='" + getTask() + "'" +
            ", image_url='" + getImage_url() + "'" +
            ", answer='" + getAnswer() + "'" +
            ", group='" + getGroup() + "'" +
            "}";
    }
}
