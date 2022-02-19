package com.truevocation.service.impl;

import com.truevocation.domain.Profession;
import com.truevocation.repository.ProfessionRepository;
import com.truevocation.service.ProfessionService;
import com.truevocation.service.dto.ProfessionDTO;
import com.truevocation.service.mapper.ProfessionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Profession}.
 */
@Service
@Transactional
public class ProfessionServiceImpl implements ProfessionService {

    private final Logger log = LoggerFactory.getLogger(ProfessionServiceImpl.class);

    private final ProfessionRepository professionRepository;

    private final ProfessionMapper professionMapper;

    public ProfessionServiceImpl(ProfessionRepository professionRepository, ProfessionMapper professionMapper) {
        this.professionRepository = professionRepository;
        this.professionMapper = professionMapper;
    }

    @Override
    public ProfessionDTO save(ProfessionDTO professionDTO) {
        log.debug("Request to save Profession : {}", professionDTO);
        Profession profession = professionMapper.toEntity(professionDTO);
        profession = professionRepository.save(profession);
        return professionMapper.toDto(profession);
    }

    @Override
    public Optional<ProfessionDTO> partialUpdate(ProfessionDTO professionDTO) {
        log.debug("Request to partially update Profession : {}", professionDTO);

        return professionRepository
            .findById(professionDTO.getId())
            .map(existingProfession -> {
                professionMapper.partialUpdate(existingProfession, professionDTO);

                return existingProfession;
            })
            .map(professionRepository::save)
            .map(professionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProfessionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Professions");
        return professionRepository.findAll(pageable).map(professionMapper::toDto);
    }

    public Page<ProfessionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return professionRepository.findAllWithEagerRelationships(pageable).map(professionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfessionDTO> findOne(Long id) {
        log.debug("Request to get Profession : {}", id);
        return professionRepository.findOneWithEagerRelationships(id).map(professionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Profession : {}", id);
        professionRepository.deleteById(id);
    }
}
