package com.yesevi.bgysapp.web.rest;

import com.yesevi.bgysapp.domain.Tehdit;
import com.yesevi.bgysapp.repository.TehditRepository;
import com.yesevi.bgysapp.service.TehditService;
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
 * REST controller for managing {@link com.yesevi.bgysapp.domain.Tehdit}.
 */
@RestController
@RequestMapping("/api")
public class TehditResource {

    private final Logger log = LoggerFactory.getLogger(TehditResource.class);

    private static final String ENTITY_NAME = "tehdit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TehditService tehditService;

    private final TehditRepository tehditRepository;

    public TehditResource(TehditService tehditService, TehditRepository tehditRepository) {
        this.tehditService = tehditService;
        this.tehditRepository = tehditRepository;
    }

    /**
     * {@code POST  /tehdits} : Create a new tehdit.
     *
     * @param tehdit the tehdit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tehdit, or with status {@code 400 (Bad Request)} if the tehdit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tehdits")
    public ResponseEntity<Tehdit> createTehdit(@Valid @RequestBody Tehdit tehdit) throws URISyntaxException {
        log.debug("REST request to save Tehdit : {}", tehdit);
        if (tehdit.getId() != null) {
            throw new BadRequestAlertException("A new tehdit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tehdit result = tehditService.save(tehdit);
        return ResponseEntity
            .created(new URI("/api/tehdits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tehdits/:id} : Updates an existing tehdit.
     *
     * @param id the id of the tehdit to save.
     * @param tehdit the tehdit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tehdit,
     * or with status {@code 400 (Bad Request)} if the tehdit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tehdit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tehdits/{id}")
    public ResponseEntity<Tehdit> updateTehdit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Tehdit tehdit
    ) throws URISyntaxException {
        log.debug("REST request to update Tehdit : {}, {}", id, tehdit);
        if (tehdit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tehdit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tehditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tehdit result = tehditService.save(tehdit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tehdit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tehdits/:id} : Partial updates given fields of an existing tehdit, field will ignore if it is null
     *
     * @param id the id of the tehdit to save.
     * @param tehdit the tehdit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tehdit,
     * or with status {@code 400 (Bad Request)} if the tehdit is not valid,
     * or with status {@code 404 (Not Found)} if the tehdit is not found,
     * or with status {@code 500 (Internal Server Error)} if the tehdit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tehdits/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Tehdit> partialUpdateTehdit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Tehdit tehdit
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tehdit partially : {}, {}", id, tehdit);
        if (tehdit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tehdit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tehditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tehdit> result = tehditService.partialUpdate(tehdit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tehdit.getId().toString())
        );
    }

    /**
     * {@code GET  /tehdits} : get all the tehdits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tehdits in body.
     */
    @GetMapping("/tehdits")
    public ResponseEntity<List<Tehdit>> getAllTehdits(Pageable pageable) {
        log.debug("REST request to get a page of Tehdits");
        Page<Tehdit> page = tehditService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tehdits/:id} : get the "id" tehdit.
     *
     * @param id the id of the tehdit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tehdit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tehdits/{id}")
    public ResponseEntity<Tehdit> getTehdit(@PathVariable Long id) {
        log.debug("REST request to get Tehdit : {}", id);
        Optional<Tehdit> tehdit = tehditService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tehdit);
    }

    /**
     * {@code DELETE  /tehdits/:id} : delete the "id" tehdit.
     *
     * @param id the id of the tehdit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tehdits/{id}")
    public ResponseEntity<Void> deleteTehdit(@PathVariable Long id) {
        log.debug("REST request to delete Tehdit : {}", id);
        tehditService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
