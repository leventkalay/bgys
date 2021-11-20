package com.yesevi.bgysapp.repository;

import com.yesevi.bgysapp.domain.TehditKategorisi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TehditKategorisi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TehditKategorisiRepository extends JpaRepository<TehditKategorisi, Long> {}
