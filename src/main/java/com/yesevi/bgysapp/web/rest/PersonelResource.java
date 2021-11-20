package com.yesevi.bgysapp.web.rest;

import com.yesevi.bgysapp.domain.Personel;
import com.yesevi.bgysapp.repository.PersonelRepository;
import com.yesevi.bgysapp.service.PersonelService;
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
 * REST controller for managing {@link com.yesevi.bgysapp.domain.Personel}.
 */
@RestController
@RequestMapping("/api")
public class PersonelResource {

    private final Logger log = LoggerFactory.getLogger(PersonelResource.class);

    private static final String ENTITY_NAME = "personel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonelService personelService;

    private final PersonelRepository personelRepository;

    public PersonelResource(PersonelService personelService, PersonelRepository personelRepository) {
        this.personelService = personelService;
        this.personelRepository = personelRepository;
    }

    /**
     * {@code POST  /personels} : Create a new personel.
     *
     * @param personel the personel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personel, or with status {@code 400 (Bad Request)} if the personel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/personels")
    public ResponseEntity<Personel> createPersonel(@Valid @RequestBody Personel personel) throws URISyntaxException {
        log.debug("REST request to save Personel : {}", personel);
        if (personel.getId() != null) {
            throw new BadRequestAlertException("A new personel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Personel result = personelService.save(personel);
        return ResponseEntity
            .created(new URI("/api/personels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /personels/:id} : Updates an existing personel.
     *
     * @param id the id of the personel to save.
     * @param personel the personel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personel,
     * or with status {@code 400 (Bad Request)} if the personel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/personels/{id}")
    public ResponseEntity<Personel> updatePersonel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Personel personel
    ) throws URISyntaxException {
        log.debug("REST request to update Personel : {}, {}", id, personel);
        if (personel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Personel result = personelService.save(personel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /personels/:id} : Partial updates given fields of an existing personel, field will ignore if it is null
     *
     * @param id the id of the personel to save.
     * @param personel the personel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personel,
     * or with status {@code 400 (Bad Request)} if the personel is not valid,
     * or with status {@code 404 (Not Found)} if the personel is not found,
     * or with status {@code 500 (Internal Server Error)} if the personel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/personels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Personel> partialUpdatePersonel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Personel personel
    ) throws URISyntaxException {
        log.debug("REST request to partial update Personel partially : {}, {}", id, personel);
        if (personel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Personel> result = personelService.partialUpdate(personel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personel.getId().toString())
        );
    }

    /**
     * {@code GET  /personels} : get all the personels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personels in body.
     */
    @GetMapping("/personels")
    public ResponseEntity<List<Personel>> getAllPersonels(Pageable pageable) {
        log.debug("REST request to get a page of Personels");
        Page<Personel> page = personelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /personels/:id} : get the "id" personel.
     *
     * @param id the id of the personel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/personels/{id}")
    public ResponseEntity<Personel> getPersonel(@PathVariable Long id) {
        log.debug("REST request to get Personel : {}", id);
        Optional<Personel> personel = personelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personel);
    }

    /**
     * {@code DELETE  /personels/:id} : delete the "id" personel.
     *
     * @param id the id of the personel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/personels/{id}")
    public ResponseEntity<Void> deletePersonel(@PathVariable Long id) {
        log.debug("REST request to delete Personel : {}", id);
        personelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
