package com.yesevi.bgysapp.repository;

import com.yesevi.bgysapp.domain.VarlikKategorisi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the VarlikKategorisi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VarlikKategorisiRepository extends JpaRepository<VarlikKategorisi, Long> {}
