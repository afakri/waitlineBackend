package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Queue;
import com.mycompany.myapp.repository.QueueRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QueueResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QueueResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/queues";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QueueRepository queueRepository;

    @Mock
    private QueueRepository queueRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQueueMockMvc;

    private Queue queue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Queue createEntity(EntityManager em) {
        Queue queue = new Queue().name(DEFAULT_NAME);
        return queue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Queue createUpdatedEntity(EntityManager em) {
        Queue queue = new Queue().name(UPDATED_NAME);
        return queue;
    }

    @BeforeEach
    public void initTest() {
        queue = createEntity(em);
    }

    @Test
    @Transactional
    void createQueue() throws Exception {
        int databaseSizeBeforeCreate = queueRepository.findAll().size();
        // Create the Queue
        restQueueMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(queue))
            )
            .andExpect(status().isCreated());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeCreate + 1);
        Queue testQueue = queueList.get(queueList.size() - 1);
        assertThat(testQueue.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createQueueWithExistingId() throws Exception {
        // Create the Queue with an existing ID
        queue.setId(1L);

        int databaseSizeBeforeCreate = queueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQueueMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(queue))
            )
            .andExpect(status().isBadRequest());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = queueRepository.findAll().size();
        // set the field null
        queue.setName(null);

        // Create the Queue, which fails.

        restQueueMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(queue))
            )
            .andExpect(status().isBadRequest());

        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQueues() throws Exception {
        // Initialize the database
        queueRepository.saveAndFlush(queue);

        // Get all the queueList
        restQueueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(queue.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQueuesWithEagerRelationshipsIsEnabled() throws Exception {
        when(queueRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQueueMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(queueRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQueuesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(queueRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQueueMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(queueRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQueue() throws Exception {
        // Initialize the database
        queueRepository.saveAndFlush(queue);

        // Get the queue
        restQueueMockMvc
            .perform(get(ENTITY_API_URL_ID, queue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(queue.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingQueue() throws Exception {
        // Get the queue
        restQueueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQueue() throws Exception {
        // Initialize the database
        queueRepository.saveAndFlush(queue);

        int databaseSizeBeforeUpdate = queueRepository.findAll().size();

        // Update the queue
        Queue updatedQueue = queueRepository.findById(queue.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQueue are not directly saved in db
        em.detach(updatedQueue);
        updatedQueue.name(UPDATED_NAME);

        restQueueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQueue.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQueue))
            )
            .andExpect(status().isOk());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
        Queue testQueue = queueList.get(queueList.size() - 1);
        assertThat(testQueue.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingQueue() throws Exception {
        int databaseSizeBeforeUpdate = queueRepository.findAll().size();
        queue.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQueueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, queue.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(queue))
            )
            .andExpect(status().isBadRequest());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQueue() throws Exception {
        int databaseSizeBeforeUpdate = queueRepository.findAll().size();
        queue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(queue))
            )
            .andExpect(status().isBadRequest());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQueue() throws Exception {
        int databaseSizeBeforeUpdate = queueRepository.findAll().size();
        queue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueueMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(queue))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQueueWithPatch() throws Exception {
        // Initialize the database
        queueRepository.saveAndFlush(queue);

        int databaseSizeBeforeUpdate = queueRepository.findAll().size();

        // Update the queue using partial update
        Queue partialUpdatedQueue = new Queue();
        partialUpdatedQueue.setId(queue.getId());

        restQueueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQueue.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQueue))
            )
            .andExpect(status().isOk());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
        Queue testQueue = queueList.get(queueList.size() - 1);
        assertThat(testQueue.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateQueueWithPatch() throws Exception {
        // Initialize the database
        queueRepository.saveAndFlush(queue);

        int databaseSizeBeforeUpdate = queueRepository.findAll().size();

        // Update the queue using partial update
        Queue partialUpdatedQueue = new Queue();
        partialUpdatedQueue.setId(queue.getId());

        partialUpdatedQueue.name(UPDATED_NAME);

        restQueueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQueue.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQueue))
            )
            .andExpect(status().isOk());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
        Queue testQueue = queueList.get(queueList.size() - 1);
        assertThat(testQueue.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingQueue() throws Exception {
        int databaseSizeBeforeUpdate = queueRepository.findAll().size();
        queue.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQueueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, queue.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(queue))
            )
            .andExpect(status().isBadRequest());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQueue() throws Exception {
        int databaseSizeBeforeUpdate = queueRepository.findAll().size();
        queue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(queue))
            )
            .andExpect(status().isBadRequest());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQueue() throws Exception {
        int databaseSizeBeforeUpdate = queueRepository.findAll().size();
        queue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(queue))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQueue() throws Exception {
        // Initialize the database
        queueRepository.saveAndFlush(queue);

        int databaseSizeBeforeDelete = queueRepository.findAll().size();

        // Delete the queue
        restQueueMockMvc
            .perform(delete(ENTITY_API_URL_ID, queue.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
