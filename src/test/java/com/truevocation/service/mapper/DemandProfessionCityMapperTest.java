package com.truevocation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DemandProfessionCityMapperTest {

    private DemandProfessionCityMapper demandProfessionCityMapper;

    @BeforeEach
    public void setUp() {
        demandProfessionCityMapper = new DemandProfessionCityMapperImpl();
    }
}
