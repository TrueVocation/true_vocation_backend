package com.truevocation.service.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.truevocation.domain.University} entity.
 */
public class UniversityDTO implements Serializable {

    private Long id;

    private String name;

    private String address;

    @Size(max = 1000)
    private String description;

    private Boolean dormitory;

    private Boolean military;

    private String status;

    private String code;

    private String logo;

    private String picture;

    private Set<FacultyDTO> faculties = new HashSet<>();

    private CityDTO city;

    private int SpecialityCount;

    private HashMap<String, String> location;


    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public HashMap<String, String> getLocation() {
        return location;
    }

    public void setLocation(HashMap<String, String> location) {
        this.location = location;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDormitory() {
        return dormitory;
    }

    public void setDormitory(Boolean dormitory) {
        this.dormitory = dormitory;
    }

    public Boolean getMilitary() {
        return military;
    }

    public void setMilitary(Boolean military) {
        this.military = military;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<FacultyDTO> getFaculties() {
        return faculties;
    }

    public void setFaculties(Set<FacultyDTO> faculties) {
        this.faculties = faculties;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    public int getSpecialityCount() {
        return SpecialityCount;
    }

    public void setSpecialityCount(int specialityCount) {
        SpecialityCount = specialityCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UniversityDTO)) {
            return false;
        }

        UniversityDTO universityDTO = (UniversityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, universityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UniversityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", description='" + getDescription() + "'" +
            ", dormitory='" + getDormitory() + "'" +
            ", military='" + getMilitary() + "'" +
            ", status='" + getStatus() + "'" +
            ", code='" + getCode() + "'" +
            ", logo='" + getLogo() + "'" +
            ", faculties=" + getFaculties() +
            ", city=" + getCity() +
            "}";
    }
}
