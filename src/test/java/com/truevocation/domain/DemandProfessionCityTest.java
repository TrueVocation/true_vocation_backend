package com.truevocation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.truevocation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemandProfessionCityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandProfessionCity.class);
        DemandProfessionCity demandProfessionCity1 = new DemandProfessionCity();
        demandProfessionCity1.setId(1L);
        DemandProfessionCity demandProfessionCity2 = new DemandProfessionCity();
        demandProfessionCity2.setId(demandProfessionCity1.getId());
        assertThat(demandProfessionCity1).isEqualTo(demandProfessionCity2);
        demandProfessionCity2.setId(2L);
        assertThat(demandProfessionCity1).isNotEqualTo(demandProfessionCity2);
        demandProfessionCity1.setId(null);
        assertThat(demandProfessionCity1).isNotEqualTo(demandProfessionCity2);
    }
}
