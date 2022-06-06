package com.truevocation.repository;

import com.truevocation.domain.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Query(
        value =
            "SELECT * FROM university U\n" +
                "INNER JOIN rel_university__faculty RUF on U.id = RUF.university_id\n" +
                "WHERE RUF.faculty_id in (\n" +
                "    select faculty_id from specialty\n" +
                "    where id = :id\n" +
                ")",
        nativeQuery = true,
        countQuery = "select count(distinct university) from University university"
    )
    Page<University> findAllBySpecialityId(Pageable pageable, @Param("id") Long id);

    @Query(
        value = "SELECT COUNT(*) FROM specialty SP\n" +
            "INNER JOIN faculty f ON f.id = SP.faculty_id\n" +
            "WHERE f.id IN (\n" +
            "    SELECT id FROM faculty\n" +
            "              INNER JOIN rel_university__faculty ruf ON faculty.id = ruf.faculty_id\n" +
            "              WHERE university_id = :id\n" +
            "    )", nativeQuery = true,
        countQuery = "select count(distinct specialty) from Specialty specialty"
    )
    int countAllSpecialityByUniversity(@Param("id") Long id);

    @Query("select distinct university from University university left join fetch university.faculties")
    List<University> findAllWithEagerRelationships();

    @Query("select university from University university left join fetch university.faculties where university.id =:id")
    Optional<University> findOneWithEagerRelationships(@Param("id") Long id);

    University findByLogoEquals(String logoUrl);

    @Query(value = "select avg(s.price) from specialty s " +
        "inner join faculty f on f.id = s.faculty_id " +
        "inner join rel_university__faculty ruf on f.id = ruf.faculty_id " +
        "where ruf.university_id = :universityId", nativeQuery = true)
    Integer getAveragePrice(@Param("universityId") Long universityId);
}
