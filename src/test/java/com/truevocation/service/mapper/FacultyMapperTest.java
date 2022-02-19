package com.truevocation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacultyMapperTest {

    private FacultyMapper facultyMapper;

    @BeforeEach
    public void setUp() {
        facultyMapper = new FacultyMapperImpl();
    }
}
