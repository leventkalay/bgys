package com.yesevi.bgysapp.service;

import com.yesevi.bgysapp.domain.Varlik;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing {@link Varlik}.
 */
public interface VarlikService {
    /**
     * Save a varlik.
     *
     * @param varlik the entity to save.
     * @return the persisted entity.
     */
    Varlik save(Varlik varlik);

    /**
     * Partially updates a varlik.
     *
     * @param varlik the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Varlik> partialUpdate(Varlik varlik);

    /**
     * Get all the varliks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Varlik> findAll(Pageable pageable);

    Page<Varlik> findByOnayDurumu(Pageable pageable);

    /**
     * Get the "id" varlik.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Varlik> findOne(Long id);

    /**
     * Delete the "id" varlik.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
