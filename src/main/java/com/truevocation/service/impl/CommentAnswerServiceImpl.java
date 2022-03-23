package com.truevocation.service.impl;

import com.truevocation.domain.CommentAnswer;
import com.truevocation.repository.CommentAnswerRepository;
import com.truevocation.service.CommentAnswerService;
import com.truevocation.service.dto.CommentAnswerDTO;
import com.truevocation.service.mapper.CommentAnswerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CommentAnswer}.
 */
@Service
@Transactional
public class CommentAnswerServiceImpl implements CommentAnswerService {

    private final Logger log = LoggerFactory.getLogger(CommentAnswerServiceImpl.class);

    private final CommentAnswerRepository commentAnswerRepository;

    private final CommentAnswerMapper commentAnswerMapper;

    public CommentAnswerServiceImpl(CommentAnswerRepository commentAnswerRepository, CommentAnswerMapper commentAnswerMapper) {
        this.commentAnswerRepository = commentAnswerRepository;
        this.commentAnswerMapper = commentAnswerMapper;
    }

    @Override
    public CommentAnswerDTO save(CommentAnswerDTO commentAnswerDTO) {
        log.debug("Request to save CommentAnswer : {}", commentAnswerDTO);
        CommentAnswer commentAnswer = commentAnswerMapper.toEntity(commentAnswerDTO);
        commentAnswer = commentAnswerRepository.save(commentAnswer);
        return commentAnswerMapper.toDto(commentAnswer);
    }

    @Override
    public Optional<CommentAnswerDTO> partialUpdate(CommentAnswerDTO commentAnswerDTO) {
        log.debug("Request to partially update CommentAnswer : {}", commentAnswerDTO);

        return commentAnswerRepository
            .findById(commentAnswerDTO.getId())
            .map(existingCommentAnswer -> {
                commentAnswerMapper.partialUpdate(existingCommentAnswer, commentAnswerDTO);

                return existingCommentAnswer;
            })
            .map(commentAnswerRepository::save)
            .map(commentAnswerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentAnswerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CommentAnswers");
        return commentAnswerRepository.findAll(pageable).map(commentAnswerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentAnswerDTO> findOne(Long id) {
        log.debug("Request to get CommentAnswer : {}", id);
        return commentAnswerRepository.findById(id).map(commentAnswerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CommentAnswer : {}", id);
        commentAnswerRepository.deleteById(id);
    }
}
