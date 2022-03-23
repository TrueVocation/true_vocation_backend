package com.truevocation.service.impl;

import com.truevocation.domain.TestResult;
import com.truevocation.repository.TestResultRepository;
import com.truevocation.service.TestResultService;
import com.truevocation.service.dto.TestResultDTO;
import com.truevocation.service.mapper.TestResultMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestResult}.
 */
@Service
@Transactional
public class TestResultServiceImpl implements TestResultService {

    private final Logger log = LoggerFactory.getLogger(TestResultServiceImpl.class);

    private final TestResultRepository testResultRepository;

    private final TestResultMapper testResultMapper;

    public TestResultServiceImpl(TestResultRepository testResultRepository, TestResultMapper testResultMapper) {
        this.testResultRepository = testResultRepository;
        this.testResultMapper = testResultMapper;
    }

    @Override
    public TestResultDTO save(TestResultDTO testResultDTO) {
        log.debug("Request to save TestResult : {}", testResultDTO);
        TestResult testResult = testResultMapper.toEntity(testResultDTO);
        testResult = testResultRepository.save(testResult);
        return testResultMapper.toDto(testResult);
    }

    @Override
    public Optional<TestResultDTO> partialUpdate(TestResultDTO testResultDTO) {
        log.debug("Request to partially update TestResult : {}", testResultDTO);

        return testResultRepository
            .findById(testResultDTO.getId())
            .map(existingTestResult -> {
                testResultMapper.partialUpdate(existingTestResult, testResultDTO);

                return existingTestResult;
            })
            .map(testResultRepository::save)
            .map(testResultMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TestResultDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TestResults");
        return testResultRepository.findAll(pageable).map(testResultMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TestResultDTO> findOne(Long id) {
        log.debug("Request to get TestResult : {}", id);
        return testResultRepository.findById(id).map(testResultMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TestResult : {}", id);
        testResultRepository.deleteById(id);
    }
}
