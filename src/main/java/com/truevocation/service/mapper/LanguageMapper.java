package com.truevocation.service.mapper;

import com.truevocation.domain.Language;
import com.truevocation.service.dto.LanguageDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Language} and its DTO {@link LanguageDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LanguageMapper extends EntityMapper<LanguageDTO, Language> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<LanguageDTO> toDtoIdSet(Set<Language> language);
}
