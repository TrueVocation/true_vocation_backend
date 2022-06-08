package com.truevocation.service;

import com.truevocation.service.dto.QuestionDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.truevocation.domain.Question}.
 */
public interface QuestionService {
    /**
     * Save a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionDTO save(QuestionDTO questionDTO);

    /**
     * Partially updates a question.
     *
     * @param questionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuestionDTO> partialUpdate(QuestionDTO questionDTO);

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuestionDTO> findAll(Pageable pageable);

    List<QuestionDTO> findAllByProfTestId(Long id);
    /**
     * Get all the QuestionDTO where AnswerUser is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<QuestionDTO> findAllWhereAnswerUserIsNull();

    /**
     * Get all the questions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuestionDTO> findAllWithEagerRelationships(Pageable pageable);

    Page<QuestionDTO> findAllWithEagerRelationships(Pageable pageable, Long testId);

    int findAllCountByTestId(Long testId);

    /**
     * Get the "id" question.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionDTO> findOne(Long id);

    /**
     * Delete the "id" question.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
