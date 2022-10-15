package io.partnerize.service.mapper;

import io.partnerize.domain.Company;
import io.partnerize.domain.Contract;
import io.partnerize.domain.Partner;
import io.partnerize.domain.PartnerPayment;
import io.partnerize.service.dto.CompanyDTO;
import io.partnerize.service.dto.ContractDTO;
import io.partnerize.service.dto.PartnerDTO;
import io.partnerize.service.dto.PartnerPaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PartnerPayment} and its DTO {@link PartnerPaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PartnerPaymentMapper extends EntityMapper<PartnerPaymentDTO, PartnerPayment> {
    @Mapping(target = "contract", source = "contract", qualifiedByName = "contractId")
    @Mapping(target = "partner", source = "partner", qualifiedByName = "partnerId")
    @Mapping(target = "company", source = "company", qualifiedByName = "companyId")
    PartnerPaymentDTO toDto(PartnerPayment s);

    @Named("contractId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContractDTO toDtoContractId(Contract contract);

    @Named("partnerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PartnerDTO toDtoPartnerId(Partner partner);

    @Named("companyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyDTO toDtoCompanyId(Company company);
}
