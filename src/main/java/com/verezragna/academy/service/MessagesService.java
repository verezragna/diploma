package com.verezragna.academy.service;

import com.verezragna.academy.domain.Messages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Messages.
 */
public interface MessagesService {

    /**
     * Save a messages.
     *
     * @param messages the entity to save
     * @return the persisted entity
     */
    Messages save(Messages messages);

    /**
     * Get all the messages.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Messages> findAll(Pageable pageable);

    /**
     * Get the "id" messages.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Messages findOne(Long id);

    /**
     * Delete the "id" messages.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
