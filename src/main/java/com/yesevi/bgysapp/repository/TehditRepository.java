package com.yesevi.bgysapp.repository;

import com.yesevi.bgysapp.domain.Tehdit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tehdit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TehditRepository extends JpaRepository<Tehdit, Long> {}
