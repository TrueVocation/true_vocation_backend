package com.truevocation.service.mapper;

import com.truevocation.domain.Faculty;
import com.truevocation.service.dto.FacultyDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Faculty} and its DTO {@link FacultyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FacultyMapper extends EntityMapper<FacultyDTO, Faculty> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FacultyDTO toDtoId(Faculty faculty);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<FacultyDTO> toDtoIdSet(Set<Faculty> faculty);
}
