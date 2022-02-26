package com.truevocation.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.truevocation.domain.ProfTest} entity.
 */
public class ProfTestDTO implements Serializable {

    private Long id;

    private String name;

    @Size(max = 1000)
    private String description;

    @Size(max = 1000)
    private String instruction;

    @Size(max = 1000)
    private String picture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfTestDTO)) {
            return false;
        }

        ProfTestDTO profTestDTO = (ProfTestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profTestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfTestDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", instruction='" + getInstruction() + "'" +
            ", picture='" + getPicture() + "'" +
            "}";
    }
}
