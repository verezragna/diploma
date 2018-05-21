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
 * Criteria class for the Messages entity. This class is used in MessagesResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /messages?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MessagesCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private IntegerFilter from;

    private IntegerFilter to;

    private StringFilter message;

    private LongFilter user_messagesId;

    public MessagesCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getFrom() {
        return from;
    }

    public void setFrom(IntegerFilter from) {
        this.from = from;
    }

    public IntegerFilter getTo() {
        return to;
    }

    public void setTo(IntegerFilter to) {
        this.to = to;
    }

    public StringFilter getMessage() {
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public LongFilter getUser_messagesId() {
        return user_messagesId;
    }

    public void setUser_messagesId(LongFilter user_messagesId) {
        this.user_messagesId = user_messagesId;
    }

    @Override
    public String toString() {
        return "MessagesCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (from != null ? "from=" + from + ", " : "") +
                (to != null ? "to=" + to + ", " : "") +
                (message != null ? "message=" + message + ", " : "") +
                (user_messagesId != null ? "user_messagesId=" + user_messagesId + ", " : "") +
            "}";
    }

}
