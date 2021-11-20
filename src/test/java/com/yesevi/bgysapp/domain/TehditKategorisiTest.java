package com.yesevi.bgysapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yesevi.bgysapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TehditKategorisiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TehditKategorisi.class);
        TehditKategorisi tehditKategorisi1 = new TehditKategorisi();
        tehditKategorisi1.setId(1L);
        TehditKategorisi tehditKategorisi2 = new TehditKategorisi();
        tehditKategorisi2.setId(tehditKategorisi1.getId());
        assertThat(tehditKategorisi1).isEqualTo(tehditKategorisi2);
        tehditKategorisi2.setId(2L);
        assertThat(tehditKategorisi1).isNotEqualTo(tehditKategorisi2);
        tehditKategorisi1.setId(null);
        assertThat(tehditKategorisi1).isNotEqualTo(tehditKategorisi2);
    }
}
