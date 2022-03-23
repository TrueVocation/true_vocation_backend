package com.truevocation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.truevocation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentAnswerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentAnswerDTO.class);
        CommentAnswerDTO commentAnswerDTO1 = new CommentAnswerDTO();
        commentAnswerDTO1.setId(1L);
        CommentAnswerDTO commentAnswerDTO2 = new CommentAnswerDTO();
        assertThat(commentAnswerDTO1).isNotEqualTo(commentAnswerDTO2);
        commentAnswerDTO2.setId(commentAnswerDTO1.getId());
        assertThat(commentAnswerDTO1).isEqualTo(commentAnswerDTO2);
        commentAnswerDTO2.setId(2L);
        assertThat(commentAnswerDTO1).isNotEqualTo(commentAnswerDTO2);
        commentAnswerDTO1.setId(null);
        assertThat(commentAnswerDTO1).isNotEqualTo(commentAnswerDTO2);
    }
}
