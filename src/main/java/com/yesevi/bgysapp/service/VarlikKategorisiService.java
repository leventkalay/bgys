package com.yesevi.bgysapp.service;

import com.yesevi.bgysapp.domain.VarlikKategorisi;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link VarlikKategorisi}.
 */
public interface VarlikKategorisiService {
    /**
     * Save a varlikKategorisi.
     *
     * @param varlikKategorisi the entity to save.
     * @return the persisted entity.
     */
    VarlikKategorisi save(VarlikKategorisi varlikKategorisi);

    /**
     * Partially updates a varlikKategorisi.
     *
     * @param varlikKategorisi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VarlikKategorisi> partialUpdate(VarlikKategorisi varlikKategorisi);

    /**
     * Get all the varlikKategorisis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VarlikKategorisi> findAll(Pageable pageable);

    /**
     * Get the "id" varlikKategorisi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VarlikKategorisi> findOne(Long id);

    /**
     * Delete the "id" varlikKategorisi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
