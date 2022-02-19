package com.truevocation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.truevocation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnswerUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnswerUser.class);
        AnswerUser answerUser1 = new AnswerUser();
        answerUser1.setId(1L);
        AnswerUser answerUser2 = new AnswerUser();
        answerUser2.setId(answerUser1.getId());
        assertThat(answerUser1).isEqualTo(answerUser2);
        answerUser2.setId(2L);
        assertThat(answerUser1).isNotEqualTo(answerUser2);
        answerUser1.setId(null);
        assertThat(answerUser1).isNotEqualTo(answerUser2);
    }
}
