package com.truevocation.service.mapper;

import com.truevocation.domain.TestResult;
import com.truevocation.service.dto.TestResultDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TestResult} and its DTO {@link TestResultDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class, RecommendationMapper.class, ProfTestMapper.class })
public interface TestResultMapper extends EntityMapper<TestResultDTO, TestResult> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    @Mapping(target = "recommendation", source = "recommendation", qualifiedByName = "id")
    @Mapping(target = "profTest", source = "profTest", qualifiedByName = "id")
    TestResultDTO toDto(TestResult s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TestResultDTO toDtoId(TestResult testResult);
}
