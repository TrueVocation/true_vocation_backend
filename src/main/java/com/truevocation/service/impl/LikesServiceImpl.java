package com.truevocation.service.impl;

import com.truevocation.domain.AppUser;
import com.truevocation.domain.Likes;
import com.truevocation.domain.User;
import com.truevocation.repository.LikesRepository;
import com.truevocation.security.SecurityUtils;
import com.truevocation.service.AppUserService;
import com.truevocation.service.LikesService;
import com.truevocation.service.PostService;
import com.truevocation.service.UserService;
import com.truevocation.service.dto.AppUserDTO;
import com.truevocation.service.dto.LikesDTO;
import com.truevocation.service.dto.PostDTO;
import com.truevocation.service.mapper.AppUserMapper;
import com.truevocation.service.mapper.LikesMapper;

import java.util.Objects;
import java.util.Optional;

import com.truevocation.service.mapper.PostMapper;
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
 * Service Implementation for managing {@link Likes}.
 */
@Service
@Transactional
public class LikesServiceImpl implements LikesService {

    private final Logger log = LoggerFactory.getLogger(LikesServiceImpl.class);

    private final LikesRepository likesRepository;

    private final LikesMapper likesMapper;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PostService postService;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private AppUserMapper appUserMapper;

    public LikesServiceImpl(LikesRepository likesRepository, LikesMapper likesMapper) {
        this.likesRepository = likesRepository;
        this.likesMapper = likesMapper;
    }

    @Override
    public LikesDTO save(LikesDTO likesDTO) {
        log.debug("Request to save Likes : {}", likesDTO);
        Likes likes = likesMapper.toEntity(likesDTO);
        likes = likesRepository.save(likes);
        return likesMapper.toDto(likes);
    }

    @Override
    public Optional<LikesDTO> partialUpdate(LikesDTO likesDTO) {
        log.debug("Request to partially update Likes : {}", likesDTO);

        return likesRepository
            .findById(likesDTO.getId())
            .map(existingLikes -> {
                likesMapper.partialUpdate(existingLikes, likesDTO);

                return existingLikes;
            })
            .map(likesRepository::save)
            .map(likesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LikesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Likes");
        return likesRepository.findAll(pageable).map(likesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LikesDTO> findOne(Long id) {
        log.debug("Request to get Likes : {}", id);
        return likesRepository.findById(id).map(likesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Likes : {}", id);
        likesRepository.deleteById(id);
    }

    @Override
    public int getPostLikesCount(Long postId) {
        return likesRepository.getPostLikesCount(postId);
    }

    @Override
    public boolean isLiked(Long postId, Long userId) {
        return likesRepository.isLiked(postId, userId);
    }

    @Override
    public ResponseEntity<Void> setPostLike(Long appUserId, Long postId) {
        AppUserDTO appUser = appUserService.findByUserId(appUserId).orElse(null);
        if(Objects.isNull(appUser)){
            throw new EntityNotFoundException("App user not found");
        }
        PostDTO postDTO = postService.findOne(postId).orElse(null);
        if(Objects.isNull(postDTO)){
            throw new EntityNotFoundException("Post not found");
        }
        Likes likes = likesRepository.findByUserIdAndPostId(appUserId, postId);
        if(!Objects.isNull(likes)){
            likesRepository.delete(likes);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            likes = new Likes();
            likes.setPost(postMapper.toEntity(postDTO));
            likes.setUser(appUserMapper.toEntity(appUser));
            likesRepository.save(likes);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

}
