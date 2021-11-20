package com.yesevi.bgysapp.repository;

import com.yesevi.bgysapp.domain.Surec;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Surec entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SurecRepository extends JpaRepository<Surec, Long> {}
