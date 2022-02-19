package com.truevocation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpecialtyMapperTest {

    private SpecialtyMapper specialtyMapper;

    @BeforeEach
    public void setUp() {
        specialtyMapper = new SpecialtyMapperImpl();
    }
}
