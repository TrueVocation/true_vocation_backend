package com.truevocation.service.mapper;

import com.truevocation.domain.Portfolio;
import com.truevocation.service.dto.PortfolioDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Portfolio} and its DTO {@link PortfolioDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class, LanguageMapper.class, SchoolMapper.class })
public interface PortfolioMapper extends EntityMapper<PortfolioDTO, Portfolio> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    @Mapping(target = "languages", source = "languages", qualifiedByName = "idSet")
    @Mapping(target = "schools", source = "schools", qualifiedByName = "idSet")
    PortfolioDTO toDto(Portfolio s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PortfolioDTO toDtoId(Portfolio portfolio);

    @Mapping(target = "removeLanguage", ignore = true)
    @Mapping(target = "removeSchool", ignore = true)
    Portfolio toEntity(PortfolioDTO portfolioDTO);
}
