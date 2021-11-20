package com.yesevi.bgysapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yesevi.bgysapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IsAkisiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IsAkisi.class);
        IsAkisi isAkisi1 = new IsAkisi();
        isAkisi1.setId(1L);
        IsAkisi isAkisi2 = new IsAkisi();
        isAkisi2.setId(isAkisi1.getId());
        assertThat(isAkisi1).isEqualTo(isAkisi2);
        isAkisi2.setId(2L);
        assertThat(isAkisi1).isNotEqualTo(isAkisi2);
        isAkisi1.setId(null);
        assertThat(isAkisi1).isNotEqualTo(isAkisi2);
    }
}
