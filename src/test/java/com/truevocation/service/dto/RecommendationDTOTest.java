package com.truevocation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.truevocation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecommendationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecommendationDTO.class);
        RecommendationDTO recommendationDTO1 = new RecommendationDTO();
        recommendationDTO1.setId(1L);
        RecommendationDTO recommendationDTO2 = new RecommendationDTO();
        assertThat(recommendationDTO1).isNotEqualTo(recommendationDTO2);
        recommendationDTO2.setId(recommendationDTO1.getId());
        assertThat(recommendationDTO1).isEqualTo(recommendationDTO2);
        recommendationDTO2.setId(2L);
        assertThat(recommendationDTO1).isNotEqualTo(recommendationDTO2);
        recommendationDTO1.setId(null);
        assertThat(recommendationDTO1).isNotEqualTo(recommendationDTO2);
    }
}
