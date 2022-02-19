package com.truevocation.service.mapper;

import com.truevocation.domain.Course;
import com.truevocation.service.dto.CourseDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring", uses = { CityMapper.class })
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {
    @Mapping(target = "city", source = "city", qualifiedByName = "id")
    CourseDTO toDto(Course s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CourseDTO toDtoId(Course course);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<CourseDTO> toDtoIdSet(Set<Course> course);
}
