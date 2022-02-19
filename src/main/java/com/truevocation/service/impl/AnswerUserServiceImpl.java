package com.truevocation.service.impl;

import com.truevocation.domain.AnswerUser;
import com.truevocation.repository.AnswerUserRepository;
import com.truevocation.service.AnswerUserService;
import com.truevocation.service.dto.AnswerUserDTO;
import com.truevocation.service.mapper.AnswerUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AnswerUser}.
 */
@Service
@Transactional
public class AnswerUserServiceImpl implements AnswerUserService {

    private final Logger log = LoggerFactory.getLogger(AnswerUserServiceImpl.class);

    private final AnswerUserRepository answerUserRepository;

    private final AnswerUserMapper answerUserMapper;

    public AnswerUserServiceImpl(AnswerUserRepository answerUserRepository, AnswerUserMapper answerUserMapper) {
        this.answerUserRepository = answerUserRepository;
        this.answerUserMapper = answerUserMapper;
    }

    @Override
    public AnswerUserDTO save(AnswerUserDTO answerUserDTO) {
        log.debug("Request to save AnswerUser : {}", answerUserDTO);
        AnswerUser answerUser = answerUserMapper.toEntity(answerUserDTO);
        answerUser = answerUserRepository.save(answerUser);
        return answerUserMapper.toDto(answerUser);
    }

    @Override
    public Optional<AnswerUserDTO> partialUpdate(AnswerUserDTO answerUserDTO) {
        log.debug("Request to partially update AnswerUser : {}", answerUserDTO);

        return answerUserRepository
            .findById(answerUserDTO.getId())
            .map(existingAnswerUser -> {
                answerUserMapper.partialUpdate(existingAnswerUser, answerUserDTO);

                return existingAnswerUser;
            })
            .map(answerUserRepository::save)
            .map(answerUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnswerUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AnswerUsers");
        return answerUserRepository.findAll(pageable).map(answerUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnswerUserDTO> findOne(Long id) {
        log.debug("Request to get AnswerUser : {}", id);
        return answerUserRepository.findById(id).map(answerUserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnswerUser : {}", id);
        answerUserRepository.deleteById(id);
    }
}
