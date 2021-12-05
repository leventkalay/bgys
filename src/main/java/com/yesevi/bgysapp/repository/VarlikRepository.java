package com.yesevi.bgysapp.repository;

import com.yesevi.bgysapp.domain.Varlik;
import com.yesevi.bgysapp.domain.enumeration.Onay;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Varlik entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VarlikRepository extends JpaRepository<Varlik, Long> {
    Page<Varlik> findByOnayDurumu(Onay onayDurumu, Pageable pageable);
}
