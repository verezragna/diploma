package com.verezragna.academy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.verezragna.academy.service.NewsService;
import com.verezragna.academy.web.rest.errors.BadRequestAlertException;
import com.verezragna.academy.web.rest.util.HeaderUtil;
import com.verezragna.academy.web.rest.util.PaginationUtil;
import com.verezragna.academy.service.dto.NewsDTO;
import com.verezragna.academy.service.dto.NewsCriteria;
import com.verezragna.academy.service.NewsQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing News.
 */
@RestController
@RequestMapping("/api")
public class NewsResource {

    private final Logger log = LoggerFactory.getLogger(NewsResource.class);

    private static final String ENTITY_NAME = "news";

    private final NewsService newsService;

    private final NewsQueryService newsQueryService;

    public NewsResource(NewsService newsService, NewsQueryService newsQueryService) {
        this.newsService = newsService;
        this.newsQueryService = newsQueryService;
    }

    /**
     * POST  /news : Create a new news.
     *
     * @param newsDTO the newsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new newsDTO, or with status 400 (Bad Request) if the news has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/news")
    @Timed
    public ResponseEntity<NewsDTO> createNews(@Valid @RequestBody NewsDTO newsDTO) throws URISyntaxException {
        log.debug("REST request to save News : {}", newsDTO);
        if (newsDTO.getId() != null) {
            throw new BadRequestAlertException("A new news cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NewsDTO result = newsService.save(newsDTO);
        return ResponseEntity.created(new URI("/api/news/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /news : Updates an existing news.
     *
     * @param newsDTO the newsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated newsDTO,
     * or with status 400 (Bad Request) if the newsDTO is not valid,
     * or with status 500 (Internal Server Error) if the newsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/news")
    @Timed
    public ResponseEntity<NewsDTO> updateNews(@Valid @RequestBody NewsDTO newsDTO) throws URISyntaxException {
        log.debug("REST request to update News : {}", newsDTO);
        if (newsDTO.getId() == null) {
            return createNews(newsDTO);
        }
        NewsDTO result = newsService.save(newsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, newsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /news : get all the news.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of news in body
     */
    @GetMapping("/news")
    @Timed
    public ResponseEntity<List<NewsDTO>> getAllNews(NewsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get News by criteria: {}", criteria);
        Page<NewsDTO> page = newsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/news");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /news/:id : get the "id" news.
     *
     * @param id the id of the newsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the newsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/news/{id}")
    @Timed
    public ResponseEntity<NewsDTO> getNews(@PathVariable Long id) {
        log.debug("REST request to get News : {}", id);
        NewsDTO newsDTO = newsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(newsDTO));
    }

    /**
     * DELETE  /news/:id : delete the "id" news.
     *
     * @param id the id of the newsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/news/{id}")
    @Timed
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        log.debug("REST request to delete News : {}", id);
        newsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
