package com.truevocation.repository;

import com.truevocation.domain.Question;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Question entity.
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(
        value = "select distinct question from Question question left join fetch question.answers",
        countQuery = "select count(distinct question) from Question question"
    )
    Page<Question> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        value = "select distinct question from Question question inner join question.answers where question.profTest.id =:testId",
        countQuery = "select count(distinct question) from Question question"
    )
    Page<Question> findAllWithEagerRelationships(Pageable pageable,@Param("testId") Long testId);

    @Query(
        value = "select count(distinct question) from Question question where question.profTest.id =:testId"
    )
    int findAllTestQuestionsCount(@Param("testId") Long testId);

    @Query("select distinct question from Question question left join fetch question.answers")
    List<Question> findAllWithEagerRelationships();

    List<Question> findAllByProfTestId(Long profTest_id);

    @Query("select question from Question question left join fetch question.answers where question.id =:id")
    Optional<Question> findOneWithEagerRelationships(@Param("id") Long id);
}
