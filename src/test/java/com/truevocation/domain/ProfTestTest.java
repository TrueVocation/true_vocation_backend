package com.truevocation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.truevocation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfTestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfTest.class);
        ProfTest profTest1 = new ProfTest();
        profTest1.setId(1L);
        ProfTest profTest2 = new ProfTest();
        profTest2.setId(profTest1.getId());
        assertThat(profTest1).isEqualTo(profTest2);
        profTest2.setId(2L);
        assertThat(profTest1).isNotEqualTo(profTest2);
        profTest1.setId(null);
        assertThat(profTest1).isNotEqualTo(profTest2);
    }
}
