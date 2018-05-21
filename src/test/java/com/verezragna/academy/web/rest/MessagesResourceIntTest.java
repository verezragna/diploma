package com.verezragna.academy.web.rest;

import com.verezragna.academy.DiplomaApp;

import com.verezragna.academy.domain.Messages;
import com.verezragna.academy.domain.User;
import com.verezragna.academy.repository.MessagesRepository;
import com.verezragna.academy.service.MessagesService;
import com.verezragna.academy.web.rest.errors.ExceptionTranslator;
import com.verezragna.academy.service.dto.MessagesCriteria;
import com.verezragna.academy.service.MessagesQueryService;

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
 * Test class for the MessagesResource REST controller.
 *
 * @see MessagesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DiplomaApp.class)
public class MessagesResourceIntTest {

    private static final Integer DEFAULT_FROM = 1;
    private static final Integer UPDATED_FROM = 2;

    private static final Integer DEFAULT_TO = 1;
    private static final Integer UPDATED_TO = 2;

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private MessagesService messagesService;

    @Autowired
    private MessagesQueryService messagesQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMessagesMockMvc;

    private Messages messages;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MessagesResource messagesResource = new MessagesResource(messagesService, messagesQueryService);
        this.restMessagesMockMvc = MockMvcBuilders.standaloneSetup(messagesResource)
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
    public static Messages createEntity(EntityManager em) {
        Messages messages = new Messages()
            .from(DEFAULT_FROM)
            .to(DEFAULT_TO)
            .message(DEFAULT_MESSAGE);
        // Add required entity
        User user_messages = UserResourceIntTest.createEntity(em);
        em.persist(user_messages);
        em.flush();
        messages.getUser_messages().add(user_messages);
        return messages;
    }

    @Before
    public void initTest() {
        messages = createEntity(em);
    }

    @Test
    @Transactional
    public void createMessages() throws Exception {
        int databaseSizeBeforeCreate = messagesRepository.findAll().size();

        // Create the Messages
        restMessagesMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messages)))
            .andExpect(status().isCreated());

        // Validate the Messages in the database
        List<Messages> messagesList = messagesRepository.findAll();
        assertThat(messagesList).hasSize(databaseSizeBeforeCreate + 1);
        Messages testMessages = messagesList.get(messagesList.size() - 1);
        assertThat(testMessages.getFrom()).isEqualTo(DEFAULT_FROM);
        assertThat(testMessages.getTo()).isEqualTo(DEFAULT_TO);
        assertThat(testMessages.getMessage()).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    public void createMessagesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = messagesRepository.findAll().size();

        // Create the Messages with an existing ID
        messages.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessagesMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messages)))
            .andExpect(status().isBadRequest());

        // Validate the Messages in the database
        List<Messages> messagesList = messagesRepository.findAll();
        assertThat(messagesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = messagesRepository.findAll().size();
        // set the field null
        messages.setFrom(null);

        // Create the Messages, which fails.

        restMessagesMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messages)))
            .andExpect(status().isBadRequest());

        List<Messages> messagesList = messagesRepository.findAll();
        assertThat(messagesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkToIsRequired() throws Exception {
        int databaseSizeBeforeTest = messagesRepository.findAll().size();
        // set the field null
        messages.setTo(null);

        // Create the Messages, which fails.

        restMessagesMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messages)))
            .andExpect(status().isBadRequest());

        List<Messages> messagesList = messagesRepository.findAll();
        assertThat(messagesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = messagesRepository.findAll().size();
        // set the field null
        messages.setMessage(null);

        // Create the Messages, which fails.

        restMessagesMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messages)))
            .andExpect(status().isBadRequest());

        List<Messages> messagesList = messagesRepository.findAll();
        assertThat(messagesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMessages() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get all the messagesList
        restMessagesMockMvc.perform(get("/api/messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messages.getId().intValue())))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM)))
            .andExpect(jsonPath("$.[*].to").value(hasItem(DEFAULT_TO)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }

    @Test
    @Transactional
    public void getMessages() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get the messages
        restMessagesMockMvc.perform(get("/api/messages/{id}", messages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(messages.getId().intValue()))
            .andExpect(jsonPath("$.from").value(DEFAULT_FROM))
            .andExpect(jsonPath("$.to").value(DEFAULT_TO))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()));
    }

    @Test
    @Transactional
    public void getAllMessagesByFromIsEqualToSomething() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get all the messagesList where from equals to DEFAULT_FROM
        defaultMessagesShouldBeFound("from.equals=" + DEFAULT_FROM);

        // Get all the messagesList where from equals to UPDATED_FROM
        defaultMessagesShouldNotBeFound("from.equals=" + UPDATED_FROM);
    }

    @Test
    @Transactional
    public void getAllMessagesByFromIsInShouldWork() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get all the messagesList where from in DEFAULT_FROM or UPDATED_FROM
        defaultMessagesShouldBeFound("from.in=" + DEFAULT_FROM + "," + UPDATED_FROM);

        // Get all the messagesList where from equals to UPDATED_FROM
        defaultMessagesShouldNotBeFound("from.in=" + UPDATED_FROM);
    }

    @Test
    @Transactional
    public void getAllMessagesByFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get all the messagesList where from is not null
        defaultMessagesShouldBeFound("from.specified=true");

        // Get all the messagesList where from is null
        defaultMessagesShouldNotBeFound("from.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessagesByFromIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get all the messagesList where from greater than or equals to DEFAULT_FROM
        defaultMessagesShouldBeFound("from.greaterOrEqualThan=" + DEFAULT_FROM);

        // Get all the messagesList where from greater than or equals to UPDATED_FROM
        defaultMessagesShouldNotBeFound("from.greaterOrEqualThan=" + UPDATED_FROM);
    }

    @Test
    @Transactional
    public void getAllMessagesByFromIsLessThanSomething() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get all the messagesList where from less than or equals to DEFAULT_FROM
        defaultMessagesShouldNotBeFound("from.lessThan=" + DEFAULT_FROM);

        // Get all the messagesList where from less than or equals to UPDATED_FROM
        defaultMessagesShouldBeFound("from.lessThan=" + UPDATED_FROM);
    }


    @Test
    @Transactional
    public void getAllMessagesByToIsEqualToSomething() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get all the messagesList where to equals to DEFAULT_TO
        defaultMessagesShouldBeFound("to.equals=" + DEFAULT_TO);

        // Get all the messagesList where to equals to UPDATED_TO
        defaultMessagesShouldNotBeFound("to.equals=" + UPDATED_TO);
    }

    @Test
    @Transactional
    public void getAllMessagesByToIsInShouldWork() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get all the messagesList where to in DEFAULT_TO or UPDATED_TO
        defaultMessagesShouldBeFound("to.in=" + DEFAULT_TO + "," + UPDATED_TO);

        // Get all the messagesList where to equals to UPDATED_TO
        defaultMessagesShouldNotBeFound("to.in=" + UPDATED_TO);
    }

    @Test
    @Transactional
    public void getAllMessagesByToIsNullOrNotNull() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get all the messagesList where to is not null
        defaultMessagesShouldBeFound("to.specified=true");

        // Get all the messagesList where to is null
        defaultMessagesShouldNotBeFound("to.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessagesByToIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get all the messagesList where to greater than or equals to DEFAULT_TO
        defaultMessagesShouldBeFound("to.greaterOrEqualThan=" + DEFAULT_TO);

        // Get all the messagesList where to greater than or equals to UPDATED_TO
        defaultMessagesShouldNotBeFound("to.greaterOrEqualThan=" + UPDATED_TO);
    }

    @Test
    @Transactional
    public void getAllMessagesByToIsLessThanSomething() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get all the messagesList where to less than or equals to DEFAULT_TO
        defaultMessagesShouldNotBeFound("to.lessThan=" + DEFAULT_TO);

        // Get all the messagesList where to less than or equals to UPDATED_TO
        defaultMessagesShouldBeFound("to.lessThan=" + UPDATED_TO);
    }


    @Test
    @Transactional
    public void getAllMessagesByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get all the messagesList where message equals to DEFAULT_MESSAGE
        defaultMessagesShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the messagesList where message equals to UPDATED_MESSAGE
        defaultMessagesShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllMessagesByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get all the messagesList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultMessagesShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the messagesList where message equals to UPDATED_MESSAGE
        defaultMessagesShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllMessagesByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get all the messagesList where message is not null
        defaultMessagesShouldBeFound("message.specified=true");

        // Get all the messagesList where message is null
        defaultMessagesShouldNotBeFound("message.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessagesByUser_messagesIsEqualToSomething() throws Exception {
        // Initialize the database
        User user_messages = UserResourceIntTest.createEntity(em);
        em.persist(user_messages);
        em.flush();
        messages.addUser_messages(user_messages);
        messagesRepository.saveAndFlush(messages);
        Long user_messagesId = user_messages.getId();

        // Get all the messagesList where user_messages equals to user_messagesId
        defaultMessagesShouldBeFound("user_messagesId.equals=" + user_messagesId);

        // Get all the messagesList where user_messages equals to user_messagesId + 1
        defaultMessagesShouldNotBeFound("user_messagesId.equals=" + (user_messagesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMessagesShouldBeFound(String filter) throws Exception {
        restMessagesMockMvc.perform(get("/api/messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messages.getId().intValue())))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM)))
            .andExpect(jsonPath("$.[*].to").value(hasItem(DEFAULT_TO)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMessagesShouldNotBeFound(String filter) throws Exception {
        restMessagesMockMvc.perform(get("/api/messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMessages() throws Exception {
        // Get the messages
        restMessagesMockMvc.perform(get("/api/messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessages() throws Exception {
        // Initialize the database
        messagesService.save(messages);

        int databaseSizeBeforeUpdate = messagesRepository.findAll().size();

        // Update the messages
        Messages updatedMessages = messagesRepository.findOne(messages.getId());
        // Disconnect from session so that the updates on updatedMessages are not directly saved in db
        em.detach(updatedMessages);
        updatedMessages
            .from(UPDATED_FROM)
            .to(UPDATED_TO)
            .message(UPDATED_MESSAGE);

        restMessagesMockMvc.perform(put("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMessages)))
            .andExpect(status().isOk());

        // Validate the Messages in the database
        List<Messages> messagesList = messagesRepository.findAll();
        assertThat(messagesList).hasSize(databaseSizeBeforeUpdate);
        Messages testMessages = messagesList.get(messagesList.size() - 1);
        assertThat(testMessages.getFrom()).isEqualTo(UPDATED_FROM);
        assertThat(testMessages.getTo()).isEqualTo(UPDATED_TO);
        assertThat(testMessages.getMessage()).isEqualTo(UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingMessages() throws Exception {
        int databaseSizeBeforeUpdate = messagesRepository.findAll().size();

        // Create the Messages

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMessagesMockMvc.perform(put("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messages)))
            .andExpect(status().isCreated());

        // Validate the Messages in the database
        List<Messages> messagesList = messagesRepository.findAll();
        assertThat(messagesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMessages() throws Exception {
        // Initialize the database
        messagesService.save(messages);

        int databaseSizeBeforeDelete = messagesRepository.findAll().size();

        // Get the messages
        restMessagesMockMvc.perform(delete("/api/messages/{id}", messages.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Messages> messagesList = messagesRepository.findAll();
        assertThat(messagesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Messages.class);
        Messages messages1 = new Messages();
        messages1.setId(1L);
        Messages messages2 = new Messages();
        messages2.setId(messages1.getId());
        assertThat(messages1).isEqualTo(messages2);
        messages2.setId(2L);
        assertThat(messages1).isNotEqualTo(messages2);
        messages1.setId(null);
        assertThat(messages1).isNotEqualTo(messages2);
    }
}
