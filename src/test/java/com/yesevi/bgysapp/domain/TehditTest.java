package com.yesevi.bgysapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yesevi.bgysapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TehditTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tehdit.class);
        Tehdit tehdit1 = new Tehdit();
        tehdit1.setId(1L);
        Tehdit tehdit2 = new Tehdit();
        tehdit2.setId(tehdit1.getId());
        assertThat(tehdit1).isEqualTo(tehdit2);
        tehdit2.setId(2L);
        assertThat(tehdit1).isNotEqualTo(tehdit2);
        tehdit1.setId(null);
        assertThat(tehdit1).isNotEqualTo(tehdit2);
    }
}
