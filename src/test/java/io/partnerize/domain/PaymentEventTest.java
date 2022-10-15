package io.partnerize.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.partnerize.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentEvent.class);
        PaymentEvent paymentEvent1 = new PaymentEvent();
        paymentEvent1.setId(1L);
        PaymentEvent paymentEvent2 = new PaymentEvent();
        paymentEvent2.setId(paymentEvent1.getId());
        assertThat(paymentEvent1).isEqualTo(paymentEvent2);
        paymentEvent2.setId(2L);
        assertThat(paymentEvent1).isNotEqualTo(paymentEvent2);
        paymentEvent1.setId(null);
        assertThat(paymentEvent1).isNotEqualTo(paymentEvent2);
    }
}
