package com.yesevi.bgysapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yesevi.bgysapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SurecTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Surec.class);
        Surec surec1 = new Surec();
        surec1.setId(1L);
        Surec surec2 = new Surec();
        surec2.setId(surec1.getId());
        assertThat(surec1).isEqualTo(surec2);
        surec2.setId(2L);
        assertThat(surec1).isNotEqualTo(surec2);
        surec1.setId(null);
        assertThat(surec1).isNotEqualTo(surec2);
    }
}
