package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Waiting;
import com.mycompany.myapp.repository.WaitingRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WaitingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WaitingResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIME_ARRIVED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_ARRIVED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_TIME_SUMMONED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_SUMMONED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_TIME_DONE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_DONE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/waitings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WaitingRepository waitingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWaitingMockMvc;

    private Waiting waiting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Waiting createEntity(EntityManager em) {
        Waiting waiting = new Waiting()
            .name(DEFAULT_NAME)
            .phone(DEFAULT_PHONE)
            .timeArrived(DEFAULT_TIME_ARRIVED)
            .timeSummoned(DEFAULT_TIME_SUMMONED)
            .timeDone(DEFAULT_TIME_DONE);
        return waiting;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Waiting createUpdatedEntity(EntityManager em) {
        Waiting waiting = new Waiting()
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .timeArrived(UPDATED_TIME_ARRIVED)
            .timeSummoned(UPDATED_TIME_SUMMONED)
            .timeDone(UPDATED_TIME_DONE);
        return waiting;
    }

    @BeforeEach
    public void initTest() {
        waiting = createEntity(em);
    }

    @Test
    @Transactional
    void createWaiting() throws Exception {
        int databaseSizeBeforeCreate = waitingRepository.findAll().size();
        // Create the Waiting
        restWaitingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waiting))
            )
            .andExpect(status().isCreated());

        // Validate the Waiting in the database
        List<Waiting> waitingList = waitingRepository.findAll();
        assertThat(waitingList).hasSize(databaseSizeBeforeCreate + 1);
        Waiting testWaiting = waitingList.get(waitingList.size() - 1);
        assertThat(testWaiting.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWaiting.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testWaiting.getTimeArrived()).isEqualTo(DEFAULT_TIME_ARRIVED);
        assertThat(testWaiting.getTimeSummoned()).isEqualTo(DEFAULT_TIME_SUMMONED);
        assertThat(testWaiting.getTimeDone()).isEqualTo(DEFAULT_TIME_DONE);
    }

    @Test
    @Transactional
    void createWaitingWithExistingId() throws Exception {
        // Create the Waiting with an existing ID
        waiting.setId(1L);

        int databaseSizeBeforeCreate = waitingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWaitingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waiting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Waiting in the database
        List<Waiting> waitingList = waitingRepository.findAll();
        assertThat(waitingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = waitingRepository.findAll().size();
        // set the field null
        waiting.setName(null);

        // Create the Waiting, which fails.

        restWaitingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waiting))
            )
            .andExpect(status().isBadRequest());

        List<Waiting> waitingList = waitingRepository.findAll();
        assertThat(waitingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = waitingRepository.findAll().size();
        // set the field null
        waiting.setPhone(null);

        // Create the Waiting, which fails.

        restWaitingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waiting))
            )
            .andExpect(status().isBadRequest());

        List<Waiting> waitingList = waitingRepository.findAll();
        assertThat(waitingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWaitings() throws Exception {
        // Initialize the database
        waitingRepository.saveAndFlush(waiting);

        // Get all the waitingList
        restWaitingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(waiting.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].timeArrived").value(hasItem(DEFAULT_TIME_ARRIVED.toString())))
            .andExpect(jsonPath("$.[*].timeSummoned").value(hasItem(DEFAULT_TIME_SUMMONED.toString())))
            .andExpect(jsonPath("$.[*].timeDone").value(hasItem(DEFAULT_TIME_DONE.toString())));
    }

    @Test
    @Transactional
    void getWaiting() throws Exception {
        // Initialize the database
        waitingRepository.saveAndFlush(waiting);

        // Get the waiting
        restWaitingMockMvc
            .perform(get(ENTITY_API_URL_ID, waiting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(waiting.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.timeArrived").value(DEFAULT_TIME_ARRIVED.toString()))
            .andExpect(jsonPath("$.timeSummoned").value(DEFAULT_TIME_SUMMONED.toString()))
            .andExpect(jsonPath("$.timeDone").value(DEFAULT_TIME_DONE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWaiting() throws Exception {
        // Get the waiting
        restWaitingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWaiting() throws Exception {
        // Initialize the database
        waitingRepository.saveAndFlush(waiting);

        int databaseSizeBeforeUpdate = waitingRepository.findAll().size();

        // Update the waiting
        Waiting updatedWaiting = waitingRepository.findById(waiting.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWaiting are not directly saved in db
        em.detach(updatedWaiting);
        updatedWaiting
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .timeArrived(UPDATED_TIME_ARRIVED)
            .timeSummoned(UPDATED_TIME_SUMMONED)
            .timeDone(UPDATED_TIME_DONE);

        restWaitingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWaiting.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWaiting))
            )
            .andExpect(status().isOk());

        // Validate the Waiting in the database
        List<Waiting> waitingList = waitingRepository.findAll();
        assertThat(waitingList).hasSize(databaseSizeBeforeUpdate);
        Waiting testWaiting = waitingList.get(waitingList.size() - 1);
        assertThat(testWaiting.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWaiting.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testWaiting.getTimeArrived()).isEqualTo(UPDATED_TIME_ARRIVED);
        assertThat(testWaiting.getTimeSummoned()).isEqualTo(UPDATED_TIME_SUMMONED);
        assertThat(testWaiting.getTimeDone()).isEqualTo(UPDATED_TIME_DONE);
    }

    @Test
    @Transactional
    void putNonExistingWaiting() throws Exception {
        int databaseSizeBeforeUpdate = waitingRepository.findAll().size();
        waiting.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWaitingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, waiting.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waiting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Waiting in the database
        List<Waiting> waitingList = waitingRepository.findAll();
        assertThat(waitingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWaiting() throws Exception {
        int databaseSizeBeforeUpdate = waitingRepository.findAll().size();
        waiting.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaitingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waiting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Waiting in the database
        List<Waiting> waitingList = waitingRepository.findAll();
        assertThat(waitingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWaiting() throws Exception {
        int databaseSizeBeforeUpdate = waitingRepository.findAll().size();
        waiting.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaitingMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(waiting))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Waiting in the database
        List<Waiting> waitingList = waitingRepository.findAll();
        assertThat(waitingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWaitingWithPatch() throws Exception {
        // Initialize the database
        waitingRepository.saveAndFlush(waiting);

        int databaseSizeBeforeUpdate = waitingRepository.findAll().size();

        // Update the waiting using partial update
        Waiting partialUpdatedWaiting = new Waiting();
        partialUpdatedWaiting.setId(waiting.getId());

        partialUpdatedWaiting.name(UPDATED_NAME).phone(UPDATED_PHONE).timeSummoned(UPDATED_TIME_SUMMONED);

        restWaitingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWaiting.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWaiting))
            )
            .andExpect(status().isOk());

        // Validate the Waiting in the database
        List<Waiting> waitingList = waitingRepository.findAll();
        assertThat(waitingList).hasSize(databaseSizeBeforeUpdate);
        Waiting testWaiting = waitingList.get(waitingList.size() - 1);
        assertThat(testWaiting.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWaiting.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testWaiting.getTimeArrived()).isEqualTo(DEFAULT_TIME_ARRIVED);
        assertThat(testWaiting.getTimeSummoned()).isEqualTo(UPDATED_TIME_SUMMONED);
        assertThat(testWaiting.getTimeDone()).isEqualTo(DEFAULT_TIME_DONE);
    }

    @Test
    @Transactional
    void fullUpdateWaitingWithPatch() throws Exception {
        // Initialize the database
        waitingRepository.saveAndFlush(waiting);

        int databaseSizeBeforeUpdate = waitingRepository.findAll().size();

        // Update the waiting using partial update
        Waiting partialUpdatedWaiting = new Waiting();
        partialUpdatedWaiting.setId(waiting.getId());

        partialUpdatedWaiting
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .timeArrived(UPDATED_TIME_ARRIVED)
            .timeSummoned(UPDATED_TIME_SUMMONED)
            .timeDone(UPDATED_TIME_DONE);

        restWaitingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWaiting.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWaiting))
            )
            .andExpect(status().isOk());

        // Validate the Waiting in the database
        List<Waiting> waitingList = waitingRepository.findAll();
        assertThat(waitingList).hasSize(databaseSizeBeforeUpdate);
        Waiting testWaiting = waitingList.get(waitingList.size() - 1);
        assertThat(testWaiting.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWaiting.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testWaiting.getTimeArrived()).isEqualTo(UPDATED_TIME_ARRIVED);
        assertThat(testWaiting.getTimeSummoned()).isEqualTo(UPDATED_TIME_SUMMONED);
        assertThat(testWaiting.getTimeDone()).isEqualTo(UPDATED_TIME_DONE);
    }

    @Test
    @Transactional
    void patchNonExistingWaiting() throws Exception {
        int databaseSizeBeforeUpdate = waitingRepository.findAll().size();
        waiting.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWaitingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, waiting.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(waiting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Waiting in the database
        List<Waiting> waitingList = waitingRepository.findAll();
        assertThat(waitingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWaiting() throws Exception {
        int databaseSizeBeforeUpdate = waitingRepository.findAll().size();
        waiting.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaitingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(waiting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Waiting in the database
        List<Waiting> waitingList = waitingRepository.findAll();
        assertThat(waitingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWaiting() throws Exception {
        int databaseSizeBeforeUpdate = waitingRepository.findAll().size();
        waiting.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaitingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(waiting))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Waiting in the database
        List<Waiting> waitingList = waitingRepository.findAll();
        assertThat(waitingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWaiting() throws Exception {
        // Initialize the database
        waitingRepository.saveAndFlush(waiting);

        int databaseSizeBeforeDelete = waitingRepository.findAll().size();

        // Delete the waiting
        restWaitingMockMvc
            .perform(delete(ENTITY_API_URL_ID, waiting.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Waiting> waitingList = waitingRepository.findAll();
        assertThat(waitingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
