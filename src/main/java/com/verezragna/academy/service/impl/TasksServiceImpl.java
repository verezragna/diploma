package com.verezragna.academy.service.impl;

import com.verezragna.academy.service.TasksService;
import com.verezragna.academy.domain.Tasks;
import com.verezragna.academy.repository.TasksRepository;
import com.verezragna.academy.service.dto.TasksDTO;
import com.verezragna.academy.service.mapper.TasksMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Tasks.
 */
@Service
@Transactional
public class TasksServiceImpl implements TasksService {

    private final Logger log = LoggerFactory.getLogger(TasksServiceImpl.class);

    private final TasksRepository tasksRepository;

    private final TasksMapper tasksMapper;

    public TasksServiceImpl(TasksRepository tasksRepository, TasksMapper tasksMapper) {
        this.tasksRepository = tasksRepository;
        this.tasksMapper = tasksMapper;
    }

    /**
     * Save a tasks.
     *
     * @param tasksDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TasksDTO save(TasksDTO tasksDTO) {
        log.debug("Request to save Tasks : {}", tasksDTO);
        Tasks tasks = tasksMapper.toEntity(tasksDTO);
        tasks = tasksRepository.save(tasks);
        return tasksMapper.toDto(tasks);
    }

    /**
     * Get all the tasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TasksDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tasks");
        return tasksRepository.findAll(pageable)
            .map(tasksMapper::toDto);
    }

    /**
     * Get one tasks by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TasksDTO findOne(Long id) {
        log.debug("Request to get Tasks : {}", id);
        Tasks tasks = tasksRepository.findOne(id);
        return tasksMapper.toDto(tasks);
    }

    /**
     * Delete the tasks by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tasks : {}", id);
        tasksRepository.delete(id);
    }
}
