package com.truevocation.service.impl;

import com.truevocation.domain.AnswerUser;
import com.truevocation.repository.AnswerUserRepository;
import com.truevocation.repository.AptitudeRepository;
import com.truevocation.service.AnswerUserService;
import com.truevocation.service.dto.AnswerUserDTO;
import com.truevocation.service.dto.UserAptitudesDTO;
import com.truevocation.service.mapper.AnswerUserMapper;
import com.truevocation.service.mapper.AptitudeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link AnswerUser}.
 */
@Service
@Transactional
public class AnswerUserServiceImpl implements AnswerUserService {

    private final Logger log = LoggerFactory.getLogger(AnswerUserServiceImpl.class);

    private final AnswerUserRepository answerUserRepository;

    private final AnswerUserMapper answerUserMapper;

    private final AptitudeRepository aptitudeRepository;

    private final AptitudeMapper aptitudeMapper;

    public AnswerUserServiceImpl(AnswerUserRepository answerUserRepository, AnswerUserMapper answerUserMapper, AptitudeRepository aptitudeRepository, AptitudeMapper aptitudeMapper) {
        this.answerUserRepository = answerUserRepository;
        this.answerUserMapper = answerUserMapper;
        this.aptitudeRepository = aptitudeRepository;
        this.aptitudeMapper = aptitudeMapper;
    }

    @Override
    public AnswerUserDTO save(AnswerUserDTO answerUserDTO) {
        log.debug("Request to save AnswerUser : {}", answerUserDTO);
        AnswerUser answerUser = answerUserMapper.toEntity(answerUserDTO);
        answerUser = answerUserRepository.save(answerUser);
        return answerUserMapper.toDto(answerUser);
    }

    public AnswerUserDTO saveAnswerUser(AnswerUserDTO answerUserDTO) {
        log.debug("Request to save AnswerUser : {}", answerUserDTO);
        AnswerUser answerUser = answerUserMapper.toEntity(answerUserDTO);
        answerUser = answerUserRepository.save(answerUser);
        return answerUserMapper.toDto(answerUser);
    }

    @Override
    public List<AnswerUserDTO> saveAnswers(List<AnswerUserDTO> answerUserDTOs) {
        answerUserRepository.deleteAllByAppUser_Id(answerUserDTOs.get(0).getAppUser().getId());
        List<AnswerUserDTO> dbAnswerUsers = new ArrayList<>();
        answerUserDTOs.forEach(answerUserDTO -> {
            AnswerUserDTO answerUserDTOFromDb = answerUserMapper.toDto(answerUserRepository
                .findAnswerUserByAppUserIdAndQuestionId(answerUserDTO.getAppUser().getId(),
                answerUserDTO.getQuestion().getId()));
            if(answerUserDTOFromDb != null){
                answerUserDTOFromDb.setAnswer(answerUserDTO.getAnswer());
                partialUpdate(answerUserDTOFromDb);
            }else{
                save(answerUserDTO);
            }
            dbAnswerUsers.add(answerUserDTO);
        });
        return dbAnswerUsers;
    }

    @Override
    public boolean checkAnswerUserList(Long id) {
        return answerUserRepository.findAllByAppUserId(id).isEmpty();
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

    public List<UserAptitudesDTO> userAptitudes(Long appUserdtoId) {

        List<UserAptitudesDTO> userAptitudes = new ArrayList<>();

        List<AnswerUserDTO> userAnswers = new ArrayList();
        if (appUserdtoId != null) {
            answerUserRepository.findAllByAppUserId(appUserdtoId)
                .forEach(answerUser -> userAnswers.add(answerUserMapper.toDto(answerUser)));

            AtomicInteger firstAptitude = new AtomicInteger();
            AtomicInteger secondAptitude = new AtomicInteger();
            AtomicInteger thirdAptitude = new AtomicInteger();
            AtomicInteger fourthAptitude = new AtomicInteger();
            AtomicInteger fifthAptitude = new AtomicInteger();
            AtomicInteger sixthAptitude = new AtomicInteger();

            userAnswers.stream().map(AnswerUserDTO::getAnswer).forEach(answerDTO -> {
                switch (answerDTO.getPoint()) {
                    case 1: {
                        firstAptitude.getAndIncrement();
                        break;
                    }
                    case 2: {
                        secondAptitude.getAndIncrement();
                        break;
                    }
                    case 3:
                        thirdAptitude.getAndIncrement();
                        break;
                    case 4:
                        fourthAptitude.getAndIncrement();
                        break;
                    case 5:
                        fifthAptitude.getAndIncrement();
                        break;
                    case 6:
                        sixthAptitude.getAndIncrement();
                        break;
                }
            });
            for (int i = 1; i <= 6; i++) {
                UserAptitudesDTO userAptitudesDTO = new UserAptitudesDTO();
                userAptitudesDTO.setAptitudeDTO(aptitudeMapper.toDto(aptitudeRepository.findAptitudeByCode(i)));
                if (i == 1) {
                    userAptitudesDTO.setScore(firstAptitude.intValue());
                }
                if (i == 2) {
                    userAptitudesDTO.setScore(secondAptitude.intValue());
                }
                if (i == 3) {
                    userAptitudesDTO.setScore(thirdAptitude.intValue());
                }
                if (i == 4) {
                    userAptitudesDTO.setScore(fourthAptitude.intValue());
                }
                if (i == 5) {
                    userAptitudesDTO.setScore(fifthAptitude.intValue());
                }
                if (i == 6) {
                    userAptitudesDTO.setScore(sixthAptitude.intValue());
                }
                userAptitudes.add(userAptitudesDTO);
            }
            return userAptitudes.stream().sorted((Comparator.comparingInt(UserAptitudesDTO::getScore)).reversed()).collect(Collectors.toList());
        }

        return userAptitudes;
    }

}
