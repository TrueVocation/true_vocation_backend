package com.truevocation.service.dto;

import com.truevocation.domain.Portfolio;
import com.truevocation.web.rest.vm.UserAccountDto;

public class PortfolioSaveDto {
    private PortfolioDTO portfolio;
    private UserAccountDto userAccountDto;


    public PortfolioSaveDto() {
    }

    public PortfolioSaveDto(PortfolioDTO portfolio, UserAccountDto userAccountDto) {
        this.portfolio = portfolio;
        this.userAccountDto = userAccountDto;
    }

    public PortfolioDTO getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(PortfolioDTO portfolio) {
        this.portfolio = portfolio;
    }

    public UserAccountDto getUserAccountDto() {
        return userAccountDto;
    }

    public void setUserAccountDto(UserAccountDto userAccountDto) {
        this.userAccountDto = userAccountDto;
    }
}
