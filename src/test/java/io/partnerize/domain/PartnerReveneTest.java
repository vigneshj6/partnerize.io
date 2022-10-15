package io.partnerize.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.partnerize.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartnerReveneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartnerRevene.class);
        PartnerRevene partnerRevene1 = new PartnerRevene();
        partnerRevene1.setId(1L);
        PartnerRevene partnerRevene2 = new PartnerRevene();
        partnerRevene2.setId(partnerRevene1.getId());
        assertThat(partnerRevene1).isEqualTo(partnerRevene2);
        partnerRevene2.setId(2L);
        assertThat(partnerRevene1).isNotEqualTo(partnerRevene2);
        partnerRevene1.setId(null);
        assertThat(partnerRevene1).isNotEqualTo(partnerRevene2);
    }
}
