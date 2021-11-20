package com.yesevi.bgysapp.service;

import com.yesevi.bgysapp.domain.Tehdit;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Tehdit}.
 */
public interface TehditService {
    /**
     * Save a tehdit.
     *
     * @param tehdit the entity to save.
     * @return the persisted entity.
     */
    Tehdit save(Tehdit tehdit);

    /**
     * Partially updates a tehdit.
     *
     * @param tehdit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Tehdit> partialUpdate(Tehdit tehdit);

    /**
     * Get all the tehdits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Tehdit> findAll(Pageable pageable);

    /**
     * Get the "id" tehdit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Tehdit> findOne(Long id);

    /**
     * Delete the "id" tehdit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
