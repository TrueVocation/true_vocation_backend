package com.truevocation.service.mapper;

import com.truevocation.domain.Achievement;
import com.truevocation.service.dto.AchievementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Achievement} and its DTO {@link AchievementDTO}.
 */
@Mapper(componentModel = "spring", uses = { PortfolioMapper.class })
public interface AchievementMapper extends EntityMapper<AchievementDTO, Achievement> {
    @Mapping(target = "portfolio", source = "portfolio", qualifiedByName = "id")
    AchievementDTO toDto(Achievement s);
}
