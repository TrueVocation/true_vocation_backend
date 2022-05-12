package com.truevocation.service;

import com.truevocation.service.dto.PicturesDTO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link com.truevocation.domain.Pictures}.
 */
public interface PicturesService {
    /**
     * Save a pictures.
     *
     * @param picturesDTO the entity to save.
     * @return the persisted entity.
     */
    PicturesDTO save(PicturesDTO picturesDTO);

    /**
     * Partially updates a pictures.
     *
     * @param picturesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PicturesDTO> partialUpdate(PicturesDTO picturesDTO);

    /**
     * Get all the pictures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PicturesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pictures.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PicturesDTO> findOne(Long id);

    /**
     * Delete the "id" pictures.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    PicturesDTO savePicture(MultipartFile file, Long sourceId, String sourceType);

    ResponseEntity<byte[]> getPictureByUrl(String url) throws IOException, URISyntaxException;
}
