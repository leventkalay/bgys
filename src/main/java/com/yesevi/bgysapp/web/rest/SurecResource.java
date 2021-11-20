package com.yesevi.bgysapp.web.rest;

import com.yesevi.bgysapp.domain.Surec;
import com.yesevi.bgysapp.repository.SurecRepository;
import com.yesevi.bgysapp.service.SurecService;
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
 * REST controller for managing {@link com.yesevi.bgysapp.domain.Surec}.
 */
@RestController
@RequestMapping("/api")
public class SurecResource {

    private final Logger log = LoggerFactory.getLogger(SurecResource.class);

    private static final String ENTITY_NAME = "surec";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SurecService surecService;

    private final SurecRepository surecRepository;

    public SurecResource(SurecService surecService, SurecRepository surecRepository) {
        this.surecService = surecService;
        this.surecRepository = surecRepository;
    }

    /**
     * {@code POST  /surecs} : Create a new surec.
     *
     * @param surec the surec to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new surec, or with status {@code 400 (Bad Request)} if the surec has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/surecs")
    public ResponseEntity<Surec> createSurec(@RequestBody Surec surec) throws URISyntaxException {
        log.debug("REST request to save Surec : {}", surec);
        if (surec.getId() != null) {
            throw new BadRequestAlertException("A new surec cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Surec result = surecService.save(surec);
        return ResponseEntity
            .created(new URI("/api/surecs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /surecs/:id} : Updates an existing surec.
     *
     * @param id the id of the surec to save.
     * @param surec the surec to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surec,
     * or with status {@code 400 (Bad Request)} if the surec is not valid,
     * or with status {@code 500 (Internal Server Error)} if the surec couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/surecs/{id}")
    public ResponseEntity<Surec> updateSurec(@PathVariable(value = "id", required = false) final Long id, @RequestBody Surec surec)
        throws URISyntaxException {
        log.debug("REST request to update Surec : {}, {}", id, surec);
        if (surec.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surec.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surecRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Surec result = surecService.save(surec);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, surec.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /surecs/:id} : Partial updates given fields of an existing surec, field will ignore if it is null
     *
     * @param id the id of the surec to save.
     * @param surec the surec to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surec,
     * or with status {@code 400 (Bad Request)} if the surec is not valid,
     * or with status {@code 404 (Not Found)} if the surec is not found,
     * or with status {@code 500 (Internal Server Error)} if the surec couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/surecs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Surec> partialUpdateSurec(@PathVariable(value = "id", required = false) final Long id, @RequestBody Surec surec)
        throws URISyntaxException {
        log.debug("REST request to partial update Surec partially : {}, {}", id, surec);
        if (surec.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surec.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surecRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Surec> result = surecService.partialUpdate(surec);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, surec.getId().toString())
        );
    }

    /**
     * {@code GET  /surecs} : get all the surecs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of surecs in body.
     */
    @GetMapping("/surecs")
    public ResponseEntity<List<Surec>> getAllSurecs(Pageable pageable) {
        log.debug("REST request to get a page of Surecs");
        Page<Surec> page = surecService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /surecs/:id} : get the "id" surec.
     *
     * @param id the id of the surec to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the surec, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/surecs/{id}")
    public ResponseEntity<Surec> getSurec(@PathVariable Long id) {
        log.debug("REST request to get Surec : {}", id);
        Optional<Surec> surec = surecService.findOne(id);
        return ResponseUtil.wrapOrNotFound(surec);
    }

    /**
     * {@code DELETE  /surecs/:id} : delete the "id" surec.
     *
     * @param id the id of the surec to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/surecs/{id}")
    public ResponseEntity<Void> deleteSurec(@PathVariable Long id) {
        log.debug("REST request to delete Surec : {}", id);
        surecService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
