package com.yesevi.bgysapp.web.rest;

import com.yesevi.bgysapp.domain.IsAkisi;
import com.yesevi.bgysapp.repository.IsAkisiRepository;
import com.yesevi.bgysapp.service.IsAkisiService;
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
 * REST controller for managing {@link com.yesevi.bgysapp.domain.IsAkisi}.
 */
@RestController
@RequestMapping("/api")
public class IsAkisiResource {

    private final Logger log = LoggerFactory.getLogger(IsAkisiResource.class);

    private static final String ENTITY_NAME = "isAkisi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IsAkisiService isAkisiService;

    private final IsAkisiRepository isAkisiRepository;

    public IsAkisiResource(IsAkisiService isAkisiService, IsAkisiRepository isAkisiRepository) {
        this.isAkisiService = isAkisiService;
        this.isAkisiRepository = isAkisiRepository;
    }

    /**
     * {@code POST  /is-akisis} : Create a new isAkisi.
     *
     * @param isAkisi the isAkisi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new isAkisi, or with status {@code 400 (Bad Request)} if the isAkisi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/is-akisis")
    public ResponseEntity<IsAkisi> createIsAkisi(@RequestBody IsAkisi isAkisi) throws URISyntaxException {
        log.debug("REST request to save IsAkisi : {}", isAkisi);
        if (isAkisi.getId() != null) {
            throw new BadRequestAlertException("A new isAkisi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IsAkisi result = isAkisiService.save(isAkisi);
        return ResponseEntity
            .created(new URI("/api/is-akisis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /is-akisis/:id} : Updates an existing isAkisi.
     *
     * @param id the id of the isAkisi to save.
     * @param isAkisi the isAkisi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated isAkisi,
     * or with status {@code 400 (Bad Request)} if the isAkisi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the isAkisi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/is-akisis/{id}")
    public ResponseEntity<IsAkisi> updateIsAkisi(@PathVariable(value = "id", required = false) final Long id, @RequestBody IsAkisi isAkisi)
        throws URISyntaxException {
        log.debug("REST request to update IsAkisi : {}, {}", id, isAkisi);
        if (isAkisi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, isAkisi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!isAkisiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IsAkisi result = isAkisiService.save(isAkisi);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, isAkisi.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /is-akisis/:id} : Partial updates given fields of an existing isAkisi, field will ignore if it is null
     *
     * @param id the id of the isAkisi to save.
     * @param isAkisi the isAkisi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated isAkisi,
     * or with status {@code 400 (Bad Request)} if the isAkisi is not valid,
     * or with status {@code 404 (Not Found)} if the isAkisi is not found,
     * or with status {@code 500 (Internal Server Error)} if the isAkisi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/is-akisis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IsAkisi> partialUpdateIsAkisi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IsAkisi isAkisi
    ) throws URISyntaxException {
        log.debug("REST request to partial update IsAkisi partially : {}, {}", id, isAkisi);
        if (isAkisi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, isAkisi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!isAkisiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IsAkisi> result = isAkisiService.partialUpdate(isAkisi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, isAkisi.getId().toString())
        );
    }

    /**
     * {@code GET  /is-akisis} : get all the isAkisis.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of isAkisis in body.
     */
    @GetMapping("/is-akisis")
    public ResponseEntity<List<IsAkisi>> getAllIsAkisis(Pageable pageable) {
        log.debug("REST request to get a page of IsAkisis");
        Page<IsAkisi> page = isAkisiService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /is-akisis/:id} : get the "id" isAkisi.
     *
     * @param id the id of the isAkisi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the isAkisi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/is-akisis/{id}")
    public ResponseEntity<IsAkisi> getIsAkisi(@PathVariable Long id) {
        log.debug("REST request to get IsAkisi : {}", id);
        Optional<IsAkisi> isAkisi = isAkisiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(isAkisi);
    }

    /**
     * {@code DELETE  /is-akisis/:id} : delete the "id" isAkisi.
     *
     * @param id the id of the isAkisi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/is-akisis/{id}")
    public ResponseEntity<Void> deleteIsAkisi(@PathVariable Long id) {
        log.debug("REST request to delete IsAkisi : {}", id);
        isAkisiService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
