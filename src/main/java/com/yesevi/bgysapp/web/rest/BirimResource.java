package com.yesevi.bgysapp.web.rest;

import com.yesevi.bgysapp.domain.Birim;
import com.yesevi.bgysapp.repository.BirimRepository;
import com.yesevi.bgysapp.service.BirimService;
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
 * REST controller for managing {@link com.yesevi.bgysapp.domain.Birim}.
 */
@RestController
@RequestMapping("/api")
public class BirimResource {

    private final Logger log = LoggerFactory.getLogger(BirimResource.class);

    private static final String ENTITY_NAME = "birim";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BirimService birimService;

    private final BirimRepository birimRepository;

    public BirimResource(BirimService birimService, BirimRepository birimRepository) {
        this.birimService = birimService;
        this.birimRepository = birimRepository;
    }

    /**
     * {@code POST  /birims} : Create a new birim.
     *
     * @param birim the birim to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new birim, or with status {@code 400 (Bad Request)} if the birim has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/birims")
    public ResponseEntity<Birim> createBirim(@Valid @RequestBody Birim birim) throws URISyntaxException {
        log.debug("REST request to save Birim : {}", birim);
        if (birim.getId() != null) {
            throw new BadRequestAlertException("A new birim cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Birim result = birimService.save(birim);
        return ResponseEntity
            .created(new URI("/api/birims/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /birims/:id} : Updates an existing birim.
     *
     * @param id the id of the birim to save.
     * @param birim the birim to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated birim,
     * or with status {@code 400 (Bad Request)} if the birim is not valid,
     * or with status {@code 500 (Internal Server Error)} if the birim couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/birims/{id}")
    public ResponseEntity<Birim> updateBirim(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Birim birim)
        throws URISyntaxException {
        log.debug("REST request to update Birim : {}, {}", id, birim);
        if (birim.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, birim.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!birimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Birim result = birimService.save(birim);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, birim.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /birims/:id} : Partial updates given fields of an existing birim, field will ignore if it is null
     *
     * @param id the id of the birim to save.
     * @param birim the birim to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated birim,
     * or with status {@code 400 (Bad Request)} if the birim is not valid,
     * or with status {@code 404 (Not Found)} if the birim is not found,
     * or with status {@code 500 (Internal Server Error)} if the birim couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/birims/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Birim> partialUpdateBirim(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Birim birim
    ) throws URISyntaxException {
        log.debug("REST request to partial update Birim partially : {}, {}", id, birim);
        if (birim.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, birim.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!birimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Birim> result = birimService.partialUpdate(birim);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, birim.getId().toString())
        );
    }

    /**
     * {@code GET  /birims} : get all the birims.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of birims in body.
     */
    @GetMapping("/birims")
    public ResponseEntity<List<Birim>> getAllBirims(Pageable pageable) {
        log.debug("REST request to get a page of Birims");
        Page<Birim> page = birimService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /birims/:id} : get the "id" birim.
     *
     * @param id the id of the birim to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the birim, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/birims/{id}")
    public ResponseEntity<Birim> getBirim(@PathVariable Long id) {
        log.debug("REST request to get Birim : {}", id);
        Optional<Birim> birim = birimService.findOne(id);
        return ResponseUtil.wrapOrNotFound(birim);
    }

    /**
     * {@code DELETE  /birims/:id} : delete the "id" birim.
     *
     * @param id the id of the birim to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/birims/{id}")
    public ResponseEntity<Void> deleteBirim(@PathVariable Long id) {
        log.debug("REST request to delete Birim : {}", id);
        birimService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
