package com.truevocation.service.impl;

import com.truevocation.domain.Aptitude;
import com.truevocation.repository.AptitudeRepository;
import com.truevocation.service.AptitudeService;
import com.truevocation.service.dto.AptitudeDTO;
import com.truevocation.service.mapper.AptitudeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AptitudeServiceImpl implements AptitudeService {

    private final Logger log = LoggerFactory.getLogger(AptitudeServiceImpl.class);

    private final AptitudeRepository aptitudeRepository;

    private final AptitudeMapper aptitudeMapper;

    public AptitudeServiceImpl(AptitudeRepository aptitudeRepository, AptitudeMapper aptitudeMapper) {
        this.aptitudeRepository = aptitudeRepository;
        this.aptitudeMapper = aptitudeMapper;
    }

    @Override
    public AptitudeDTO save(AptitudeDTO aptitudeDTO) {
        log.debug("Request to save Aptitude : {}", aptitudeDTO);
        Aptitude aptitude = aptitudeMapper.toEntity(aptitudeDTO);
        aptitude = aptitudeRepository.save(aptitude);
        return aptitudeMapper.toDto(aptitude);
    }

    @Override
    public Optional<AptitudeDTO> partialUpdate(AptitudeDTO aptitudeDTO) {
        log.debug("Request to partially update Aptitude : {}", aptitudeDTO);

        return aptitudeRepository
            .findById(aptitudeDTO.getId())
            .map(existingAptitude -> {
                aptitudeMapper.partialUpdate(existingAptitude, aptitudeDTO);

                return existingAptitude;
            })
            .map(aptitudeRepository::save)
            .map(aptitudeMapper::toDto);
    }

    @Override
    @Transactional
    public Page<AptitudeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Aptitudesies");
        return aptitudeRepository.findAll(pageable).map(aptitudeMapper::toDto);
    }

    @Override
    @Transactional
    public Optional<AptitudeDTO> findOne(Long id) {
        log.debug("Request to get Aptitude : {}", id);
        return aptitudeRepository.findById(id).map(aptitudeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Aptitude : {}", id);
        aptitudeRepository.deleteById(id);
    }
}
