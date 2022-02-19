package com.truevocation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.truevocation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentAnswerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentAnswer.class);
        CommentAnswer commentAnswer1 = new CommentAnswer();
        commentAnswer1.setId(1L);
        CommentAnswer commentAnswer2 = new CommentAnswer();
        commentAnswer2.setId(commentAnswer1.getId());
        assertThat(commentAnswer1).isEqualTo(commentAnswer2);
        commentAnswer2.setId(2L);
        assertThat(commentAnswer1).isNotEqualTo(commentAnswer2);
        commentAnswer1.setId(null);
        assertThat(commentAnswer1).isNotEqualTo(commentAnswer2);
    }
}
