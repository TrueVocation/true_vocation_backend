package com.truevocation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PortfolioMapperTest {

    private PortfolioMapper portfolioMapper;

    @BeforeEach
    public void setUp() {
        portfolioMapper = new PortfolioMapperImpl();
    }
}
