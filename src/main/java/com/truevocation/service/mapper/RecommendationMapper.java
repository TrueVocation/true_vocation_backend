package com.truevocation.service.mapper;

import com.truevocation.domain.Recommendation;
import com.truevocation.service.dto.RecommendationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Recommendation} and its DTO {@link RecommendationDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProfessionMapper.class })
public interface RecommendationMapper extends EntityMapper<RecommendationDTO, Recommendation> {
    @Mapping(target = "profession", source = "profession", qualifiedByName = "id")
    RecommendationDTO toDto(Recommendation s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RecommendationDTO toDtoId(Recommendation recommendation);
}
