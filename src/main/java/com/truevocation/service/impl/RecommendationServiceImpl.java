package com.truevocation.service.impl;

import com.truevocation.domain.Recommendation;
import com.truevocation.repository.RecommendationRepository;
import com.truevocation.service.RecommendationService;
import com.truevocation.service.dto.RecommendationDTO;
import com.truevocation.service.mapper.RecommendationMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Recommendation}.
 */
@Service
@Transactional
public class RecommendationServiceImpl implements RecommendationService {

    private final Logger log = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final RecommendationRepository recommendationRepository;

    private final RecommendationMapper recommendationMapper;

    public RecommendationServiceImpl(RecommendationRepository recommendationRepository, RecommendationMapper recommendationMapper) {
        this.recommendationRepository = recommendationRepository;
        this.recommendationMapper = recommendationMapper;
    }

    @Override
    public RecommendationDTO save(RecommendationDTO recommendationDTO) {
        log.debug("Request to save Recommendation : {}", recommendationDTO);
        Recommendation recommendation = recommendationMapper.toEntity(recommendationDTO);
        recommendation = recommendationRepository.save(recommendation);
        return recommendationMapper.toDto(recommendation);
    }

    @Override
    public Optional<RecommendationDTO> partialUpdate(RecommendationDTO recommendationDTO) {
        log.debug("Request to partially update Recommendation : {}", recommendationDTO);

        return recommendationRepository
            .findById(recommendationDTO.getId())
            .map(existingRecommendation -> {
                recommendationMapper.partialUpdate(existingRecommendation, recommendationDTO);

                return existingRecommendation;
            })
            .map(recommendationRepository::save)
            .map(recommendationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecommendationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Recommendations");
        return recommendationRepository.findAll(pageable).map(recommendationMapper::toDto);
    }

    /**
     *  Get all the recommendations where TestResult is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RecommendationDTO> findAllWhereTestResultIsNull() {
        log.debug("Request to get all recommendations where TestResult is null");
        return StreamSupport
            .stream(recommendationRepository.findAll().spliterator(), false)
            .filter(recommendation -> recommendation.getTestResult() == null)
            .map(recommendationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecommendationDTO> findOne(Long id) {
        log.debug("Request to get Recommendation : {}", id);
        return recommendationRepository.findById(id).map(recommendationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Recommendation : {}", id);
        recommendationRepository.deleteById(id);
    }
}
