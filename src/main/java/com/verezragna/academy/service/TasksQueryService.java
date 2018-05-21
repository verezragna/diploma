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

import com.verezragna.academy.domain.Tasks;
import com.verezragna.academy.domain.*; // for static metamodels
import com.verezragna.academy.repository.TasksRepository;
import com.verezragna.academy.service.dto.TasksCriteria;

import com.verezragna.academy.service.dto.TasksDTO;
import com.verezragna.academy.service.mapper.TasksMapper;

/**
 * Service for executing complex queries for Tasks entities in the database.
 * The main input is a {@link TasksCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TasksDTO} or a {@link Page} of {@link TasksDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TasksQueryService extends QueryService<Tasks> {

    private final Logger log = LoggerFactory.getLogger(TasksQueryService.class);


    private final TasksRepository tasksRepository;

    private final TasksMapper tasksMapper;

    public TasksQueryService(TasksRepository tasksRepository, TasksMapper tasksMapper) {
        this.tasksRepository = tasksRepository;
        this.tasksMapper = tasksMapper;
    }

    /**
     * Return a {@link List} of {@link TasksDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TasksDTO> findByCriteria(TasksCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Tasks> specification = createSpecification(criteria);
        return tasksMapper.toDto(tasksRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TasksDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TasksDTO> findByCriteria(TasksCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Tasks> specification = createSpecification(criteria);
        final Page<Tasks> result = tasksRepository.findAll(specification, page);
        return result.map(tasksMapper::toDto);
    }

    /**
     * Function to convert TasksCriteria to a {@link Specifications}
     */
    private Specifications<Tasks> createSpecification(TasksCriteria criteria) {
        Specifications<Tasks> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Tasks_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Tasks_.title));
            }
            if (criteria.getTask() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTask(), Tasks_.task));
            }
            if (criteria.getImage_url() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImage_url(), Tasks_.image_url));
            }
            if (criteria.getAnswer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAnswer(), Tasks_.answer));
            }
            if (criteria.getGroup() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGroup(), Tasks_.group));
            }
            if (criteria.getUser_tasksId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUser_tasksId(), Tasks_.user_tasks, User_.id));
            }
        }
        return specification;
    }

}
