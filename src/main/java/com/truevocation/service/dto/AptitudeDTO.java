package com.truevocation.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class AptitudeDTO implements Serializable {

    private Long id;

    private Integer code;

    private String name;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AptitudeDTO)) {
            return false;
        }
        AptitudeDTO aptitudeDTO = (AptitudeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aptitudeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "AptitudeDTO{" +
            "id=" + id +
            ", code=" + code +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
