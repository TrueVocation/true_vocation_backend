package com.truevocation.repository;

import com.truevocation.domain.Profession;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Profession entity.
 */
@Repository
public interface ProfessionRepository extends JpaRepository<Profession, Long> {
    @Query(
        value = "select distinct profession from Profession profession left join fetch profession.courses",
        countQuery = "select count(distinct profession) from Profession profession"
    )
    Page<Profession> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct profession from Profession profession left join fetch profession.courses")
    List<Profession> findAllWithEagerRelationships();

    @Query("select profession from Profession profession left join fetch profession.courses where profession.id =:id")
    Optional<Profession> findOneWithEagerRelationships(@Param("id") Long id);

    @Query(
        value = "SELECT * FROM profession P\n" +
            "INNER JOIN rel_specialty__profession RSP on P.id = RSP.profession_id\n" +
            "WHERE RSP.specialty_id in (\n" +
            "    select specialty.id from specialty\n" +
            "    where id = :id\n" +
            ")",
        nativeQuery = true,
        countQuery = "select count(distinct profession) from Profession profession"
    )
    Page<Profession> findAllBySpecialties(Pageable pageable, @Param("id") Long id);
}
