package com.truevocation.service.mapper;

import com.truevocation.domain.Specialty;
import com.truevocation.service.dto.SpecialtyDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Specialty} and its DTO {@link SpecialtyDTO}.
 */
@Mapper(componentModel = "spring", uses = { SubjectMapper.class, ProfessionMapper.class, FacultyMapper.class })
public interface SpecialtyMapper extends EntityMapper<SpecialtyDTO, Specialty> {
    @Mapping(target = "subjects", source = "subjects", qualifiedByName = "idSet")
    @Mapping(target = "professions", source = "professions", qualifiedByName = "idSet")
    @Mapping(target = "faculty", source = "faculty", qualifiedByName = "id")
    SpecialtyDTO toDto(Specialty s);

    @Mapping(target = "removeSubject", ignore = true)
    @Mapping(target = "removeProfession", ignore = true)
    Specialty toEntity(SpecialtyDTO specialtyDTO);
}
