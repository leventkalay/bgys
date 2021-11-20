package com.yesevi.bgysapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yesevi.bgysapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BirimTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Birim.class);
        Birim birim1 = new Birim();
        birim1.setId(1L);
        Birim birim2 = new Birim();
        birim2.setId(birim1.getId());
        assertThat(birim1).isEqualTo(birim2);
        birim2.setId(2L);
        assertThat(birim1).isNotEqualTo(birim2);
        birim1.setId(null);
        assertThat(birim1).isNotEqualTo(birim2);
    }
}
