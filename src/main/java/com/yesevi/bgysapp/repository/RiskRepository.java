package com.yesevi.bgysapp.repository;

import com.yesevi.bgysapp.domain.Risk;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Risk entity.
 */
@Repository
public interface RiskRepository extends JpaRepository<Risk, Long> {
    @Query(
        value = "select distinct risk from Risk risk left join fetch risk.varliks",
        countQuery = "select count(distinct risk) from Risk risk"
    )
    Page<Risk> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct risk from Risk risk left join fetch risk.varliks")
    List<Risk> findAllWithEagerRelationships();

    @Query("select risk from Risk risk left join fetch risk.varliks where risk.id =:id")
    Optional<Risk> findOneWithEagerRelationships(@Param("id") Long id);
}
