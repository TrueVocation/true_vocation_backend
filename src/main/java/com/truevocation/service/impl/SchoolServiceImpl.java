package com.truevocation.service.impl;

import com.truevocation.domain.School;
import com.truevocation.repository.SchoolRepository;
import com.truevocation.service.SchoolService;
import com.truevocation.service.dto.SchoolDTO;
import com.truevocation.service.mapper.SchoolMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link School}.
 */
@Service
@Transactional
public class SchoolServiceImpl implements SchoolService {

    private final Logger log = LoggerFactory.getLogger(SchoolServiceImpl.class);

    private final SchoolRepository schoolRepository;

    private final SchoolMapper schoolMapper;

    public SchoolServiceImpl(SchoolRepository schoolRepository, SchoolMapper schoolMapper) {
        this.schoolRepository = schoolRepository;
        this.schoolMapper = schoolMapper;
    }

    @Override
    public SchoolDTO save(SchoolDTO schoolDTO) {
        log.debug("Request to save School : {}", schoolDTO);
        School school = schoolMapper.toEntity(schoolDTO);
        school = schoolRepository.save(school);
        return schoolMapper.toDto(school);
    }

    @Override
    public Optional<SchoolDTO> partialUpdate(SchoolDTO schoolDTO) {
        log.debug("Request to partially update School : {}", schoolDTO);

        return schoolRepository
            .findById(schoolDTO.getId())
            .map(existingSchool -> {
                schoolMapper.partialUpdate(existingSchool, schoolDTO);

                return existingSchool;
            })
            .map(schoolRepository::save)
            .map(schoolMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchoolDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Schools");
        return schoolRepository.findAll(pageable).map(schoolMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SchoolDTO> findOne(Long id) {
        log.debug("Request to get School : {}", id);
        return schoolRepository.findById(id).map(schoolMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete School : {}", id);
        schoolRepository.deleteById(id);
    }
}
