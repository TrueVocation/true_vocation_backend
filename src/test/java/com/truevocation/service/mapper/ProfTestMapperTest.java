package com.truevocation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfTestMapperTest {

    private ProfTestMapper profTestMapper;

    @BeforeEach
    public void setUp() {
        profTestMapper = new ProfTestMapperImpl();
    }
}
