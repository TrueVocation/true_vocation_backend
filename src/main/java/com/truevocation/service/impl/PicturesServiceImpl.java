package com.truevocation.service.impl;

import com.truevocation.domain.Pictures;
import com.truevocation.repository.PicturesRepository;
import com.truevocation.service.PicturesService;
import com.truevocation.service.dto.PicturesDTO;
import com.truevocation.service.mapper.PicturesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pictures}.
 */
@Service
@Transactional
public class PicturesServiceImpl implements PicturesService {

    private final Logger log = LoggerFactory.getLogger(PicturesServiceImpl.class);

    private final PicturesRepository picturesRepository;

    private final PicturesMapper picturesMapper;

    public PicturesServiceImpl(PicturesRepository picturesRepository, PicturesMapper picturesMapper) {
        this.picturesRepository = picturesRepository;
        this.picturesMapper = picturesMapper;
    }

    @Override
    public PicturesDTO save(PicturesDTO picturesDTO) {
        log.debug("Request to save Pictures : {}", picturesDTO);
        Pictures pictures = picturesMapper.toEntity(picturesDTO);
        pictures = picturesRepository.save(pictures);
        return picturesMapper.toDto(pictures);
    }

    @Override
    public Optional<PicturesDTO> partialUpdate(PicturesDTO picturesDTO) {
        log.debug("Request to partially update Pictures : {}", picturesDTO);

        return picturesRepository
            .findById(picturesDTO.getId())
            .map(existingPictures -> {
                picturesMapper.partialUpdate(existingPictures, picturesDTO);

                return existingPictures;
            })
            .map(picturesRepository::save)
            .map(picturesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PicturesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pictures");
        return picturesRepository.findAll(pageable).map(picturesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PicturesDTO> findOne(Long id) {
        log.debug("Request to get Pictures : {}", id);
        return picturesRepository.findById(id).map(picturesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pictures : {}", id);
        picturesRepository.deleteById(id);
    }
}
