package io.partnerize.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.partnerize.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentEventDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentEventDTO.class);
        PaymentEventDTO paymentEventDTO1 = new PaymentEventDTO();
        paymentEventDTO1.setId(1L);
        PaymentEventDTO paymentEventDTO2 = new PaymentEventDTO();
        assertThat(paymentEventDTO1).isNotEqualTo(paymentEventDTO2);
        paymentEventDTO2.setId(paymentEventDTO1.getId());
        assertThat(paymentEventDTO1).isEqualTo(paymentEventDTO2);
        paymentEventDTO2.setId(2L);
        assertThat(paymentEventDTO1).isNotEqualTo(paymentEventDTO2);
        paymentEventDTO1.setId(null);
        assertThat(paymentEventDTO1).isNotEqualTo(paymentEventDTO2);
    }
}
