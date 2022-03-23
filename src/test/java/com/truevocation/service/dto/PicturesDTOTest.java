package com.truevocation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.truevocation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PicturesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PicturesDTO.class);
        PicturesDTO picturesDTO1 = new PicturesDTO();
        picturesDTO1.setId(1L);
        PicturesDTO picturesDTO2 = new PicturesDTO();
        assertThat(picturesDTO1).isNotEqualTo(picturesDTO2);
        picturesDTO2.setId(picturesDTO1.getId());
        assertThat(picturesDTO1).isEqualTo(picturesDTO2);
        picturesDTO2.setId(2L);
        assertThat(picturesDTO1).isNotEqualTo(picturesDTO2);
        picturesDTO1.setId(null);
        assertThat(picturesDTO1).isNotEqualTo(picturesDTO2);
    }
}
