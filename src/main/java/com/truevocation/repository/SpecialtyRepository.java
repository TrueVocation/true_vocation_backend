package com.truevocation.repository;

import com.truevocation.domain.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    Optional<List<Specialty>> findAllByFaculty_Id(Long id);

    @Query(
        value = "SELECT * FROM specialty SP\n" +
            "INNER JOIN faculty f ON f.id = SP.faculty_id\n" +
            "WHERE f.id IN (\n" +
            "    SELECT id FROM faculty\n" +
            "              INNER JOIN rel_university__faculty ruf ON faculty.id = ruf.faculty_id\n" +
            "              WHERE university_id = :id\n" +
            "    )", nativeQuery = true,
        countQuery = "select count(distinct specialty) from Specialty specialty"
    )
    Page<Specialty> findAllByUniversity(Pageable pageable,@Param("id")  Long id);

    @Query(
        value = "SELECT * FROM specialty S\n" +
            "INNER JOIN rel_specialty__profession RSP ON S.id = RSP.specialty_id\n" +
            "WHERE RSP.profession_id IN (\n" +
            "    SELECT profession.id FROM profession\n" +
            "    WHERE id = :id\n" +
            ")",
        nativeQuery = true,
        countQuery = "select count(distinct specialty) from Specialty specialty"
    )
    Page<Specialty> findAllByProfession(Pageable pageable,@Param("id")  Long id);
}
