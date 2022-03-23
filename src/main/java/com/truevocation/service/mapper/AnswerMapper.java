package com.truevocation.service.mapper;

import com.truevocation.domain.Answer;
import com.truevocation.service.dto.AnswerDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Answer} and its DTO {@link AnswerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AnswerMapper extends EntityMapper<AnswerDTO, Answer> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AnswerDTO toDtoId(Answer answer);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<AnswerDTO> toDtoIdSet(Set<Answer> answer);
}
