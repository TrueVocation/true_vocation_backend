package com.truevocation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestResultMapperTest {

    private TestResultMapper testResultMapper;

    @BeforeEach
    public void setUp() {
        testResultMapper = new TestResultMapperImpl();
    }
}
