package com.verezragna.academy.web.rest;

import com.verezragna.academy.DiplomaApp;

import com.verezragna.academy.domain.News;
import com.verezragna.academy.domain.User;
import com.verezragna.academy.repository.NewsRepository;
import com.verezragna.academy.service.NewsService;
import com.verezragna.academy.service.dto.NewsDTO;
import com.verezragna.academy.service.mapper.NewsMapper;
import com.verezragna.academy.web.rest.errors.ExceptionTranslator;
import com.verezragna.academy.service.dto.NewsCriteria;
import com.verezragna.academy.service.NewsQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.verezragna.academy.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the NewsResource REST controller.
 *
 * @see NewsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DiplomaApp.class)
public class NewsResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_GROUP = "BBBBBBBBBB";

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsQueryService newsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNewsMockMvc;

    private News news;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NewsResource newsResource = new NewsResource(newsService, newsQueryService);
        this.restNewsMockMvc = MockMvcBuilders.standaloneSetup(newsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static News createEntity(EntityManager em) {
        News news = new News()
            .title(DEFAULT_TITLE)
            .image_url(DEFAULT_IMAGE_URL)
            .description(DEFAULT_DESCRIPTION)
            .text(DEFAULT_TEXT)
            .group(DEFAULT_GROUP);
        // Add required entity
        User user_news = UserResourceIntTest.createEntity(em);
        em.persist(user_news);
        em.flush();
        news.setUser_news(user_news);
        return news;
    }

    @Before
    public void initTest() {
        news = createEntity(em);
    }

    @Test
    @Transactional
    public void createNews() throws Exception {
        int databaseSizeBeforeCreate = newsRepository.findAll().size();

        // Create the News
        NewsDTO newsDTO = newsMapper.toDto(news);
        restNewsMockMvc.perform(post("/api/news")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsDTO)))
            .andExpect(status().isCreated());

        // Validate the News in the database
        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeCreate + 1);
        News testNews = newsList.get(newsList.size() - 1);
        assertThat(testNews.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNews.getImage_url()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testNews.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testNews.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testNews.getGroup()).isEqualTo(DEFAULT_GROUP);
    }

    @Test
    @Transactional
    public void createNewsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = newsRepository.findAll().size();

        // Create the News with an existing ID
        news.setId(1L);
        NewsDTO newsDTO = newsMapper.toDto(news);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNewsMockMvc.perform(post("/api/news")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the News in the database
        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = newsRepository.findAll().size();
        // set the field null
        news.setTitle(null);

        // Create the News, which fails.
        NewsDTO newsDTO = newsMapper.toDto(news);

        restNewsMockMvc.perform(post("/api/news")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsDTO)))
            .andExpect(status().isBadRequest());

        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImage_urlIsRequired() throws Exception {
        int databaseSizeBeforeTest = newsRepository.findAll().size();
        // set the field null
        news.setImage_url(null);

        // Create the News, which fails.
        NewsDTO newsDTO = newsMapper.toDto(news);

        restNewsMockMvc.perform(post("/api/news")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsDTO)))
            .andExpect(status().isBadRequest());

        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = newsRepository.findAll().size();
        // set the field null
        news.setDescription(null);

        // Create the News, which fails.
        NewsDTO newsDTO = newsMapper.toDto(news);

        restNewsMockMvc.perform(post("/api/news")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsDTO)))
            .andExpect(status().isBadRequest());

        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = newsRepository.findAll().size();
        // set the field null
        news.setText(null);

        // Create the News, which fails.
        NewsDTO newsDTO = newsMapper.toDto(news);

        restNewsMockMvc.perform(post("/api/news")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsDTO)))
            .andExpect(status().isBadRequest());

        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGroupIsRequired() throws Exception {
        int databaseSizeBeforeTest = newsRepository.findAll().size();
        // set the field null
        news.setGroup(null);

        // Create the News, which fails.
        NewsDTO newsDTO = newsMapper.toDto(news);

        restNewsMockMvc.perform(post("/api/news")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsDTO)))
            .andExpect(status().isBadRequest());

        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNews() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList
        restNewsMockMvc.perform(get("/api/news?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(news.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].image_url").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.toString())));
    }

    @Test
    @Transactional
    public void getNews() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get the news
        restNewsMockMvc.perform(get("/api/news/{id}", news.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(news.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.image_url").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.group").value(DEFAULT_GROUP.toString()));
    }

    @Test
    @Transactional
    public void getAllNewsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where title equals to DEFAULT_TITLE
        defaultNewsShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the newsList where title equals to UPDATED_TITLE
        defaultNewsShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllNewsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultNewsShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the newsList where title equals to UPDATED_TITLE
        defaultNewsShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllNewsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where title is not null
        defaultNewsShouldBeFound("title.specified=true");

        // Get all the newsList where title is null
        defaultNewsShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllNewsByImage_urlIsEqualToSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where image_url equals to DEFAULT_IMAGE_URL
        defaultNewsShouldBeFound("image_url.equals=" + DEFAULT_IMAGE_URL);

        // Get all the newsList where image_url equals to UPDATED_IMAGE_URL
        defaultNewsShouldNotBeFound("image_url.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllNewsByImage_urlIsInShouldWork() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where image_url in DEFAULT_IMAGE_URL or UPDATED_IMAGE_URL
        defaultNewsShouldBeFound("image_url.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL);

        // Get all the newsList where image_url equals to UPDATED_IMAGE_URL
        defaultNewsShouldNotBeFound("image_url.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllNewsByImage_urlIsNullOrNotNull() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where image_url is not null
        defaultNewsShouldBeFound("image_url.specified=true");

        // Get all the newsList where image_url is null
        defaultNewsShouldNotBeFound("image_url.specified=false");
    }

    @Test
    @Transactional
    public void getAllNewsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where description equals to DEFAULT_DESCRIPTION
        defaultNewsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the newsList where description equals to UPDATED_DESCRIPTION
        defaultNewsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllNewsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultNewsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the newsList where description equals to UPDATED_DESCRIPTION
        defaultNewsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllNewsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where description is not null
        defaultNewsShouldBeFound("description.specified=true");

        // Get all the newsList where description is null
        defaultNewsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllNewsByTextIsEqualToSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where text equals to DEFAULT_TEXT
        defaultNewsShouldBeFound("text.equals=" + DEFAULT_TEXT);

        // Get all the newsList where text equals to UPDATED_TEXT
        defaultNewsShouldNotBeFound("text.equals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllNewsByTextIsInShouldWork() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where text in DEFAULT_TEXT or UPDATED_TEXT
        defaultNewsShouldBeFound("text.in=" + DEFAULT_TEXT + "," + UPDATED_TEXT);

        // Get all the newsList where text equals to UPDATED_TEXT
        defaultNewsShouldNotBeFound("text.in=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllNewsByTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where text is not null
        defaultNewsShouldBeFound("text.specified=true");

        // Get all the newsList where text is null
        defaultNewsShouldNotBeFound("text.specified=false");
    }

    @Test
    @Transactional
    public void getAllNewsByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where group equals to DEFAULT_GROUP
        defaultNewsShouldBeFound("group.equals=" + DEFAULT_GROUP);

        // Get all the newsList where group equals to UPDATED_GROUP
        defaultNewsShouldNotBeFound("group.equals=" + UPDATED_GROUP);
    }

    @Test
    @Transactional
    public void getAllNewsByGroupIsInShouldWork() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where group in DEFAULT_GROUP or UPDATED_GROUP
        defaultNewsShouldBeFound("group.in=" + DEFAULT_GROUP + "," + UPDATED_GROUP);

        // Get all the newsList where group equals to UPDATED_GROUP
        defaultNewsShouldNotBeFound("group.in=" + UPDATED_GROUP);
    }

    @Test
    @Transactional
    public void getAllNewsByGroupIsNullOrNotNull() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newsList where group is not null
        defaultNewsShouldBeFound("group.specified=true");

        // Get all the newsList where group is null
        defaultNewsShouldNotBeFound("group.specified=false");
    }

    @Test
    @Transactional
    public void getAllNewsByUser_newsIsEqualToSomething() throws Exception {
        // Initialize the database
        User user_news = UserResourceIntTest.createEntity(em);
        em.persist(user_news);
        em.flush();
        news.setUser_news(user_news);
        newsRepository.saveAndFlush(news);
        Long user_newsId = user_news.getId();

        // Get all the newsList where user_news equals to user_newsId
        defaultNewsShouldBeFound("user_newsId.equals=" + user_newsId);

        // Get all the newsList where user_news equals to user_newsId + 1
        defaultNewsShouldNotBeFound("user_newsId.equals=" + (user_newsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultNewsShouldBeFound(String filter) throws Exception {
        restNewsMockMvc.perform(get("/api/news?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(news.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].image_url").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultNewsShouldNotBeFound(String filter) throws Exception {
        restNewsMockMvc.perform(get("/api/news?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingNews() throws Exception {
        // Get the news
        restNewsMockMvc.perform(get("/api/news/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNews() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);
        int databaseSizeBeforeUpdate = newsRepository.findAll().size();

        // Update the news
        News updatedNews = newsRepository.findOne(news.getId());
        // Disconnect from session so that the updates on updatedNews are not directly saved in db
        em.detach(updatedNews);
        updatedNews
            .title(UPDATED_TITLE)
            .image_url(UPDATED_IMAGE_URL)
            .description(UPDATED_DESCRIPTION)
            .text(UPDATED_TEXT)
            .group(UPDATED_GROUP);
        NewsDTO newsDTO = newsMapper.toDto(updatedNews);

        restNewsMockMvc.perform(put("/api/news")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsDTO)))
            .andExpect(status().isOk());

        // Validate the News in the database
        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeUpdate);
        News testNews = newsList.get(newsList.size() - 1);
        assertThat(testNews.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNews.getImage_url()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testNews.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNews.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testNews.getGroup()).isEqualTo(UPDATED_GROUP);
    }

    @Test
    @Transactional
    public void updateNonExistingNews() throws Exception {
        int databaseSizeBeforeUpdate = newsRepository.findAll().size();

        // Create the News
        NewsDTO newsDTO = newsMapper.toDto(news);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNewsMockMvc.perform(put("/api/news")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsDTO)))
            .andExpect(status().isCreated());

        // Validate the News in the database
        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNews() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);
        int databaseSizeBeforeDelete = newsRepository.findAll().size();

        // Get the news
        restNewsMockMvc.perform(delete("/api/news/{id}", news.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<News> newsList = newsRepository.findAll();
        assertThat(newsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(News.class);
        News news1 = new News();
        news1.setId(1L);
        News news2 = new News();
        news2.setId(news1.getId());
        assertThat(news1).isEqualTo(news2);
        news2.setId(2L);
        assertThat(news1).isNotEqualTo(news2);
        news1.setId(null);
        assertThat(news1).isNotEqualTo(news2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NewsDTO.class);
        NewsDTO newsDTO1 = new NewsDTO();
        newsDTO1.setId(1L);
        NewsDTO newsDTO2 = new NewsDTO();
        assertThat(newsDTO1).isNotEqualTo(newsDTO2);
        newsDTO2.setId(newsDTO1.getId());
        assertThat(newsDTO1).isEqualTo(newsDTO2);
        newsDTO2.setId(2L);
        assertThat(newsDTO1).isNotEqualTo(newsDTO2);
        newsDTO1.setId(null);
        assertThat(newsDTO1).isNotEqualTo(newsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(newsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(newsMapper.fromId(null)).isNull();
    }
}
