package com.truevocation.service;

import com.truevocation.service.dto.CommentAnswerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.truevocation.domain.CommentAnswer}.
 */
public interface CommentAnswerService {
    /**
     * Save a commentAnswer.
     *
     * @param commentAnswerDTO the entity to save.
     * @return the persisted entity.
     */
    CommentAnswerDTO save(CommentAnswerDTO commentAnswerDTO);

    /**
     * Partially updates a commentAnswer.
     *
     * @param commentAnswerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CommentAnswerDTO> partialUpdate(CommentAnswerDTO commentAnswerDTO);

    /**
     * Get all the commentAnswers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CommentAnswerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" commentAnswer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CommentAnswerDTO> findOne(Long id);

    /**
     * Delete the "id" commentAnswer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
