package com.verezragna.academy.service.impl;

import com.verezragna.academy.service.MessagesService;
import com.verezragna.academy.domain.Messages;
import com.verezragna.academy.repository.MessagesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Messages.
 */
@Service
@Transactional
public class MessagesServiceImpl implements MessagesService {

    private final Logger log = LoggerFactory.getLogger(MessagesServiceImpl.class);

    private final MessagesRepository messagesRepository;

    public MessagesServiceImpl(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    /**
     * Save a messages.
     *
     * @param messages the entity to save
     * @return the persisted entity
     */
    @Override
    public Messages save(Messages messages) {
        log.debug("Request to save Messages : {}", messages);
        return messagesRepository.save(messages);
    }

    /**
     * Get all the messages.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Messages> findAll(Pageable pageable) {
        log.debug("Request to get all Messages");
        return messagesRepository.findAll(pageable);
    }

    /**
     * Get one messages by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Messages findOne(Long id) {
        log.debug("Request to get Messages : {}", id);
        return messagesRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the messages by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Messages : {}", id);
        messagesRepository.delete(id);
    }
}
