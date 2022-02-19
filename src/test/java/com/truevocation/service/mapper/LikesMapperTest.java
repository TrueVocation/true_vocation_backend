package com.truevocation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LikesMapperTest {

    private LikesMapper likesMapper;

    @BeforeEach
    public void setUp() {
        likesMapper = new LikesMapperImpl();
    }
}
