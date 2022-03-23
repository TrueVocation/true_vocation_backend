package com.truevocation.service.impl;

import com.truevocation.domain.DemandProfessionCity;
import com.truevocation.repository.DemandProfessionCityRepository;
import com.truevocation.service.DemandProfessionCityService;
import com.truevocation.service.dto.DemandProfessionCityDTO;
import com.truevocation.service.mapper.DemandProfessionCityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DemandProfessionCity}.
 */
@Service
@Transactional
public class DemandProfessionCityServiceImpl implements DemandProfessionCityService {

    private final Logger log = LoggerFactory.getLogger(DemandProfessionCityServiceImpl.class);

    private final DemandProfessionCityRepository demandProfessionCityRepository;

    private final DemandProfessionCityMapper demandProfessionCityMapper;

    public DemandProfessionCityServiceImpl(
        DemandProfessionCityRepository demandProfessionCityRepository,
        DemandProfessionCityMapper demandProfessionCityMapper
    ) {
        this.demandProfessionCityRepository = demandProfessionCityRepository;
        this.demandProfessionCityMapper = demandProfessionCityMapper;
    }

    @Override
    public DemandProfessionCityDTO save(DemandProfessionCityDTO demandProfessionCityDTO) {
        log.debug("Request to save DemandProfessionCity : {}", demandProfessionCityDTO);
        DemandProfessionCity demandProfessionCity = demandProfessionCityMapper.toEntity(demandProfessionCityDTO);
        demandProfessionCity = demandProfessionCityRepository.save(demandProfessionCity);
        return demandProfessionCityMapper.toDto(demandProfessionCity);
    }

    @Override
    public Optional<DemandProfessionCityDTO> partialUpdate(DemandProfessionCityDTO demandProfessionCityDTO) {
        log.debug("Request to partially update DemandProfessionCity : {}", demandProfessionCityDTO);

        return demandProfessionCityRepository
            .findById(demandProfessionCityDTO.getId())
            .map(existingDemandProfessionCity -> {
                demandProfessionCityMapper.partialUpdate(existingDemandProfessionCity, demandProfessionCityDTO);

                return existingDemandProfessionCity;
            })
            .map(demandProfessionCityRepository::save)
            .map(demandProfessionCityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DemandProfessionCityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DemandProfessionCities");
        return demandProfessionCityRepository.findAll(pageable).map(demandProfessionCityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DemandProfessionCityDTO> findOne(Long id) {
        log.debug("Request to get DemandProfessionCity : {}", id);
        return demandProfessionCityRepository.findById(id).map(demandProfessionCityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DemandProfessionCity : {}", id);
        demandProfessionCityRepository.deleteById(id);
    }
}
