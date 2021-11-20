package com.yesevi.bgysapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yesevi.bgysapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VarlikTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Varlik.class);
        Varlik varlik1 = new Varlik();
        varlik1.setId(1L);
        Varlik varlik2 = new Varlik();
        varlik2.setId(varlik1.getId());
        assertThat(varlik1).isEqualTo(varlik2);
        varlik2.setId(2L);
        assertThat(varlik1).isNotEqualTo(varlik2);
        varlik1.setId(null);
        assertThat(varlik1).isNotEqualTo(varlik2);
    }
}
