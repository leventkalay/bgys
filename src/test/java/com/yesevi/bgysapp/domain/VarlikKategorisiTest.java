package com.yesevi.bgysapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yesevi.bgysapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VarlikKategorisiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VarlikKategorisi.class);
        VarlikKategorisi varlikKategorisi1 = new VarlikKategorisi();
        varlikKategorisi1.setId(1L);
        VarlikKategorisi varlikKategorisi2 = new VarlikKategorisi();
        varlikKategorisi2.setId(varlikKategorisi1.getId());
        assertThat(varlikKategorisi1).isEqualTo(varlikKategorisi2);
        varlikKategorisi2.setId(2L);
        assertThat(varlikKategorisi1).isNotEqualTo(varlikKategorisi2);
        varlikKategorisi1.setId(null);
        assertThat(varlikKategorisi1).isNotEqualTo(varlikKategorisi2);
    }
}
