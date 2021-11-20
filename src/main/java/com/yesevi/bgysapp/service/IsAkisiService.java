package com.yesevi.bgysapp.service;

import com.yesevi.bgysapp.domain.IsAkisi;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link IsAkisi}.
 */
public interface IsAkisiService {
    /**
     * Save a isAkisi.
     *
     * @param isAkisi the entity to save.
     * @return the persisted entity.
     */
    IsAkisi save(IsAkisi isAkisi);

    /**
     * Partially updates a isAkisi.
     *
     * @param isAkisi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IsAkisi> partialUpdate(IsAkisi isAkisi);

    /**
     * Get all the isAkisis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IsAkisi> findAll(Pageable pageable);

    /**
     * Get the "id" isAkisi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IsAkisi> findOne(Long id);

    /**
     * Delete the "id" isAkisi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
