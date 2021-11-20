package com.yesevi.bgysapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yesevi.bgysapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AksiyonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Aksiyon.class);
        Aksiyon aksiyon1 = new Aksiyon();
        aksiyon1.setId(1L);
        Aksiyon aksiyon2 = new Aksiyon();
        aksiyon2.setId(aksiyon1.getId());
        assertThat(aksiyon1).isEqualTo(aksiyon2);
        aksiyon2.setId(2L);
        assertThat(aksiyon1).isNotEqualTo(aksiyon2);
        aksiyon1.setId(null);
        assertThat(aksiyon1).isNotEqualTo(aksiyon2);
    }
}
