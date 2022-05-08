package com.truevocation.service.impl;

import com.truevocation.domain.Question;
import com.truevocation.repository.QuestionRepository;
import com.truevocation.service.QuestionService;
import com.truevocation.service.dto.QuestionDTO;
import com.truevocation.service.mapper.QuestionMapper;
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
 * Service Implementation for managing {@link Question}.
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;

    private final QuestionMapper questionMapper;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    @Override
    public QuestionDTO save(QuestionDTO questionDTO) {
        log.debug("Request to save Question : {}", questionDTO);
        Question question = questionMapper.toEntity(questionDTO);
        question = questionRepository.save(question);
        return questionMapper.toDto(question);
    }

    @Override
    public Optional<QuestionDTO> partialUpdate(QuestionDTO questionDTO) {
        log.debug("Request to partially update Question : {}", questionDTO);

        return questionRepository
            .findById(questionDTO.getId())
            .map(existingQuestion -> {
                questionMapper.partialUpdate(existingQuestion, questionDTO);

                return existingQuestion;
            })
            .map(questionRepository::save)
            .map(questionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Questions");
        return questionRepository.findAll(pageable).map(questionMapper::toDto);
    }

    public Page<QuestionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return questionRepository.findAllWithEagerRelationships(pageable).map(questionMapper::toDto);
    }

    public Page<QuestionDTO> findAllWithEagerRelationships(Pageable pageable,Long testId) {
        return questionRepository.findAllWithEagerRelationships(pageable,testId).map(questionMapper::toDto);
    }

    public int findAllCountByTestId(Long testId) {
        return questionRepository.findAllTestQuestionsCount(testId);
    }

    /**
     *  Get all the questions where AnswerUser is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<QuestionDTO> findAllWhereAnswerUserIsNull() {
        log.debug("Request to get all questions where AnswerUser is null");
        return StreamSupport
            .stream(questionRepository.findAll().spliterator(), false)
            .filter(question -> question.getAnswerUser() == null)
            .map(questionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionDTO> findOne(Long id) {
        log.debug("Request to get Question : {}", id);
        return questionRepository.findOneWithEagerRelationships(id).map(questionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Question : {}", id);
        questionRepository.deleteById(id);
    }
}
