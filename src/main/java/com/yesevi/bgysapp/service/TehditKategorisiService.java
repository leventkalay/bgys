package com.yesevi.bgysapp.service;

import com.yesevi.bgysapp.domain.TehditKategorisi;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TehditKategorisi}.
 */
public interface TehditKategorisiService {
    /**
     * Save a tehditKategorisi.
     *
     * @param tehditKategorisi the entity to save.
     * @return the persisted entity.
     */
    TehditKategorisi save(TehditKategorisi tehditKategorisi);

    /**
     * Partially updates a tehditKategorisi.
     *
     * @param tehditKategorisi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TehditKategorisi> partialUpdate(TehditKategorisi tehditKategorisi);

    /**
     * Get all the tehditKategorisis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TehditKategorisi> findAll(Pageable pageable);

    /**
     * Get the "id" tehditKategorisi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TehditKategorisi> findOne(Long id);

    /**
     * Delete the "id" tehditKategorisi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
