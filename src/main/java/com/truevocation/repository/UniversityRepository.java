package com.truevocation.repository;

import com.truevocation.domain.University;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the University entity.
 */
@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    @Query(
        value = "select distinct university from University university left join fetch university.faculties",
        countQuery = "select count(distinct university) from University university"
    )
    Page<University> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct university from University university left join fetch university.faculties")
    List<University> findAllWithEagerRelationships();

    @Query("select university from University university left join fetch university.faculties where university.id =:id")
    Optional<University> findOneWithEagerRelationships(@Param("id") Long id);
}
