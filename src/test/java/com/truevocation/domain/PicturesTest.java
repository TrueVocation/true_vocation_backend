package com.truevocation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.truevocation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PicturesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pictures.class);
        Pictures pictures1 = new Pictures();
        pictures1.setId(1L);
        Pictures pictures2 = new Pictures();
        pictures2.setId(pictures1.getId());
        assertThat(pictures1).isEqualTo(pictures2);
        pictures2.setId(2L);
        assertThat(pictures1).isNotEqualTo(pictures2);
        pictures1.setId(null);
        assertThat(pictures1).isNotEqualTo(pictures2);
    }
}
