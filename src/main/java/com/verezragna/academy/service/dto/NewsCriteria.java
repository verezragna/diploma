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
 * Criteria class for the News entity. This class is used in NewsResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /news?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NewsCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter title;

    private StringFilter image_url;

    private StringFilter description;

    private StringFilter text;

    private StringFilter group;

    private LongFilter user_newsId;

    public NewsCriteria() {
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

    public StringFilter getImage_url() {
        return image_url;
    }

    public void setImage_url(StringFilter image_url) {
        this.image_url = image_url;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getText() {
        return text;
    }

    public void setText(StringFilter text) {
        this.text = text;
    }

    public StringFilter getGroup() {
        return group;
    }

    public void setGroup(StringFilter group) {
        this.group = group;
    }

    public LongFilter getUser_newsId() {
        return user_newsId;
    }

    public void setUser_newsId(LongFilter user_newsId) {
        this.user_newsId = user_newsId;
    }

    @Override
    public String toString() {
        return "NewsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (image_url != null ? "image_url=" + image_url + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (text != null ? "text=" + text + ", " : "") +
                (group != null ? "group=" + group + ", " : "") +
                (user_newsId != null ? "user_newsId=" + user_newsId + ", " : "") +
            "}";
    }

}
