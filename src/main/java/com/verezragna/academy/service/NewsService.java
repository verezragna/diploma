package com.verezragna.academy.service;

import com.verezragna.academy.service.dto.NewsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing News.
 */
public interface NewsService {

    /**
     * Save a news.
     *
     * @param newsDTO the entity to save
     * @return the persisted entity
     */
    NewsDTO save(NewsDTO newsDTO);

    /**
     * Get all the news.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<NewsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" news.
     *
     * @param id the id of the entity
     * @return the entity
     */
    NewsDTO findOne(Long id);

    /**
     * Delete the "id" news.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
