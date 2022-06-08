package com.truevocation.repository;

import com.truevocation.domain.Portfolio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Portfolio entity.
 */
@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    @Query(
        value = "select distinct portfolio from Portfolio portfolio left join fetch portfolio.languages left join fetch portfolio.schools",
        countQuery = "select count(distinct portfolio) from Portfolio portfolio"
    )
    Page<Portfolio> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct portfolio from Portfolio portfolio left join fetch portfolio.languages left join fetch portfolio.schools")
    List<Portfolio> findAllWithEagerRelationships();

    @Query(
        "select portfolio from Portfolio portfolio left join fetch portfolio.languages left join fetch portfolio.schools where portfolio.id =:id"
    )
    Optional<Portfolio> findOneWithEagerRelationships(@Param("id") Long id);

    @Query(value = "delete from Portfolio p where p.appUser.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);


    @Query(
        "select portfolio from Portfolio portfolio left join fetch portfolio.languages left join fetch portfolio.schools where portfolio.appUser.user.id =:id"
    )
    Optional<Portfolio> findOneWithEagerRelationshipsByUser(@Param("id") Long id);
}
