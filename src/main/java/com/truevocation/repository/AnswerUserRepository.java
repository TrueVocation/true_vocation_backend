package com.truevocation.repository;

import com.truevocation.domain.Answer;
import com.truevocation.domain.AnswerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the AnswerUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswerUserRepository extends JpaRepository<AnswerUser, Long> {

    List<AnswerUser> findAllByAppUserId(Long app_user_id);

    List<Answer> findAllByAppUserIdAndAndAnswerPoint(Long id, int point);

    List<Answer> findAllByAnswerPoint(int point);

    AnswerUser findAnswerUserByAppUserIdAndQuestionId(Long appUserId, Long questionId);


}
