package com.yesevi.bgysapp.web.rest;

import com.yesevi.bgysapp.domain.Aksiyon;
import com.yesevi.bgysapp.repository.AksiyonRepository;
import com.yesevi.bgysapp.service.AksiyonService;
import com.yesevi.bgysapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.yesevi.bgysapp.domain.Aksiyon}.
 */
@RestController
@RequestMapping("/api")
public class AksiyonResource {

    private final Logger log = LoggerFactory.getLogger(AksiyonResource.class);

    private static final String ENTITY_NAME = "aksiyon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AksiyonService aksiyonService;

    private final AksiyonRepository aksiyonRepository;

    public AksiyonResource(AksiyonService aksiyonService, AksiyonRepository aksiyonRepository) {
        this.aksiyonService = aksiyonService;
        this.aksiyonRepository = aksiyonRepository;
    }

    /**
     * {@code POST  /aksiyons} : Create a new aksiyon.
     *
     * @param aksiyon the aksiyon to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aksiyon, or with status {@code 400 (Bad Request)} if the aksiyon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aksiyons")
    public ResponseEntity<Aksiyon> createAksiyon(@Valid @RequestBody Aksiyon aksiyon) throws URISyntaxException {
        log.debug("REST request to save Aksiyon : {}", aksiyon);
        if (aksiyon.getId() != null) {
            throw new BadRequestAlertException("A new aksiyon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Aksiyon result = aksiyonService.save(aksiyon);
        return ResponseEntity
            .created(new URI("/api/aksiyons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aksiyons/:id} : Updates an existing aksiyon.
     *
     * @param id the id of the aksiyon to save.
     * @param aksiyon the aksiyon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aksiyon,
     * or with status {@code 400 (Bad Request)} if the aksiyon is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aksiyon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aksiyons/{id}")
    public ResponseEntity<Aksiyon> updateAksiyon(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Aksiyon aksiyon
    ) throws URISyntaxException {
        log.debug("REST request to update Aksiyon : {}, {}", id, aksiyon);
        if (aksiyon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aksiyon.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aksiyonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Aksiyon result = aksiyonService.save(aksiyon);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aksiyon.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aksiyons/:id} : Partial updates given fields of an existing aksiyon, field will ignore if it is null
     *
     * @param id the id of the aksiyon to save.
     * @param aksiyon the aksiyon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aksiyon,
     * or with status {@code 400 (Bad Request)} if the aksiyon is not valid,
     * or with status {@code 404 (Not Found)} if the aksiyon is not found,
     * or with status {@code 500 (Internal Server Error)} if the aksiyon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/aksiyons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Aksiyon> partialUpdateAksiyon(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Aksiyon aksiyon
    ) throws URISyntaxException {
        log.debug("REST request to partial update Aksiyon partially : {}, {}", id, aksiyon);
        if (aksiyon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aksiyon.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aksiyonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Aksiyon> result = aksiyonService.partialUpdate(aksiyon);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aksiyon.getId().toString())
        );
    }

    /**
     * {@code GET  /aksiyons} : get all the aksiyons.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aksiyons in body.
     */
    @GetMapping("/aksiyons")
    public ResponseEntity<List<Aksiyon>> getAllAksiyons(Pageable pageable) {
        log.debug("REST request to get a page of Aksiyons");
        Page<Aksiyon> page = aksiyonService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /aksiyons/:id} : get the "id" aksiyon.
     *
     * @param id the id of the aksiyon to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aksiyon, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aksiyons/{id}")
    public ResponseEntity<Aksiyon> getAksiyon(@PathVariable Long id) {
        log.debug("REST request to get Aksiyon : {}", id);
        Optional<Aksiyon> aksiyon = aksiyonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aksiyon);
    }

    /**
     * {@code DELETE  /aksiyons/:id} : delete the "id" aksiyon.
     *
     * @param id the id of the aksiyon to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aksiyons/{id}")
    public ResponseEntity<Void> deleteAksiyon(@PathVariable Long id) {
        log.debug("REST request to delete Aksiyon : {}", id);
        aksiyonService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
