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
    Page<University> findAllBySpecialityId(Pageable pageable,@Param("id") Long id);

    @Query("select distinct university from University university left join fetch university.faculties")
    List<University> findAllWithEagerRelationships();

    @Query("select university from University university left join fetch university.faculties where university.id =:id")
    Optional<University> findOneWithEagerRelationships(@Param("id") Long id);

    University findByLogoEquals(String logoUrl);
}
