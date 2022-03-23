package com.truevocation.service.mapper;

import com.truevocation.domain.Pictures;
import com.truevocation.service.dto.PicturesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pictures} and its DTO {@link PicturesDTO}.
 */
@Mapper(componentModel = "spring", uses = { CourseMapper.class, UniversityMapper.class, PortfolioMapper.class })
public interface PicturesMapper extends EntityMapper<PicturesDTO, Pictures> {
    @Mapping(target = "course", source = "course", qualifiedByName = "id")
    @Mapping(target = "university", source = "university", qualifiedByName = "id")
    @Mapping(target = "portfolio", source = "portfolio", qualifiedByName = "id")
    PicturesDTO toDto(Pictures s);
}
