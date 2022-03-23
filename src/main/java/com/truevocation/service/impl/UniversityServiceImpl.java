package com.truevocation.service.impl;

import com.truevocation.domain.University;
import com.truevocation.repository.UniversityRepository;
import com.truevocation.service.UniversityService;
import com.truevocation.service.dto.UniversityDTO;
import com.truevocation.service.mapper.UniversityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link University}.
 */
@Service
@Transactional
public class UniversityServiceImpl implements UniversityService {

    private final Logger log = LoggerFactory.getLogger(UniversityServiceImpl.class);

    private final UniversityRepository universityRepository;

    private final UniversityMapper universityMapper;

    public UniversityServiceImpl(UniversityRepository universityRepository, UniversityMapper universityMapper) {
        this.universityRepository = universityRepository;
        this.universityMapper = universityMapper;
    }

    @Override
    public UniversityDTO save(UniversityDTO universityDTO) {
        log.debug("Request to save University : {}", universityDTO);
        University university = universityMapper.toEntity(universityDTO);
        university = universityRepository.save(university);
        return universityMapper.toDto(university);
    }

    @Override
    public Optional<UniversityDTO> partialUpdate(UniversityDTO universityDTO) {
        log.debug("Request to partially update University : {}", universityDTO);

        return universityRepository
            .findById(universityDTO.getId())
            .map(existingUniversity -> {
                universityMapper.partialUpdate(existingUniversity, universityDTO);

                return existingUniversity;
            })
            .map(universityRepository::save)
            .map(universityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UniversityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Universities");
        return universityRepository.findAll(pageable).map(universityMapper::toDto);
    }

    public Page<UniversityDTO> findAllWithEagerRelationships(Pageable pageable) {
        return universityRepository.findAllWithEagerRelationships(pageable).map(universityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UniversityDTO> findOne(Long id) {
        log.debug("Request to get University : {}", id);
        return universityRepository.findOneWithEagerRelationships(id).map(universityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete University : {}", id);
        universityRepository.deleteById(id);
    }
}
