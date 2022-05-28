package com.truevocation.service.impl;

import com.truevocation.domain.Likes;
import com.truevocation.repository.LikesRepository;
import com.truevocation.service.LikesService;
import com.truevocation.service.dto.LikesDTO;
import com.truevocation.service.mapper.LikesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Likes}.
 */
@Service
@Transactional
public class LikesServiceImpl implements LikesService {

    private final Logger log = LoggerFactory.getLogger(LikesServiceImpl.class);

    private final LikesRepository likesRepository;

    private final LikesMapper likesMapper;

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
}
