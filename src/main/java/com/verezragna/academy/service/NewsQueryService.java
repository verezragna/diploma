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

import com.verezragna.academy.domain.News;
import com.verezragna.academy.domain.*; // for static metamodels
import com.verezragna.academy.repository.NewsRepository;
import com.verezragna.academy.service.dto.NewsCriteria;

import com.verezragna.academy.service.dto.NewsDTO;
import com.verezragna.academy.service.mapper.NewsMapper;

/**
 * Service for executing complex queries for News entities in the database.
 * The main input is a {@link NewsCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NewsDTO} or a {@link Page} of {@link NewsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NewsQueryService extends QueryService<News> {

    private final Logger log = LoggerFactory.getLogger(NewsQueryService.class);


    private final NewsRepository newsRepository;

    private final NewsMapper newsMapper;

    public NewsQueryService(NewsRepository newsRepository, NewsMapper newsMapper) {
        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
    }

    /**
     * Return a {@link List} of {@link NewsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NewsDTO> findByCriteria(NewsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<News> specification = createSpecification(criteria);
        return newsMapper.toDto(newsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NewsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NewsDTO> findByCriteria(NewsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<News> specification = createSpecification(criteria);
        final Page<News> result = newsRepository.findAll(specification, page);
        return result.map(newsMapper::toDto);
    }

    /**
     * Function to convert NewsCriteria to a {@link Specifications}
     */
    private Specifications<News> createSpecification(NewsCriteria criteria) {
        Specifications<News> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), News_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), News_.title));
            }
            if (criteria.getImage_url() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImage_url(), News_.image_url));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), News_.description));
            }
            if (criteria.getText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getText(), News_.text));
            }
            if (criteria.getGroup() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGroup(), News_.group));
            }
            if (criteria.getUser_newsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUser_newsId(), News_.user_news, User_.id));
            }
        }
        return specification;
    }

}
