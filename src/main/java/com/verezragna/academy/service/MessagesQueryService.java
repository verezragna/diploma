package com.verezragna.academy.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.verezragna.academy.domain.Messages;
import com.verezragna.academy.domain.*; // for static metamodels
import com.verezragna.academy.repository.MessagesRepository;
import com.verezragna.academy.service.dto.MessagesCriteria;


/**
 * Service for executing complex queries for Messages entities in the database.
 * The main input is a {@link MessagesCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Messages} or a {@link Page} of {@link Messages} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MessagesQueryService extends QueryService<Messages> {

    private final Logger log = LoggerFactory.getLogger(MessagesQueryService.class);


    private final MessagesRepository messagesRepository;

    public MessagesQueryService(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    /**
     * Return a {@link List} of {@link Messages} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Messages> findByCriteria(MessagesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Messages> specification = createSpecification(criteria);
        return messagesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Messages} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Messages> findByCriteria(MessagesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Messages> specification = createSpecification(criteria);
        return messagesRepository.findAll(specification, page);
    }

    /**
     * Function to convert MessagesCriteria to a {@link Specifications}
     */
    private Specifications<Messages> createSpecification(MessagesCriteria criteria) {
        Specifications<Messages> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Messages_.id));
            }
            if (criteria.getFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFrom(), Messages_.from));
            }
            if (criteria.getTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTo(), Messages_.to));
            }
            if (criteria.getMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMessage(), Messages_.message));
            }
            if (criteria.getUser_messagesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUser_messagesId(), Messages_.user_messages, User_.id));
            }
        }
        return specification;
    }

}
