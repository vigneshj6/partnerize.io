package io.partnerize.service.mapper;

import io.partnerize.domain.PartnerPayment;
import io.partnerize.domain.PaymentEvent;
import io.partnerize.service.dto.PartnerPaymentDTO;
import io.partnerize.service.dto.PaymentEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentEvent} and its DTO {@link PaymentEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentEventMapper extends EntityMapper<PaymentEventDTO, PaymentEvent> {
    @Mapping(target = "partnerPayment", source = "partnerPayment", qualifiedByName = "partnerPaymentId")
    PaymentEventDTO toDto(PaymentEvent s);

    @Named("partnerPaymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PartnerPaymentDTO toDtoPartnerPaymentId(PartnerPayment partnerPayment);
}
