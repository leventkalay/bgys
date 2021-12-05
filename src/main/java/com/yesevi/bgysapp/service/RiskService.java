package com.yesevi.bgysapp.service;

import com.yesevi.bgysapp.domain.Risk;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing {@link Risk}.
 */
public interface RiskService {
    /**
     * Save a risk.
     *
     * @param risk the entity to save.
     * @return the persisted entity.
     */
    Risk save(Risk risk);

    /**
     * Partially updates a risk.
     *
     * @param risk the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Risk> partialUpdate(Risk risk);

    /**
     * Get all the risks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Risk> findAll(Pageable pageable);

    Page<Risk> findByOnayDurumu(Pageable pageable);

    /**
     * Get all the risks with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Risk> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" risk.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Risk> findOne(Long id);

    /**
     * Delete the "id" risk.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
