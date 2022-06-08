package com.truevocation.service;

import com.truevocation.domain.Aptitude;
import com.truevocation.service.dto.AnswerUserDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.truevocation.service.dto.AppUserDTO;
import com.truevocation.service.dto.AptitudeDTO;
import com.truevocation.service.dto.UserAptitudesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.truevocation.domain.AnswerUser}.
 */
public interface AnswerUserService {
    /**
     * Save a answerUser.
     *
     * @param answerUserDTO the entity to save.
     * @return the persisted entity.
     */
    AnswerUserDTO save(AnswerUserDTO answerUserDTO);


    List<AnswerUserDTO> saveAnswers(List<AnswerUserDTO> answerUserDTOs);

    /**
     * Partially updates a answerUser.
     *
     * @param answerUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AnswerUserDTO> partialUpdate(AnswerUserDTO answerUserDTO);

    /**
     * Get all the answerUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnswerUserDTO> findAll(Pageable pageable);

    /**
     * Get the "id" answerUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnswerUserDTO> findOne(Long id);

    /**
     * Delete the "id" answerUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<UserAptitudesDTO> userAptitudes(Long appUserDtoId);
}
