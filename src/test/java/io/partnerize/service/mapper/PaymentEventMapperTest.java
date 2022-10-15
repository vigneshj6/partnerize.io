package io.partnerize.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentEventMapperTest {

    private PaymentEventMapper paymentEventMapper;

    @BeforeEach
    public void setUp() {
        paymentEventMapper = new PaymentEventMapperImpl();
    }
}
