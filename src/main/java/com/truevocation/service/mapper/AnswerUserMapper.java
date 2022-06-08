package com.truevocation.service.mapper;

import com.truevocation.domain.AnswerUser;
import com.truevocation.service.dto.AnswerUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AnswerUser} and its DTO {@link AnswerUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionMapper.class, AnswerMapper.class, TestResultMapper.class })
public interface AnswerUserMapper extends EntityMapper<AnswerUserDTO, AnswerUser> {
    @Mapping(target = "question")
    @Mapping(target = "answer", source = "answer")
    @Mapping(target = "testResult", source = "testResult")
    AnswerUserDTO toDto(AnswerUser s);
}
