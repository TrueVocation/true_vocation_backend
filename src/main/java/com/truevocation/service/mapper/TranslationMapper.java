package com.truevocation.service.mapper;

import com.truevocation.domain.Translation;
import com.truevocation.service.dto.TranslationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Translation} and its DTO {@link TranslationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TranslationMapper extends EntityMapper<TranslationDTO, Translation> {}
