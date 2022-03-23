package com.truevocation.service.mapper;

import com.truevocation.domain.Favorite;
import com.truevocation.service.dto.FavoriteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Favorite} and its DTO {@link FavoriteDTO}.
 */
@Mapper(componentModel = "spring", uses = { UniversityMapper.class, AppUserMapper.class, PostMapper.class })
public interface FavoriteMapper extends EntityMapper<FavoriteDTO, Favorite> {
    @Mapping(target = "university", source = "university", qualifiedByName = "id")
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    @Mapping(target = "post", source = "post", qualifiedByName = "id")
    FavoriteDTO toDto(Favorite s);
}
