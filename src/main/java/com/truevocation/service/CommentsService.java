package com.truevocation.service;

import com.truevocation.domain.Comments;
import com.truevocation.service.dto.CommentsDTO;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.truevocation.domain.Comments}.
 */
public interface CommentsService {
    /**
     * Save a comments.
     *
     * @param commentsDTO the entity to save.
     * @return the persisted entity.
     */
    CommentsDTO save(CommentsDTO commentsDTO);

    /**
     * Partially updates a comments.
     *
     * @param commentsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CommentsDTO> partialUpdate(CommentsDTO commentsDTO);

    /**
     * Get all the comments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CommentsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" comments.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CommentsDTO> findOne(Long id);

    /**
     * Delete the "id" comments.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    int getPostCommentsCount(Long postId);

    List<Comments> getPostComments(Long postID);
}
