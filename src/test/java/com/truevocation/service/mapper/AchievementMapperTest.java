package com.truevocation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AchievementMapperTest {

    private AchievementMapper achievementMapper;

    @BeforeEach
    public void setUp() {
        achievementMapper = new AchievementMapperImpl();
    }
}
