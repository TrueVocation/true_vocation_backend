package com.truevocation.service.mapper;

import com.truevocation.domain.Question;
import com.truevocation.service.dto.QuestionDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring", uses = { AnswerMapper.class, ProfTestMapper.class })
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {
    @Mapping(target = "answers", source = "answers", qualifiedByName = "idSet")
    @Mapping(target = "profTest", source = "profTest", qualifiedByName = "id")
    QuestionDTO toDto(Question s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionDTO toDtoId(Question question);

    @Mapping(target = "removeAnswer", ignore = true)
    Question toEntity(QuestionDTO questionDTO);
}
