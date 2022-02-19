package com.truevocation.service;

import com.truevocation.service.dto.ProfessionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.truevocation.domain.Profession}.
 */
public interface ProfessionService {
    /**
     * Save a profession.
     *
     * @param professionDTO the entity to save.
     * @return the persisted entity.
     */
    ProfessionDTO save(ProfessionDTO professionDTO);

    /**
     * Partially updates a profession.
     *
     * @param professionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProfessionDTO> partialUpdate(ProfessionDTO professionDTO);

    /**
     * Get all the professions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProfessionDTO> findAll(Pageable pageable);

    /**
     * Get all the professions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProfessionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" profession.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProfessionDTO> findOne(Long id);

    /**
     * Delete the "id" profession.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
