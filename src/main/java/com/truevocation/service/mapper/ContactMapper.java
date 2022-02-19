package com.truevocation.service.mapper;

import com.truevocation.domain.Contact;
import com.truevocation.service.dto.ContactDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contact} and its DTO {@link ContactDTO}.
 */
@Mapper(componentModel = "spring", uses = { CourseMapper.class, UniversityMapper.class, PortfolioMapper.class })
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {
    @Mapping(target = "course", source = "course", qualifiedByName = "id")
    @Mapping(target = "university", source = "university", qualifiedByName = "id")
    @Mapping(target = "portfolio", source = "portfolio", qualifiedByName = "id")
    ContactDTO toDto(Contact s);
}
