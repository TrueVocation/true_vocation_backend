package com.truevocation.service.mapper;

import com.truevocation.domain.School;
import com.truevocation.service.dto.SchoolDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link School} and its DTO {@link SchoolDTO}.
 */
@Mapper(componentModel = "spring", uses = { CityMapper.class })
public interface SchoolMapper extends EntityMapper<SchoolDTO, School> {
    @Mapping(target = "city", source = "city", qualifiedByName = "id")
    SchoolDTO toDto(School s);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<SchoolDTO> toDtoIdSet(Set<School> school);
}
