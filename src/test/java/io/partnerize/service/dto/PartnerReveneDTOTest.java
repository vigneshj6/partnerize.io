package io.partnerize.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.partnerize.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartnerReveneDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartnerReveneDTO.class);
        PartnerReveneDTO partnerReveneDTO1 = new PartnerReveneDTO();
        partnerReveneDTO1.setId(1L);
        PartnerReveneDTO partnerReveneDTO2 = new PartnerReveneDTO();
        assertThat(partnerReveneDTO1).isNotEqualTo(partnerReveneDTO2);
        partnerReveneDTO2.setId(partnerReveneDTO1.getId());
        assertThat(partnerReveneDTO1).isEqualTo(partnerReveneDTO2);
        partnerReveneDTO2.setId(2L);
        assertThat(partnerReveneDTO1).isNotEqualTo(partnerReveneDTO2);
        partnerReveneDTO1.setId(null);
        assertThat(partnerReveneDTO1).isNotEqualTo(partnerReveneDTO2);
    }
}
