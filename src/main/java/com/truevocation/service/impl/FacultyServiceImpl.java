package com.truevocation.service.impl;

import com.truevocation.domain.Faculty;
import com.truevocation.repository.FacultyRepository;
import com.truevocation.service.FacultyService;
import com.truevocation.service.dto.FacultyDTO;
import com.truevocation.service.mapper.FacultyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Faculty}.
 */
@Service
@Transactional
public class FacultyServiceImpl implements FacultyService {

    private final Logger log = LoggerFactory.getLogger(FacultyServiceImpl.class);

    private final FacultyRepository facultyRepository;

    private final FacultyMapper facultyMapper;

    public FacultyServiceImpl(FacultyRepository facultyRepository, FacultyMapper facultyMapper) {
        this.facultyRepository = facultyRepository;
        this.facultyMapper = facultyMapper;
    }

    @Override
    public FacultyDTO save(FacultyDTO facultyDTO) {
        log.debug("Request to save Faculty : {}", facultyDTO);
        Faculty faculty = facultyMapper.toEntity(facultyDTO);
        faculty = facultyRepository.save(faculty);
        return facultyMapper.toDto(faculty);
    }

    @Override
    public Optional<FacultyDTO> partialUpdate(FacultyDTO facultyDTO) {
        log.debug("Request to partially update Faculty : {}", facultyDTO);

        return facultyRepository
            .findById(facultyDTO.getId())
            .map(existingFaculty -> {
                facultyMapper.partialUpdate(existingFaculty, facultyDTO);

                return existingFaculty;
            })
            .map(facultyRepository::save)
            .map(facultyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FacultyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Faculties");
        return facultyRepository.findAll(pageable).map(facultyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FacultyDTO> findOne(Long id) {
        log.debug("Request to get Faculty : {}", id);
        return facultyRepository.findById(id).map(facultyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Faculty : {}", id);
        facultyRepository.deleteById(id);
    }
}
