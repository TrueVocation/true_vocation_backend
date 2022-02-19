package com.truevocation.repository;

import com.truevocation.domain.AnswerUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AnswerUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswerUserRepository extends JpaRepository<AnswerUser, Long> {}
