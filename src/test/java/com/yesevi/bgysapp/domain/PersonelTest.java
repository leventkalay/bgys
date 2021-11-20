package com.yesevi.bgysapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yesevi.bgysapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Personel.class);
        Personel personel1 = new Personel();
        personel1.setId(1L);
        Personel personel2 = new Personel();
        personel2.setId(personel1.getId());
        assertThat(personel1).isEqualTo(personel2);
        personel2.setId(2L);
        assertThat(personel1).isNotEqualTo(personel2);
        personel1.setId(null);
        assertThat(personel1).isNotEqualTo(personel2);
    }
}
