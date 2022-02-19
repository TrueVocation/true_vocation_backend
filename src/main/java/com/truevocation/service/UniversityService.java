package com.truevocation.service;

import com.truevocation.service.dto.UniversityDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.truevocation.domain.University}.
 */
public interface UniversityService {
    /**
     * Save a university.
     *
     * @param universityDTO the entity to save.
     * @return the persisted entity.
     */
    UniversityDTO save(UniversityDTO universityDTO);

    /**
     * Partially updates a university.
     *
     * @param universityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UniversityDTO> partialUpdate(UniversityDTO universityDTO);

    /**
     * Get all the universities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UniversityDTO> findAll(Pageable pageable);

    /**
     * Get all the universities with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UniversityDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" university.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UniversityDTO> findOne(Long id);

    /**
     * Delete the "id" university.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
