package com.yesevi.bgysapp.service;

import com.yesevi.bgysapp.domain.Birim;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Birim}.
 */
public interface BirimService {
    /**
     * Save a birim.
     *
     * @param birim the entity to save.
     * @return the persisted entity.
     */
    Birim save(Birim birim);

    /**
     * Partially updates a birim.
     *
     * @param birim the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Birim> partialUpdate(Birim birim);

    /**
     * Get all the birims.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Birim> findAll(Pageable pageable);

    /**
     * Get the "id" birim.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Birim> findOne(Long id);

    /**
     * Delete the "id" birim.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
