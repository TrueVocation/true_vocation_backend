package com.truevocation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnswerUserMapperTest {

    private AnswerUserMapper answerUserMapper;

    @BeforeEach
    public void setUp() {
        answerUserMapper = new AnswerUserMapperImpl();
    }
}
