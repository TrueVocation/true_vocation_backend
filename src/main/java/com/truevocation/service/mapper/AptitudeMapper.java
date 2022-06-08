package com.truevocation.service.mapper;

import com.truevocation.domain.Aptitude;
import com.truevocation.service.dto.AptitudeDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Aptitude} and its DTO {@link AptitudeDTO}.
 */
@Mapper(componentModel = "spring")
public interface AptitudeMapper extends EntityMapper<AptitudeDTO, Aptitude>{
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AptitudeDTO toDtoId(Aptitude aptitude);
}
