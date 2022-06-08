package com.truevocation.service.impl;

import com.truevocation.domain.Answer;
import com.truevocation.repository.AnswerRepository;
import com.truevocation.service.AnswerService;
import com.truevocation.service.dto.AnswerDTO;
import com.truevocation.service.mapper.AnswerMapper;
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
 * Service Implementation for managing {@link Answer}.
 */
@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {

    private final Logger log = LoggerFactory.getLogger(AnswerServiceImpl.class);

    private final AnswerRepository answerRepository;

    private final AnswerMapper answerMapper;

    public AnswerServiceImpl(AnswerRepository answerRepository, AnswerMapper answerMapper) {
        this.answerRepository = answerRepository;
        this.answerMapper = answerMapper;
    }

    @Override
    public AnswerDTO save(AnswerDTO answerDTO) {
        log.debug("Request to save Answer : {}", answerDTO);
        Answer answer = answerMapper.toEntity(answerDTO);
        answer = answerRepository.save(answer);
        return answerMapper.toDto(answer);
    }

    @Override
    public Optional<AnswerDTO> partialUpdate(AnswerDTO answerDTO) {
        log.debug("Request to partially update Answer : {}", answerDTO);

        return answerRepository
            .findById(answerDTO.getId())
            .map(existingAnswer -> {
                answerMapper.partialUpdate(existingAnswer, answerDTO);

                return existingAnswer;
            })
            .map(answerRepository::save)
            .map(answerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnswerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Answers");
        return answerRepository.findAll(pageable).map(answerMapper::toDto);
    }

    /**
     *  Get all the answers where AnswerUser is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AnswerDTO> findAllWhereAnswerUserIsNull() {
        log.debug("Request to get all answers where AnswerUser is null");
        return StreamSupport
            .stream(answerRepository.findAll().spliterator(), false)
            .filter(answer -> answer.getAnswerUsers() == null)
            .map(answerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnswerDTO> findOne(Long id) {
        log.debug("Request to get Answer : {}", id);
        return answerRepository.findById(id).map(answerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Answer : {}", id);
        answerRepository.deleteById(id);
    }
}
