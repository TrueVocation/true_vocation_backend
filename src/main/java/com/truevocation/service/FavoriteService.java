package com.truevocation.service;

import com.truevocation.service.dto.*;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 * Service Interface for managing {@link com.truevocation.domain.Favorite}.
 */
public interface FavoriteService {
    /**
     * Save a favorite.
     *
     * @param favoriteDTO the entity to save.
     * @return the persisted entity.
     */
    FavoriteDTO save(FavoriteDTO favoriteDTO);

    /**
     * Partially updates a favorite.
     *
     * @param favoriteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FavoriteDTO> partialUpdate(FavoriteDTO favoriteDTO);

    /**
     * Get all the favorites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FavoriteDTO> findAll(Pageable pageable);

    Page<UniversityDTO> findAllFavoritesUniversitiesByUserId(Pageable pageable, Long id);

    Page<SpecialtyDTO> findAllFavoriteSpecialtyByUserId(Pageable pageable, Long id);

    Page<ProfessionDTO> findAllFavoriteProfessionByUserId(Pageable pageable, Long id);

    Page<PostDTO> findAllFavoritePostByUserId(Pageable pageable, Long id);

    /**
     * Get the "id" favorite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FavoriteDTO> findOne(Long id);

    /**
     * Delete the "id" favorite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    boolean isFavorite(Long postId, Long userId);

    boolean isFavoriteUniversity(Long universityId, Long userId);
    boolean isFavoriteSpeciality(Long specialtyId, Long userId);
    boolean isFavoriteProfession(Long professionId, Long userId);
    void deleteFavoriteProfession(Long professionId, Long userId);
    void deleteFavoriteSpeciality(Long specialtyId, Long userId);
    void deleteFavoriteUniversity(Long universityId, Long userId);

    ResponseEntity<Void> setPostFavorite(Long userId, Long postId);
}
