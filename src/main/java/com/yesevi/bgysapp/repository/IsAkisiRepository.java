package com.yesevi.bgysapp.repository;

import com.yesevi.bgysapp.domain.IsAkisi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the IsAkisi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IsAkisiRepository extends JpaRepository<IsAkisi, Long> {}
