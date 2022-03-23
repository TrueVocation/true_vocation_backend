package com.truevocation.repository;

import com.truevocation.domain.Specialty;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Specialty entity.
 */
@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    @Query(
        value = "select distinct specialty from Specialty specialty left join fetch specialty.subjects left join fetch specialty.professions",
        countQuery = "select count(distinct specialty) from Specialty specialty"
    )
    Page<Specialty> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct specialty from Specialty specialty left join fetch specialty.subjects left join fetch specialty.professions")
    List<Specialty> findAllWithEagerRelationships();

    @Query(
        "select specialty from Specialty specialty left join fetch specialty.subjects left join fetch specialty.professions where specialty.id =:id"
    )
    Optional<Specialty> findOneWithEagerRelationships(@Param("id") Long id);
}
