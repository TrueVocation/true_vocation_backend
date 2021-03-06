package com.truevocation.service;

import com.truevocation.service.dto.LikesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 * Service Interface for managing {@link com.truevocation.domain.Likes}.
 */
public interface LikesService {
    /**
     * Save a likes.
     *
     * @param likesDTO the entity to save.
     * @return the persisted entity.
     */
    LikesDTO save(LikesDTO likesDTO);

    /**
     * Partially updates a likes.
     *
     * @param likesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LikesDTO> partialUpdate(LikesDTO likesDTO);

    /**
     * Get all the likes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LikesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" likes.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LikesDTO> findOne(Long id);

    /**
     * Delete the "id" likes.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    int getPostLikesCount(Long postId);

    boolean isLiked(Long postId, Long userId);

    ResponseEntity<Void> setPostLike(Long appUserId, Long postId);

}
