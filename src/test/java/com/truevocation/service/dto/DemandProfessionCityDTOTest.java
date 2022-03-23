package com.truevocation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.truevocation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemandProfessionCityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandProfessionCityDTO.class);
        DemandProfessionCityDTO demandProfessionCityDTO1 = new DemandProfessionCityDTO();
        demandProfessionCityDTO1.setId(1L);
        DemandProfessionCityDTO demandProfessionCityDTO2 = new DemandProfessionCityDTO();
        assertThat(demandProfessionCityDTO1).isNotEqualTo(demandProfessionCityDTO2);
        demandProfessionCityDTO2.setId(demandProfessionCityDTO1.getId());
        assertThat(demandProfessionCityDTO1).isEqualTo(demandProfessionCityDTO2);
        demandProfessionCityDTO2.setId(2L);
        assertThat(demandProfessionCityDTO1).isNotEqualTo(demandProfessionCityDTO2);
        demandProfessionCityDTO1.setId(null);
        assertThat(demandProfessionCityDTO1).isNotEqualTo(demandProfessionCityDTO2);
    }
}
