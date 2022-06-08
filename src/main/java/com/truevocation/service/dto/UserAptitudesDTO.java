package com.truevocation.service.dto;

import java.io.Serializable;

public class UserAptitudesDTO implements Serializable {
    private Integer score;
    private AptitudeDTO aptitudeDTO;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public AptitudeDTO getAptitudeDTO() {
        return aptitudeDTO;
    }

    public void setAptitudeDTO(AptitudeDTO aptitudeDTO) {
        this.aptitudeDTO = aptitudeDTO;
    }
}
