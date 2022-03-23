package com.truevocation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PicturesMapperTest {

    private PicturesMapper picturesMapper;

    @BeforeEach
    public void setUp() {
        picturesMapper = new PicturesMapperImpl();
    }
}
