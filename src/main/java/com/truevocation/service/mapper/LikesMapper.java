package com.truevocation.service.mapper;

import com.truevocation.domain.Likes;
import com.truevocation.service.dto.LikesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Likes} and its DTO {@link LikesDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommentsMapper.class, AppUserMapper.class, PostMapper.class })
public interface LikesMapper extends EntityMapper<LikesDTO, Likes> {
    @Mapping(target = "comment", source = "comment", qualifiedByName = "id")
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    @Mapping(target = "post", source = "post", qualifiedByName = "id")
    LikesDTO toDto(Likes s);
}
