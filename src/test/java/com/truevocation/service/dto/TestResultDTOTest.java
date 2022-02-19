package com.truevocation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.truevocation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestResultDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestResultDTO.class);
        TestResultDTO testResultDTO1 = new TestResultDTO();
        testResultDTO1.setId(1L);
        TestResultDTO testResultDTO2 = new TestResultDTO();
        assertThat(testResultDTO1).isNotEqualTo(testResultDTO2);
        testResultDTO2.setId(testResultDTO1.getId());
        assertThat(testResultDTO1).isEqualTo(testResultDTO2);
        testResultDTO2.setId(2L);
        assertThat(testResultDTO1).isNotEqualTo(testResultDTO2);
        testResultDTO1.setId(null);
        assertThat(testResultDTO1).isNotEqualTo(testResultDTO2);
    }
}
