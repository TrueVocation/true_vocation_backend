package com.truevocation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.truevocation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnswerUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnswerUserDTO.class);
        AnswerUserDTO answerUserDTO1 = new AnswerUserDTO();
        answerUserDTO1.setId(1L);
        AnswerUserDTO answerUserDTO2 = new AnswerUserDTO();
        assertThat(answerUserDTO1).isNotEqualTo(answerUserDTO2);
        answerUserDTO2.setId(answerUserDTO1.getId());
        assertThat(answerUserDTO1).isEqualTo(answerUserDTO2);
        answerUserDTO2.setId(2L);
        assertThat(answerUserDTO1).isNotEqualTo(answerUserDTO2);
        answerUserDTO1.setId(null);
        assertThat(answerUserDTO1).isNotEqualTo(answerUserDTO2);
    }
}
