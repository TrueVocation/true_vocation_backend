package com.truevocation.service.impl;

import com.truevocation.domain.ProfTest;
import com.truevocation.repository.ProfTestRepository;
import com.truevocation.service.ProfTestService;
import com.truevocation.service.dto.ProfTestDTO;
import com.truevocation.service.mapper.ProfTestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProfTest}.
 */
@Service
@Transactional
public class ProfTestServiceImpl implements ProfTestService {

    private final Logger log = LoggerFactory.getLogger(ProfTestServiceImpl.class);

    private final ProfTestRepository profTestRepository;

    private final ProfTestMapper profTestMapper;

    public ProfTestServiceImpl(ProfTestRepository profTestRepository, ProfTestMapper profTestMapper) {
        this.profTestRepository = profTestRepository;
        this.profTestMapper = profTestMapper;
    }

    @Override
    public ProfTestDTO save(ProfTestDTO profTestDTO) {
        log.debug("Request to save ProfTest : {}", profTestDTO);
        ProfTest profTest = profTestMapper.toEntity(profTestDTO);
        profTest = profTestRepository.save(profTest);
        return profTestMapper.toDto(profTest);
    }

    @Override
    public Optional<ProfTestDTO> partialUpdate(ProfTestDTO profTestDTO) {
        log.debug("Request to partially update ProfTest : {}", profTestDTO);

        return profTestRepository
            .findById(profTestDTO.getId())
            .map(existingProfTest -> {
                profTestMapper.partialUpdate(existingProfTest, profTestDTO);

                return existingProfTest;
            })
            .map(profTestRepository::save)
            .map(profTestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProfTestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProfTests");
        return profTestRepository.findAll(pageable).map(profTestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfTestDTO> findOne(Long id) {
        log.debug("Request to get ProfTest : {}", id);
        return profTestRepository.findById(id).map(profTestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProfTest : {}", id);
        profTestRepository.deleteById(id);
    }
}
