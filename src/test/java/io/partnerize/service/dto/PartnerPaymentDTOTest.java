package io.partnerize.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.partnerize.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartnerPaymentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartnerPaymentDTO.class);
        PartnerPaymentDTO partnerPaymentDTO1 = new PartnerPaymentDTO();
        partnerPaymentDTO1.setId(1L);
        PartnerPaymentDTO partnerPaymentDTO2 = new PartnerPaymentDTO();
        assertThat(partnerPaymentDTO1).isNotEqualTo(partnerPaymentDTO2);
        partnerPaymentDTO2.setId(partnerPaymentDTO1.getId());
        assertThat(partnerPaymentDTO1).isEqualTo(partnerPaymentDTO2);
        partnerPaymentDTO2.setId(2L);
        assertThat(partnerPaymentDTO1).isNotEqualTo(partnerPaymentDTO2);
        partnerPaymentDTO1.setId(null);
        assertThat(partnerPaymentDTO1).isNotEqualTo(partnerPaymentDTO2);
    }
}
