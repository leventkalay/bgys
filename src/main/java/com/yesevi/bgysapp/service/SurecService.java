package com.yesevi.bgysapp.service;

import com.yesevi.bgysapp.domain.Surec;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Surec}.
 */
public interface SurecService {
    /**
     * Save a surec.
     *
     * @param surec the entity to save.
     * @return the persisted entity.
     */
    Surec save(Surec surec);

    /**
     * Partially updates a surec.
     *
     * @param surec the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Surec> partialUpdate(Surec surec);

    /**
     * Get all the surecs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Surec> findAll(Pageable pageable);

    /**
     * Get the "id" surec.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Surec> findOne(Long id);

    /**
     * Delete the "id" surec.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
