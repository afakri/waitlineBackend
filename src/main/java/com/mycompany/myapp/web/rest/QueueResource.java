package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Queue;
import com.mycompany.myapp.repository.QueueRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Queue}.
 */
@RestController
@RequestMapping("/api/queues")
@Transactional
public class QueueResource {

    private final Logger log = LoggerFactory.getLogger(QueueResource.class);

    private static final String ENTITY_NAME = "queue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QueueRepository queueRepository;

    public QueueResource(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }

    /**
     * {@code POST  /queues} : Create a new queue.
     *
     * @param queue the queue to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new queue, or with status {@code 400 (Bad Request)} if the queue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Queue> createQueue(@Valid @RequestBody Queue queue) throws URISyntaxException {
        log.debug("REST request to save Queue : {}", queue);
        if (queue.getId() != null) {
            throw new BadRequestAlertException("A new queue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Queue result = queueRepository.save(queue);
        return ResponseEntity
            .created(new URI("/api/queues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /queues/:id} : Updates an existing queue.
     *
     * @param id the id of the queue to save.
     * @param queue the queue to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated queue,
     * or with status {@code 400 (Bad Request)} if the queue is not valid,
     * or with status {@code 500 (Internal Server Error)} if the queue couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Queue> updateQueue(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Queue queue)
        throws URISyntaxException {
        log.debug("REST request to update Queue : {}, {}", id, queue);
        if (queue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, queue.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!queueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Queue result = queueRepository.save(queue);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, queue.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /queues/:id} : Partial updates given fields of an existing queue, field will ignore if it is null
     *
     * @param id the id of the queue to save.
     * @param queue the queue to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated queue,
     * or with status {@code 400 (Bad Request)} if the queue is not valid,
     * or with status {@code 404 (Not Found)} if the queue is not found,
     * or with status {@code 500 (Internal Server Error)} if the queue couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Queue> partialUpdateQueue(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Queue queue
    ) throws URISyntaxException {
        log.debug("REST request to partial update Queue partially : {}, {}", id, queue);
        if (queue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, queue.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!queueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Queue> result = queueRepository
            .findById(queue.getId())
            .map(existingQueue -> {
                if (queue.getName() != null) {
                    existingQueue.setName(queue.getName());
                }

                return existingQueue;
            })
            .map(queueRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, queue.getId().toString())
        );
    }

    /**
     * {@code GET  /queues} : get all the queues.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of queues in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Queue>> getAllQueues(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Queues");
        Page<Queue> page;
        if (eagerload) {
            page = queueRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = queueRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /queues/:id} : get the "id" queue.
     *
     * @param id the id of the queue to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the queue, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Queue> getQueue(@PathVariable("id") Long id) {
        log.debug("REST request to get Queue : {}", id);
        Optional<Queue> queue = queueRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(queue);
    }

    /**
     * {@code DELETE  /queues/:id} : delete the "id" queue.
     *
     * @param id the id of the queue to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQueue(@PathVariable("id") Long id) {
        log.debug("REST request to delete Queue : {}", id);
        queueRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
