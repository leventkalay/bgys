package com.yesevi.bgysapp.web.rest;

import com.yesevi.bgysapp.domain.VarlikKategorisi;
import com.yesevi.bgysapp.repository.VarlikKategorisiRepository;
import com.yesevi.bgysapp.service.VarlikKategorisiService;
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
 * REST controller for managing {@link com.yesevi.bgysapp.domain.VarlikKategorisi}.
 */
@RestController
@RequestMapping("/api")
public class VarlikKategorisiResource {

    private final Logger log = LoggerFactory.getLogger(VarlikKategorisiResource.class);

    private static final String ENTITY_NAME = "varlikKategorisi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VarlikKategorisiService varlikKategorisiService;

    private final VarlikKategorisiRepository varlikKategorisiRepository;

    public VarlikKategorisiResource(
        VarlikKategorisiService varlikKategorisiService,
        VarlikKategorisiRepository varlikKategorisiRepository
    ) {
        this.varlikKategorisiService = varlikKategorisiService;
        this.varlikKategorisiRepository = varlikKategorisiRepository;
    }

    /**
     * {@code POST  /varlik-kategorisis} : Create a new varlikKategorisi.
     *
     * @param varlikKategorisi the varlikKategorisi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new varlikKategorisi, or with status {@code 400 (Bad Request)} if the varlikKategorisi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/varlik-kategorisis")
    public ResponseEntity<VarlikKategorisi> createVarlikKategorisi(@RequestBody VarlikKategorisi varlikKategorisi)
        throws URISyntaxException {
        log.debug("REST request to save VarlikKategorisi : {}", varlikKategorisi);
        if (varlikKategorisi.getId() != null) {
            throw new BadRequestAlertException("A new varlikKategorisi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VarlikKategorisi result = varlikKategorisiService.save(varlikKategorisi);
        return ResponseEntity
            .created(new URI("/api/varlik-kategorisis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /varlik-kategorisis/:id} : Updates an existing varlikKategorisi.
     *
     * @param id the id of the varlikKategorisi to save.
     * @param varlikKategorisi the varlikKategorisi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated varlikKategorisi,
     * or with status {@code 400 (Bad Request)} if the varlikKategorisi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the varlikKategorisi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/varlik-kategorisis/{id}")
    public ResponseEntity<VarlikKategorisi> updateVarlikKategorisi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VarlikKategorisi varlikKategorisi
    ) throws URISyntaxException {
        log.debug("REST request to update VarlikKategorisi : {}, {}", id, varlikKategorisi);
        if (varlikKategorisi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, varlikKategorisi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!varlikKategorisiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VarlikKategorisi result = varlikKategorisiService.save(varlikKategorisi);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, varlikKategorisi.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /varlik-kategorisis/:id} : Partial updates given fields of an existing varlikKategorisi, field will ignore if it is null
     *
     * @param id the id of the varlikKategorisi to save.
     * @param varlikKategorisi the varlikKategorisi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated varlikKategorisi,
     * or with status {@code 400 (Bad Request)} if the varlikKategorisi is not valid,
     * or with status {@code 404 (Not Found)} if the varlikKategorisi is not found,
     * or with status {@code 500 (Internal Server Error)} if the varlikKategorisi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/varlik-kategorisis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VarlikKategorisi> partialUpdateVarlikKategorisi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VarlikKategorisi varlikKategorisi
    ) throws URISyntaxException {
        log.debug("REST request to partial update VarlikKategorisi partially : {}, {}", id, varlikKategorisi);
        if (varlikKategorisi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, varlikKategorisi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!varlikKategorisiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VarlikKategorisi> result = varlikKategorisiService.partialUpdate(varlikKategorisi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, varlikKategorisi.getId().toString())
        );
    }

    /**
     * {@code GET  /varlik-kategorisis} : get all the varlikKategorisis.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of varlikKategorisis in body.
     */
    @GetMapping("/varlik-kategorisis")
    public ResponseEntity<List<VarlikKategorisi>> getAllVarlikKategorisis(Pageable pageable) {
        log.debug("REST request to get a page of VarlikKategorisis");
        Page<VarlikKategorisi> page = varlikKategorisiService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /varlik-kategorisis/:id} : get the "id" varlikKategorisi.
     *
     * @param id the id of the varlikKategorisi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the varlikKategorisi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/varlik-kategorisis/{id}")
    public ResponseEntity<VarlikKategorisi> getVarlikKategorisi(@PathVariable Long id) {
        log.debug("REST request to get VarlikKategorisi : {}", id);
        Optional<VarlikKategorisi> varlikKategorisi = varlikKategorisiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(varlikKategorisi);
    }

    /**
     * {@code DELETE  /varlik-kategorisis/:id} : delete the "id" varlikKategorisi.
     *
     * @param id the id of the varlikKategorisi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/varlik-kategorisis/{id}")
    public ResponseEntity<Void> deleteVarlikKategorisi(@PathVariable Long id) {
        log.debug("REST request to delete VarlikKategorisi : {}", id);
        varlikKategorisiService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
