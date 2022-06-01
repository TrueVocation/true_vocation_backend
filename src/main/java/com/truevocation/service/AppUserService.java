package com.truevocation.service;

import com.truevocation.service.dto.AppUserDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.truevocation.domain.AppUser}.
 */
public interface AppUserService {
    /**
     * Save a appUser.
     *
     * @param appUserDTO the entity to save.
     * @return the persisted entity.
     */
    AppUserDTO save(AppUserDTO appUserDTO);

    /**
     * Partially updates a appUser.
     *
     * @param appUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppUserDTO> partialUpdate(AppUserDTO appUserDTO);

    /**
     * Get all the appUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AppUserDTO> findAll(Pageable pageable);
    /**
     * Get all the AppUserDTO where Portfolio is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<AppUserDTO> findAllWherePortfolioIsNull();
    /**
     * Get all the AppUserDTO where TestResult is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<AppUserDTO> findAllWhereTestResultIsNull();

    /**
     * Get the "id" appUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppUserDTO> findOne(Long id);
    Optional<AppUserDTO> findByUserId(Long id);

    /**
     * Delete the "id" appUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    boolean checkUserExistence(String phoneNumber);
}
