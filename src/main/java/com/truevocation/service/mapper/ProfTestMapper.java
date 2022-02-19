package com.truevocation.service.mapper;

import com.truevocation.domain.ProfTest;
import com.truevocation.service.dto.ProfTestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfTest} and its DTO {@link ProfTestDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProfTestMapper extends EntityMapper<ProfTestDTO, ProfTest> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfTestDTO toDtoId(ProfTest profTest);
}
