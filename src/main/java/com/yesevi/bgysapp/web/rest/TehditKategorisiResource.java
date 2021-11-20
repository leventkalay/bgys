package com.yesevi.bgysapp.web.rest;

import com.yesevi.bgysapp.domain.TehditKategorisi;
import com.yesevi.bgysapp.repository.TehditKategorisiRepository;
import com.yesevi.bgysapp.service.TehditKategorisiService;
import com.yesevi.bgysapp.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.yesevi.bgysapp.domain.TehditKategorisi}.
 */
@RestController
@RequestMapping("/api")
public class TehditKategorisiResource {

    private final Logger log = LoggerFactory.getLogger(TehditKategorisiResource.class);

    private static final String ENTITY_NAME = "tehditKategorisi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TehditKategorisiService tehditKategorisiService;

    private final TehditKategorisiRepository tehditKategorisiRepository;

    public TehditKategorisiResource(
        TehditKategorisiService tehditKategorisiService,
        TehditKategorisiRepository tehditKategorisiRepository
    ) {
        this.tehditKategorisiService = tehditKategorisiService;
        this.tehditKategorisiRepository = tehditKategorisiRepository;
    }

    /**
     * {@code POST  /tehdit-kategorisis} : Create a new tehditKategorisi.
     *
     * @param tehditKategorisi the tehditKategorisi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tehditKategorisi, or with status {@code 400 (Bad Request)} if the tehditKategorisi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tehdit-kategorisis")
    public ResponseEntity<TehditKategorisi> createTehditKategorisi(@RequestBody TehditKategorisi tehditKategorisi)
        throws URISyntaxException {
        log.debug("REST request to save TehditKategorisi : {}", tehditKategorisi);
        if (tehditKategorisi.getId() != null) {
            throw new BadRequestAlertException("A new tehditKategorisi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TehditKategorisi result = tehditKategorisiService.save(tehditKategorisi);
        return ResponseEntity
            .created(new URI("/api/tehdit-kategorisis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tehdit-kategorisis/:id} : Updates an existing tehditKategorisi.
     *
     * @param id the id of the tehditKategorisi to save.
     * @param tehditKategorisi the tehditKategorisi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tehditKategorisi,
     * or with status {@code 400 (Bad Request)} if the tehditKategorisi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tehditKategorisi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tehdit-kategorisis/{id}")
    public ResponseEntity<TehditKategorisi> updateTehditKategorisi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TehditKategorisi tehditKategorisi
    ) throws URISyntaxException {
        log.debug("REST request to update TehditKategorisi : {}, {}", id, tehditKategorisi);
        if (tehditKategorisi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tehditKategorisi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tehditKategorisiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TehditKategorisi result = tehditKategorisiService.save(tehditKategorisi);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tehditKategorisi.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tehdit-kategorisis/:id} : Partial updates given fields of an existing tehditKategorisi, field will ignore if it is null
     *
     * @param id the id of the tehditKategorisi to save.
     * @param tehditKategorisi the tehditKategorisi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tehditKategorisi,
     * or with status {@code 400 (Bad Request)} if the tehditKategorisi is not valid,
     * or with status {@code 404 (Not Found)} if the tehditKategorisi is not found,
     * or with status {@code 500 (Internal Server Error)} if the tehditKategorisi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tehdit-kategorisis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TehditKategorisi> partialUpdateTehditKategorisi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TehditKategorisi tehditKategorisi
    ) throws URISyntaxException {
        log.debug("REST request to partial update TehditKategorisi partially : {}, {}", id, tehditKategorisi);
        if (tehditKategorisi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tehditKategorisi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tehditKategorisiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TehditKategorisi> result = tehditKategorisiService.partialUpdate(tehditKategorisi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tehditKategorisi.getId().toString())
        );
    }

    /**
     * {@code GET  /tehdit-kategorisis} : get all the tehditKategorisis.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tehditKategorisis in body.
     */
    @GetMapping("/tehdit-kategorisis")
    public ResponseEntity<List<TehditKategorisi>> getAllTehditKategorisis(Pageable pageable) {
        log.debug("REST request to get a page of TehditKategorisis");
        Page<TehditKategorisi> page = tehditKategorisiService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tehdit-kategorisis/:id} : get the "id" tehditKategorisi.
     *
     * @param id the id of the tehditKategorisi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tehditKategorisi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tehdit-kategorisis/{id}")
    public ResponseEntity<TehditKategorisi> getTehditKategorisi(@PathVariable Long id) {
        log.debug("REST request to get TehditKategorisi : {}", id);
        Optional<TehditKategorisi> tehditKategorisi = tehditKategorisiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tehditKategorisi);
    }

    /**
     * {@code DELETE  /tehdit-kategorisis/:id} : delete the "id" tehditKategorisi.
     *
     * @param id the id of the tehditKategorisi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tehdit-kategorisis/{id}")
    public ResponseEntity<Void> deleteTehditKategorisi(@PathVariable Long id) {
        log.debug("REST request to delete TehditKategorisi : {}", id);
        tehditKategorisiService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
