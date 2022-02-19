package com.truevocation.service.mapper;

import com.truevocation.domain.University;
import com.truevocation.service.dto.UniversityDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link University} and its DTO {@link UniversityDTO}.
 */
@Mapper(componentModel = "spring", uses = { FacultyMapper.class, CityMapper.class })
public interface UniversityMapper extends EntityMapper<UniversityDTO, University> {
    @Mapping(target = "faculties", source = "faculties", qualifiedByName = "idSet")
    @Mapping(target = "city", source = "city", qualifiedByName = "id")
    UniversityDTO toDto(University s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UniversityDTO toDtoId(University university);

    @Mapping(target = "removeFaculty", ignore = true)
    University toEntity(UniversityDTO universityDTO);
}
