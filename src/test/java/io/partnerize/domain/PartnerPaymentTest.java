package io.partnerize.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.partnerize.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartnerPaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartnerPayment.class);
        PartnerPayment partnerPayment1 = new PartnerPayment();
        partnerPayment1.setId(1L);
        PartnerPayment partnerPayment2 = new PartnerPayment();
        partnerPayment2.setId(partnerPayment1.getId());
        assertThat(partnerPayment1).isEqualTo(partnerPayment2);
        partnerPayment2.setId(2L);
        assertThat(partnerPayment1).isNotEqualTo(partnerPayment2);
        partnerPayment1.setId(null);
        assertThat(partnerPayment1).isNotEqualTo(partnerPayment2);
    }
}
