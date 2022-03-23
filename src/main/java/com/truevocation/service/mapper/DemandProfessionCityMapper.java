package com.truevocation.service.mapper;

import com.truevocation.domain.DemandProfessionCity;
import com.truevocation.service.dto.DemandProfessionCityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DemandProfessionCity} and its DTO {@link DemandProfessionCityDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProfessionMapper.class, CityMapper.class })
public interface DemandProfessionCityMapper extends EntityMapper<DemandProfessionCityDTO, DemandProfessionCity> {
    @Mapping(target = "profession", source = "profession", qualifiedByName = "id")
    @Mapping(target = "city", source = "city", qualifiedByName = "id")
    DemandProfessionCityDTO toDto(DemandProfessionCity s);
}
