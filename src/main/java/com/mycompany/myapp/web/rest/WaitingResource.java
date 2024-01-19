package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Waiting;
import com.mycompany.myapp.repository.WaitingRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Waiting}.
 */
@RestController
@RequestMapping("/api/waitings")
@Transactional
public class WaitingResource {

    private final Logger log = LoggerFactory.getLogger(WaitingResource.class);

    private static final String ENTITY_NAME = "waiting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WaitingRepository waitingRepository;

    public WaitingResource(WaitingRepository waitingRepository) {
        this.waitingRepository = waitingRepository;
    }

    /**
     * {@code POST  /waitings} : Create a new waiting.
     *
     * @param waiting the waiting to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new waiting, or with status {@code 400 (Bad Request)} if the waiting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Waiting> createWaiting(@Valid @RequestBody Waiting waiting) throws URISyntaxException {
        log.debug("REST request to save Waiting : {}", waiting);
        if (waiting.getId() != null) {
            throw new BadRequestAlertException("A new waiting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Waiting result = waitingRepository.save(waiting);
        return ResponseEntity
            .created(new URI("/api/waitings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /waitings/:id} : Updates an existing waiting.
     *
     * @param id the id of the waiting to save.
     * @param waiting the waiting to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated waiting,
     * or with status {@code 400 (Bad Request)} if the waiting is not valid,
     * or with status {@code 500 (Internal Server Error)} if the waiting couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Waiting> updateWaiting(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Waiting waiting
    ) throws URISyntaxException {
        log.debug("REST request to update Waiting : {}, {}", id, waiting);
        if (waiting.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, waiting.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!waitingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Waiting result = waitingRepository.save(waiting);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, waiting.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /waitings/:id} : Partial updates given fields of an existing waiting, field will ignore if it is null
     *
     * @param id the id of the waiting to save.
     * @param waiting the waiting to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated waiting,
     * or with status {@code 400 (Bad Request)} if the waiting is not valid,
     * or with status {@code 404 (Not Found)} if the waiting is not found,
     * or with status {@code 500 (Internal Server Error)} if the waiting couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Waiting> partialUpdateWaiting(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Waiting waiting
    ) throws URISyntaxException {
        log.debug("REST request to partial update Waiting partially : {}, {}", id, waiting);
        if (waiting.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, waiting.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!waitingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Waiting> result = waitingRepository
            .findById(waiting.getId())
            .map(existingWaiting -> {
                if (waiting.getName() != null) {
                    existingWaiting.setName(waiting.getName());
                }
                if (waiting.getPhone() != null) {
                    existingWaiting.setPhone(waiting.getPhone());
                }
                if (waiting.getTimeArrived() != null) {
                    existingWaiting.setTimeArrived(waiting.getTimeArrived());
                }
                if (waiting.getTimeSummoned() != null) {
                    existingWaiting.setTimeSummoned(waiting.getTimeSummoned());
                }
                if (waiting.getTimeDone() != null) {
                    existingWaiting.setTimeDone(waiting.getTimeDone());
                }

                return existingWaiting;
            })
            .map(waitingRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, waiting.getId().toString())
        );
    }

    /**
     * {@code GET  /waitings} : get all the waitings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of waitings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Waiting>> getAllWaitings(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Waitings");
        Page<Waiting> page = waitingRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /waitings/:id} : get the "id" waiting.
     *
     * @param id the id of the waiting to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the waiting, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Waiting> getWaiting(@PathVariable("id") Long id) {
        log.debug("REST request to get Waiting : {}", id);
        Optional<Waiting> waiting = waitingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(waiting);
    }

    /**
     * {@code DELETE  /waitings/:id} : delete the "id" waiting.
     *
     * @param id the id of the waiting to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWaiting(@PathVariable("id") Long id) {
        log.debug("REST request to delete Waiting : {}", id);
        waitingRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
