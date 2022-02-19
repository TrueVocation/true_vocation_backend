package com.truevocation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.truevocation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecommendationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recommendation.class);
        Recommendation recommendation1 = new Recommendation();
        recommendation1.setId(1L);
        Recommendation recommendation2 = new Recommendation();
        recommendation2.setId(recommendation1.getId());
        assertThat(recommendation1).isEqualTo(recommendation2);
        recommendation2.setId(2L);
        assertThat(recommendation1).isNotEqualTo(recommendation2);
        recommendation1.setId(null);
        assertThat(recommendation1).isNotEqualTo(recommendation2);
    }
}
