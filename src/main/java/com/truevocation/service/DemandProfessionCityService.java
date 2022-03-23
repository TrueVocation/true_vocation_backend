package com.truevocation.service;

import com.truevocation.service.dto.DemandProfessionCityDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.truevocation.domain.DemandProfessionCity}.
 */
public interface DemandProfessionCityService {
    /**
     * Save a demandProfessionCity.
     *
     * @param demandProfessionCityDTO the entity to save.
     * @return the persisted entity.
     */
    DemandProfessionCityDTO save(DemandProfessionCityDTO demandProfessionCityDTO);

    /**
     * Partially updates a demandProfessionCity.
     *
     * @param demandProfessionCityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DemandProfessionCityDTO> partialUpdate(DemandProfessionCityDTO demandProfessionCityDTO);

    /**
     * Get all the demandProfessionCities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DemandProfessionCityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" demandProfessionCity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DemandProfessionCityDTO> findOne(Long id);

    /**
     * Delete the "id" demandProfessionCity.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
