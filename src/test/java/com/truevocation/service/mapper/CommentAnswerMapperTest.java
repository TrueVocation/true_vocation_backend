package com.truevocation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommentAnswerMapperTest {

    private CommentAnswerMapper commentAnswerMapper;

    @BeforeEach
    public void setUp() {
        commentAnswerMapper = new CommentAnswerMapperImpl();
    }
}
