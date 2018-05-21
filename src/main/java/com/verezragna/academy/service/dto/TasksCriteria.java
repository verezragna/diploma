package com.verezragna.academy.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Tasks entity. This class is used in TasksResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /tasks?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TasksCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter title;

    private StringFilter task;

    private StringFilter image_url;

    private StringFilter answer;

    private StringFilter group;

    private LongFilter user_tasksId;

    public TasksCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getTask() {
        return task;
    }

    public void setTask(StringFilter task) {
        this.task = task;
    }

    public StringFilter getImage_url() {
        return image_url;
    }

    public void setImage_url(StringFilter image_url) {
        this.image_url = image_url;
    }

    public StringFilter getAnswer() {
        return answer;
    }

    public void setAnswer(StringFilter answer) {
        this.answer = answer;
    }

    public StringFilter getGroup() {
        return group;
    }

    public void setGroup(StringFilter group) {
        this.group = group;
    }

    public LongFilter getUser_tasksId() {
        return user_tasksId;
    }

    public void setUser_tasksId(LongFilter user_tasksId) {
        this.user_tasksId = user_tasksId;
    }

    @Override
    public String toString() {
        return "TasksCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (task != null ? "task=" + task + ", " : "") +
                (image_url != null ? "image_url=" + image_url + ", " : "") +
                (answer != null ? "answer=" + answer + ", " : "") +
                (group != null ? "group=" + group + ", " : "") +
                (user_tasksId != null ? "user_tasksId=" + user_tasksId + ", " : "") +
            "}";
    }

}
