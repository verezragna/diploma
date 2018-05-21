package com.verezragna.academy.repository;

import com.verezragna.academy.domain.Tasks;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Tasks entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TasksRepository extends JpaRepository<Tasks, Long>, JpaSpecificationExecutor<Tasks> {

    @Query("select tasks from Tasks tasks where tasks.user_tasks.login = ?#{principal.username}")
    List<Tasks> findByUser_tasksIsCurrentUser();

}
