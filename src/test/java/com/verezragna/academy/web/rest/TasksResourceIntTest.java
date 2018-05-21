package com.verezragna.academy.web.rest;

import com.verezragna.academy.DiplomaApp;

import com.verezragna.academy.domain.Tasks;
import com.verezragna.academy.domain.User;
import com.verezragna.academy.repository.TasksRepository;
import com.verezragna.academy.service.TasksService;
import com.verezragna.academy.service.dto.TasksDTO;
import com.verezragna.academy.service.mapper.TasksMapper;
import com.verezragna.academy.web.rest.errors.ExceptionTranslator;
import com.verezragna.academy.service.dto.TasksCriteria;
import com.verezragna.academy.service.TasksQueryService;

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
 * Test class for the TasksResource REST controller.
 *
 * @see TasksResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DiplomaApp.class)
public class TasksResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_TASK = "AAAAAAAAAA";
    private static final String UPDATED_TASK = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_GROUP = "BBBBBBBBBB";

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private TasksMapper tasksMapper;

    @Autowired
    private TasksService tasksService;

    @Autowired
    private TasksQueryService tasksQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTasksMockMvc;

    private Tasks tasks;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TasksResource tasksResource = new TasksResource(tasksService, tasksQueryService);
        this.restTasksMockMvc = MockMvcBuilders.standaloneSetup(tasksResource)
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
    public static Tasks createEntity(EntityManager em) {
        Tasks tasks = new Tasks()
            .title(DEFAULT_TITLE)
            .task(DEFAULT_TASK)
            .image_url(DEFAULT_IMAGE_URL)
            .answer(DEFAULT_ANSWER)
            .group(DEFAULT_GROUP);
        // Add required entity
        User user_tasks = UserResourceIntTest.createEntity(em);
        em.persist(user_tasks);
        em.flush();
        tasks.setUser_tasks(user_tasks);
        return tasks;
    }

    @Before
    public void initTest() {
        tasks = createEntity(em);
    }

    @Test
    @Transactional
    public void createTasks() throws Exception {
        int databaseSizeBeforeCreate = tasksRepository.findAll().size();

        // Create the Tasks
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);
        restTasksMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tasksDTO)))
            .andExpect(status().isCreated());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeCreate + 1);
        Tasks testTasks = tasksList.get(tasksList.size() - 1);
        assertThat(testTasks.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTasks.getTask()).isEqualTo(DEFAULT_TASK);
        assertThat(testTasks.getImage_url()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testTasks.getAnswer()).isEqualTo(DEFAULT_ANSWER);
        assertThat(testTasks.getGroup()).isEqualTo(DEFAULT_GROUP);
    }

    @Test
    @Transactional
    public void createTasksWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tasksRepository.findAll().size();

        // Create the Tasks with an existing ID
        tasks.setId(1L);
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTasksMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tasksDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = tasksRepository.findAll().size();
        // set the field null
        tasks.setTitle(null);

        // Create the Tasks, which fails.
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);

        restTasksMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tasksDTO)))
            .andExpect(status().isBadRequest());

        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTaskIsRequired() throws Exception {
        int databaseSizeBeforeTest = tasksRepository.findAll().size();
        // set the field null
        tasks.setTask(null);

        // Create the Tasks, which fails.
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);

        restTasksMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tasksDTO)))
            .andExpect(status().isBadRequest());

        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnswerIsRequired() throws Exception {
        int databaseSizeBeforeTest = tasksRepository.findAll().size();
        // set the field null
        tasks.setAnswer(null);

        // Create the Tasks, which fails.
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);

        restTasksMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tasksDTO)))
            .andExpect(status().isBadRequest());

        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGroupIsRequired() throws Exception {
        int databaseSizeBeforeTest = tasksRepository.findAll().size();
        // set the field null
        tasks.setGroup(null);

        // Create the Tasks, which fails.
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);

        restTasksMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tasksDTO)))
            .andExpect(status().isBadRequest());

        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList
        restTasksMockMvc.perform(get("/api/tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tasks.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].task").value(hasItem(DEFAULT_TASK.toString())))
            .andExpect(jsonPath("$.[*].image_url").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER.toString())))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.toString())));
    }

    @Test
    @Transactional
    public void getTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get the tasks
        restTasksMockMvc.perform(get("/api/tasks/{id}", tasks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tasks.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.task").value(DEFAULT_TASK.toString()))
            .andExpect(jsonPath("$.image_url").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER.toString()))
            .andExpect(jsonPath("$.group").value(DEFAULT_GROUP.toString()));
    }

    @Test
    @Transactional
    public void getAllTasksByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where title equals to DEFAULT_TITLE
        defaultTasksShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the tasksList where title equals to UPDATED_TITLE
        defaultTasksShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllTasksByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultTasksShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the tasksList where title equals to UPDATED_TITLE
        defaultTasksShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllTasksByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where title is not null
        defaultTasksShouldBeFound("title.specified=true");

        // Get all the tasksList where title is null
        defaultTasksShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where task equals to DEFAULT_TASK
        defaultTasksShouldBeFound("task.equals=" + DEFAULT_TASK);

        // Get all the tasksList where task equals to UPDATED_TASK
        defaultTasksShouldNotBeFound("task.equals=" + UPDATED_TASK);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where task in DEFAULT_TASK or UPDATED_TASK
        defaultTasksShouldBeFound("task.in=" + DEFAULT_TASK + "," + UPDATED_TASK);

        // Get all the tasksList where task equals to UPDATED_TASK
        defaultTasksShouldNotBeFound("task.in=" + UPDATED_TASK);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where task is not null
        defaultTasksShouldBeFound("task.specified=true");

        // Get all the tasksList where task is null
        defaultTasksShouldNotBeFound("task.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByImage_urlIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where image_url equals to DEFAULT_IMAGE_URL
        defaultTasksShouldBeFound("image_url.equals=" + DEFAULT_IMAGE_URL);

        // Get all the tasksList where image_url equals to UPDATED_IMAGE_URL
        defaultTasksShouldNotBeFound("image_url.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllTasksByImage_urlIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where image_url in DEFAULT_IMAGE_URL or UPDATED_IMAGE_URL
        defaultTasksShouldBeFound("image_url.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL);

        // Get all the tasksList where image_url equals to UPDATED_IMAGE_URL
        defaultTasksShouldNotBeFound("image_url.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllTasksByImage_urlIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where image_url is not null
        defaultTasksShouldBeFound("image_url.specified=true");

        // Get all the tasksList where image_url is null
        defaultTasksShouldNotBeFound("image_url.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByAnswerIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where answer equals to DEFAULT_ANSWER
        defaultTasksShouldBeFound("answer.equals=" + DEFAULT_ANSWER);

        // Get all the tasksList where answer equals to UPDATED_ANSWER
        defaultTasksShouldNotBeFound("answer.equals=" + UPDATED_ANSWER);
    }

    @Test
    @Transactional
    public void getAllTasksByAnswerIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where answer in DEFAULT_ANSWER or UPDATED_ANSWER
        defaultTasksShouldBeFound("answer.in=" + DEFAULT_ANSWER + "," + UPDATED_ANSWER);

        // Get all the tasksList where answer equals to UPDATED_ANSWER
        defaultTasksShouldNotBeFound("answer.in=" + UPDATED_ANSWER);
    }

    @Test
    @Transactional
    public void getAllTasksByAnswerIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where answer is not null
        defaultTasksShouldBeFound("answer.specified=true");

        // Get all the tasksList where answer is null
        defaultTasksShouldNotBeFound("answer.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where group equals to DEFAULT_GROUP
        defaultTasksShouldBeFound("group.equals=" + DEFAULT_GROUP);

        // Get all the tasksList where group equals to UPDATED_GROUP
        defaultTasksShouldNotBeFound("group.equals=" + UPDATED_GROUP);
    }

    @Test
    @Transactional
    public void getAllTasksByGroupIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where group in DEFAULT_GROUP or UPDATED_GROUP
        defaultTasksShouldBeFound("group.in=" + DEFAULT_GROUP + "," + UPDATED_GROUP);

        // Get all the tasksList where group equals to UPDATED_GROUP
        defaultTasksShouldNotBeFound("group.in=" + UPDATED_GROUP);
    }

    @Test
    @Transactional
    public void getAllTasksByGroupIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where group is not null
        defaultTasksShouldBeFound("group.specified=true");

        // Get all the tasksList where group is null
        defaultTasksShouldNotBeFound("group.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByUser_tasksIsEqualToSomething() throws Exception {
        // Initialize the database
        User user_tasks = UserResourceIntTest.createEntity(em);
        em.persist(user_tasks);
        em.flush();
        tasks.setUser_tasks(user_tasks);
        tasksRepository.saveAndFlush(tasks);
        Long user_tasksId = user_tasks.getId();

        // Get all the tasksList where user_tasks equals to user_tasksId
        defaultTasksShouldBeFound("user_tasksId.equals=" + user_tasksId);

        // Get all the tasksList where user_tasks equals to user_tasksId + 1
        defaultTasksShouldNotBeFound("user_tasksId.equals=" + (user_tasksId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTasksShouldBeFound(String filter) throws Exception {
        restTasksMockMvc.perform(get("/api/tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tasks.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].task").value(hasItem(DEFAULT_TASK.toString())))
            .andExpect(jsonPath("$.[*].image_url").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER.toString())))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTasksShouldNotBeFound(String filter) throws Exception {
        restTasksMockMvc.perform(get("/api/tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTasks() throws Exception {
        // Get the tasks
        restTasksMockMvc.perform(get("/api/tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();

        // Update the tasks
        Tasks updatedTasks = tasksRepository.findOne(tasks.getId());
        // Disconnect from session so that the updates on updatedTasks are not directly saved in db
        em.detach(updatedTasks);
        updatedTasks
            .title(UPDATED_TITLE)
            .task(UPDATED_TASK)
            .image_url(UPDATED_IMAGE_URL)
            .answer(UPDATED_ANSWER)
            .group(UPDATED_GROUP);
        TasksDTO tasksDTO = tasksMapper.toDto(updatedTasks);

        restTasksMockMvc.perform(put("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tasksDTO)))
            .andExpect(status().isOk());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
        Tasks testTasks = tasksList.get(tasksList.size() - 1);
        assertThat(testTasks.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTasks.getTask()).isEqualTo(UPDATED_TASK);
        assertThat(testTasks.getImage_url()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testTasks.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testTasks.getGroup()).isEqualTo(UPDATED_GROUP);
    }

    @Test
    @Transactional
    public void updateNonExistingTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();

        // Create the Tasks
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTasksMockMvc.perform(put("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tasksDTO)))
            .andExpect(status().isCreated());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);
        int databaseSizeBeforeDelete = tasksRepository.findAll().size();

        // Get the tasks
        restTasksMockMvc.perform(delete("/api/tasks/{id}", tasks.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tasks.class);
        Tasks tasks1 = new Tasks();
        tasks1.setId(1L);
        Tasks tasks2 = new Tasks();
        tasks2.setId(tasks1.getId());
        assertThat(tasks1).isEqualTo(tasks2);
        tasks2.setId(2L);
        assertThat(tasks1).isNotEqualTo(tasks2);
        tasks1.setId(null);
        assertThat(tasks1).isNotEqualTo(tasks2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TasksDTO.class);
        TasksDTO tasksDTO1 = new TasksDTO();
        tasksDTO1.setId(1L);
        TasksDTO tasksDTO2 = new TasksDTO();
        assertThat(tasksDTO1).isNotEqualTo(tasksDTO2);
        tasksDTO2.setId(tasksDTO1.getId());
        assertThat(tasksDTO1).isEqualTo(tasksDTO2);
        tasksDTO2.setId(2L);
        assertThat(tasksDTO1).isNotEqualTo(tasksDTO2);
        tasksDTO1.setId(null);
        assertThat(tasksDTO1).isNotEqualTo(tasksDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tasksMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tasksMapper.fromId(null)).isNull();
    }
}
