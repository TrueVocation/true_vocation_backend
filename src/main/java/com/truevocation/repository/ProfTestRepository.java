package com.truevocation.repository;

import com.truevocation.domain.ProfTest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProfTest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfTestRepository extends JpaRepository<ProfTest, Long> {}
