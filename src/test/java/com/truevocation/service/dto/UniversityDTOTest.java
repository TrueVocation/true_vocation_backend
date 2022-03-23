package com.truevocation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.truevocation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UniversityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UniversityDTO.class);
        UniversityDTO universityDTO1 = new UniversityDTO();
        universityDTO1.setId(1L);
        UniversityDTO universityDTO2 = new UniversityDTO();
        assertThat(universityDTO1).isNotEqualTo(universityDTO2);
        universityDTO2.setId(universityDTO1.getId());
        assertThat(universityDTO1).isEqualTo(universityDTO2);
        universityDTO2.setId(2L);
        assertThat(universityDTO1).isNotEqualTo(universityDTO2);
        universityDTO1.setId(null);
        assertThat(universityDTO1).isNotEqualTo(universityDTO2);
    }
}
