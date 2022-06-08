package com.truevocation.service.impl;

import com.truevocation.domain.Favorite;
import com.truevocation.domain.Likes;
import com.truevocation.repository.FavoriteRepository;
import com.truevocation.service.AppUserService;
import com.truevocation.service.FavoriteService;
import com.truevocation.service.PostService;
import com.truevocation.service.dto.*;
import com.truevocation.service.mapper.*;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

/**
 * Service Implementation for managing {@link Favorite}.
 */
@Service
@Transactional
public class FavoriteServiceImpl implements FavoriteService {

    private final Logger log = LoggerFactory.getLogger(FavoriteServiceImpl.class);

    private final FavoriteRepository favoriteRepository;

    private final FavoriteMapper favoriteMapper;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PostService postService;

    @Autowired
    private UniversityMapper universityMapper;

    @Autowired
    private SpecialtyMapper specialtyMapper;

    @Autowired
    private ProfessionMapper professionMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private AppUserMapper appUserMapper;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, FavoriteMapper favoriteMapper) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteMapper = favoriteMapper;
    }

    @Override
    public FavoriteDTO save(FavoriteDTO favoriteDTO) {
        log.debug("Request to save Favorite : {}", favoriteDTO);
        Favorite favorite = favoriteMapper.toEntity(favoriteDTO);
        favorite = favoriteRepository.save(favorite);
        return favoriteMapper.toDto(favorite);
    }

    @Override
    public Optional<FavoriteDTO> partialUpdate(FavoriteDTO favoriteDTO) {
        log.debug("Request to partially update Favorite : {}", favoriteDTO);

        return favoriteRepository
            .findById(favoriteDTO.getId())
            .map(existingFavorite -> {
                favoriteMapper.partialUpdate(existingFavorite, favoriteDTO);

                return existingFavorite;
            })
            .map(favoriteRepository::save)
            .map(favoriteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FavoriteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Favorites");
        return favoriteRepository.findAll(pageable).map(favoriteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UniversityDTO> findAllFavoritesUniversitiesByUserId(Pageable pageable, Long id) {
        log.debug("Request to get all Favorites");
        return favoriteRepository.findAllFavoriteUniversityByUserId(pageable, id).map(universityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecialtyDTO> findAllFavoriteSpecialtyByUserId(Pageable pageable, Long id) {
        log.debug("Request to get all Favorites");
        return favoriteRepository.findAllFavoriteSpecialtyByUserId(pageable, id).map(specialtyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProfessionDTO> findAllFavoriteProfessionByUserId(Pageable pageable, Long id) {
        log.debug("Request to get all Favorites");
        return favoriteRepository.findAllFavoriteProfessionByUserId(pageable, id).map(professionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostDTO> findAllFavoritePostByUserId(Pageable pageable, Long id) {
        log.debug("Request to get all Favorites");
        return favoriteRepository.findAllFavoritePostByUserId(pageable, id).map(postMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FavoriteDTO> findOne(Long id) {
        log.debug("Request to get Favorite : {}", id);
        return favoriteRepository.findById(id).map(favoriteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Favorite : {}", id);
        favoriteRepository.deleteById(id);
    }

    @Override
    public boolean isFavorite(Long postId, Long userId) {
        return favoriteRepository.isFavorite(postId, userId);
    }

    @Override
    public boolean isFavoriteUniversity(Long universityId, Long userId) {
        return favoriteRepository.isFavoriteUniversity(universityId, userId);
    }

    @Override
    public boolean isFavoriteSpeciality(Long specialtyId, Long userId) {
        return favoriteRepository.isFavoriteUniversity(specialtyId, userId);
    }

    @Override
    public boolean isFavoriteProfession(Long professionId, Long userId) {
        return favoriteRepository.isFavoriteUniversity(professionId, userId);
    }

    @Override
    public void deleteFavoriteProfession(Long professionId, Long userId) {
        favoriteRepository.deleteFavoriteProfession(professionId, userId);
    }

    @Override
    public void deleteFavoriteSpeciality(Long specialtyId, Long userId) {
        favoriteRepository.deleteFavoriteSpeciality(specialtyId, userId);
    }

    @Override
    public void deleteFavoriteUniversity(Long universityId, Long userId) {
        favoriteRepository.deleteFavoriteUniversity(universityId, userId);
    }

    @Override
    public ResponseEntity<Void> setPostFavorite(Long userId, Long postId) {
        AppUserDTO appUser = appUserService.findByUserId(userId).orElse(null);
        if(Objects.isNull(appUser)){
            throw new EntityNotFoundException("App user not found");
        }
        PostDTO postDTO = postService.findOne(postId).orElse(null);
        if(Objects.isNull(postDTO)){
            throw new EntityNotFoundException("Post not found");
        }
        Favorite favorite = favoriteRepository.findByUserIdAndPostId(userId, postId);
        if(!Objects.isNull(favorite)){
            favoriteRepository.delete(favorite);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            favorite = new Favorite();
            favorite.setPost(postMapper.toEntity(postDTO));
            favorite.setUser(appUserMapper.toEntity(appUser));
            favoriteRepository.save(favorite);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}
