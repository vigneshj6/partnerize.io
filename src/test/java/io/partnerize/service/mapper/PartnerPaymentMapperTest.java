package io.partnerize.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PartnerPaymentMapperTest {

    private PartnerPaymentMapper partnerPaymentMapper;

    @BeforeEach
    public void setUp() {
        partnerPaymentMapper = new PartnerPaymentMapperImpl();
    }
}
