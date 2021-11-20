package com.yesevi.bgysapp.repository;

import com.yesevi.bgysapp.domain.Aksiyon;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Aksiyon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AksiyonRepository extends JpaRepository<Aksiyon, Long> {}
