package com.yesevi.bgysapp.repository;

import com.yesevi.bgysapp.domain.Varlik;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Varlik entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VarlikRepository extends JpaRepository<Varlik, Long> {}
