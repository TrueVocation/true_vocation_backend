package com.truevocation.service;

import com.truevocation.service.dto.AchievementDTO;

import java.io.IOException;
import java.util.Optional;

import com.truevocation.service.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link com.truevocation.domain.Achievement}.
 */
public interface AchievementService {
    /**
     * Save a achievement.
     *
     * @param achievementDTO the entity to save.
     * @return the persisted entity.
     */
    AchievementDTO save(AchievementDTO achievementDTO);

    /**
     * Partially updates a achievement.
     *
     * @param achievementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AchievementDTO> partialUpdate(AchievementDTO achievementDTO);

    /**
     * Get all the achievements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AchievementDTO> findAll(Pageable pageable);

    /**
     * Get the "id" achievement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AchievementDTO> findOne(Long id);

    /**
     * Delete the "id" achievement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


    AchievementDTO uploadPicture(MultipartFile file, Long achievementId);

    ResponseEntity<byte[]> getPicture(String url) throws IOException;
}
