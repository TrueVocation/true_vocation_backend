package com.truevocation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.truevocation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfTestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfTestDTO.class);
        ProfTestDTO profTestDTO1 = new ProfTestDTO();
        profTestDTO1.setId(1L);
        ProfTestDTO profTestDTO2 = new ProfTestDTO();
        assertThat(profTestDTO1).isNotEqualTo(profTestDTO2);
        profTestDTO2.setId(profTestDTO1.getId());
        assertThat(profTestDTO1).isEqualTo(profTestDTO2);
        profTestDTO2.setId(2L);
        assertThat(profTestDTO1).isNotEqualTo(profTestDTO2);
        profTestDTO1.setId(null);
        assertThat(profTestDTO1).isNotEqualTo(profTestDTO2);
    }
}
