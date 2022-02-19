package com.truevocation.service.mapper;

import com.truevocation.domain.AnswerUser;
import com.truevocation.service.dto.AnswerUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AnswerUser} and its DTO {@link AnswerUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionMapper.class, AnswerMapper.class, TestResultMapper.class })
public interface AnswerUserMapper extends EntityMapper<AnswerUserDTO, AnswerUser> {
    @Mapping(target = "question", source = "question", qualifiedByName = "id")
    @Mapping(target = "answer", source = "answer", qualifiedByName = "id")
    @Mapping(target = "testResult", source = "testResult", qualifiedByName = "id")
    AnswerUserDTO toDto(AnswerUser s);
}
