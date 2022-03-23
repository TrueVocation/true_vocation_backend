package com.truevocation.service;

import com.truevocation.service.dto.RecommendationDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.truevocation.domain.Recommendation}.
 */
public interface RecommendationService {
    /**
     * Save a recommendation.
     *
     * @param recommendationDTO the entity to save.
     * @return the persisted entity.
     */
    RecommendationDTO save(RecommendationDTO recommendationDTO);

    /**
     * Partially updates a recommendation.
     *
     * @param recommendationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RecommendationDTO> partialUpdate(RecommendationDTO recommendationDTO);

    /**
     * Get all the recommendations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RecommendationDTO> findAll(Pageable pageable);
    /**
     * Get all the RecommendationDTO where TestResult is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<RecommendationDTO> findAllWhereTestResultIsNull();

    /**
     * Get the "id" recommendation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RecommendationDTO> findOne(Long id);

    /**
     * Delete the "id" recommendation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
