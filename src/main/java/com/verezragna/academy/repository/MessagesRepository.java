package com.verezragna.academy.repository;

import com.verezragna.academy.domain.Messages;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Messages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessagesRepository extends JpaRepository<Messages, Long>, JpaSpecificationExecutor<Messages> {
    @Query("select distinct messages from Messages messages left join fetch messages.user_messages")
    List<Messages> findAllWithEagerRelationships();

    @Query("select messages from Messages messages left join fetch messages.user_messages where messages.id =:id")
    Messages findOneWithEagerRelationships(@Param("id") Long id);

}
