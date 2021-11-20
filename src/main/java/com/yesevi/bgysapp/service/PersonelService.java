package com.yesevi.bgysapp.service;

import com.yesevi.bgysapp.domain.Personel;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Personel}.
 */
public interface PersonelService {
    /**
     * Save a personel.
     *
     * @param personel the entity to save.
     * @return the persisted entity.
     */
    Personel save(Personel personel);

    /**
     * Partially updates a personel.
     *
     * @param personel the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Personel> partialUpdate(Personel personel);

    /**
     * Get all the personels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Personel> findAll(Pageable pageable);

    /**
     * Get the "id" personel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Personel> findOne(Long id);

    /**
     * Delete the "id" personel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
