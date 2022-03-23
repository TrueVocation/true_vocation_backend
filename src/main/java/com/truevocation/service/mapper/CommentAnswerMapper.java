package com.truevocation.service.mapper;

import com.truevocation.domain.CommentAnswer;
import com.truevocation.service.dto.CommentAnswerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommentAnswer} and its DTO {@link CommentAnswerDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommentsMapper.class, AppUserMapper.class })
public interface CommentAnswerMapper extends EntityMapper<CommentAnswerDTO, CommentAnswer> {
    @Mapping(target = "comment", source = "comment", qualifiedByName = "id")
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    CommentAnswerDTO toDto(CommentAnswer s);
}
