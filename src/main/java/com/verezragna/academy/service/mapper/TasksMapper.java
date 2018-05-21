package com.verezragna.academy.service.mapper;

import com.verezragna.academy.domain.*;
import com.verezragna.academy.service.dto.TasksDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Tasks and its DTO TasksDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TasksMapper extends EntityMapper<TasksDTO, Tasks> {

    @Mapping(source = "user_tasks.id", target = "user_tasksId")
    @Mapping(source = "user_tasks.login", target = "user_tasksLogin")
    TasksDTO toDto(Tasks tasks);

    @Mapping(source = "user_tasksId", target = "user_tasks")
    Tasks toEntity(TasksDTO tasksDTO);

    default Tasks fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tasks tasks = new Tasks();
        tasks.setId(id);
        return tasks;
    }
}
