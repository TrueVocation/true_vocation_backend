package com.truevocation.service.dto;

import java.io.Serializable;
import java.util.List;

public class UniversityFilterDto implements Serializable {
    private Long cityId;
    private String search;
    private List<String> statuses;
    private boolean dormitory;
    private boolean military;
    private List<Price> averagePriceList;

    public static class Price {
        private Integer from;
        private Integer to;

        public Integer getFrom() {
            return from;
        }

        public void setFrom(Integer from) {
            this.from = from;
        }

        public Integer getTo() {
            return to;
        }

        public void setTo(Integer to) {
            this.to = to;
        }
    }

    public UniversityFilterDto() {
    }

    public UniversityFilterDto(Long cityId, String search, List<String> statuses, Boolean dormitory, Boolean military, List<Price> averagePriceList) {
        this.cityId = cityId;
        this.search = search;
        this.statuses = statuses;
        this.dormitory = dormitory;
        this.military = military;
        this.averagePriceList = averagePriceList;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }

    public boolean getDormitory() {
        return dormitory;
    }

    public void setDormitory(boolean dormitory) {
        this.dormitory = dormitory;
    }

    public boolean getMilitary() {
        return military;
    }

    public void setMilitary(boolean military) {
        this.military = military;
    }

    public List<Price> getAveragePriceList() {
        return averagePriceList;
    }

    public void setAveragePriceList(List<Price> averagePriceList) {
        this.averagePriceList = averagePriceList;
    }
}
