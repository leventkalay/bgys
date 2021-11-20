package com.yesevi.bgysapp.repository;

import com.yesevi.bgysapp.domain.Personel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Personel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonelRepository extends JpaRepository<Personel, Long> {}
