package com.yesevi.bgysapp.repository;

import com.yesevi.bgysapp.domain.Birim;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Birim entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BirimRepository extends JpaRepository<Birim, Long> {}
