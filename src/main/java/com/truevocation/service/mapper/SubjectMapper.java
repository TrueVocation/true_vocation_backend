package com.truevocation.service.mapper;

import com.truevocation.domain.Subject;
import com.truevocation.service.dto.SubjectDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Subject} and its DTO {@link SubjectDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubjectMapper extends EntityMapper<SubjectDTO, Subject> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<SubjectDTO> toDtoIdSet(Set<Subject> subject);
}
