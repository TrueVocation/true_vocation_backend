package com.truevocation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecommendationMapperTest {

    private RecommendationMapper recommendationMapper;

    @BeforeEach
    public void setUp() {
        recommendationMapper = new RecommendationMapperImpl();
    }
}
