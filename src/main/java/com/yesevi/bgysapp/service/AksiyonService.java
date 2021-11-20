package com.yesevi.bgysapp.service;

import com.yesevi.bgysapp.domain.Aksiyon;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Aksiyon}.
 */
public interface AksiyonService {
    /**
     * Save a aksiyon.
     *
     * @param aksiyon the entity to save.
     * @return the persisted entity.
     */
    Aksiyon save(Aksiyon aksiyon);

    /**
     * Partially updates a aksiyon.
     *
     * @param aksiyon the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Aksiyon> partialUpdate(Aksiyon aksiyon);

    /**
     * Get all the aksiyons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Aksiyon> findAll(Pageable pageable);

    /**
     * Get the "id" aksiyon.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Aksiyon> findOne(Long id);

    /**
     * Delete the "id" aksiyon.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
