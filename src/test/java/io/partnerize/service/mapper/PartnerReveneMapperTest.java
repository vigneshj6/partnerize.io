package io.partnerize.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PartnerReveneMapperTest {

    private PartnerReveneMapper partnerReveneMapper;

    @BeforeEach
    public void setUp() {
        partnerReveneMapper = new PartnerReveneMapperImpl();
    }
}
