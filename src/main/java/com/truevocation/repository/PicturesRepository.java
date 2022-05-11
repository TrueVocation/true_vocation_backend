package com.truevocation.repository;

import com.truevocation.domain.Pictures;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Pictures entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PicturesRepository extends JpaRepository<Pictures, Long> {}
