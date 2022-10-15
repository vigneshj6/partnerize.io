package io.partnerize.service.mapper;

import io.partnerize.domain.Company;
import io.partnerize.domain.Contract;
import io.partnerize.domain.Currency;
import io.partnerize.domain.Partner;
import io.partnerize.service.dto.CompanyDTO;
import io.partnerize.service.dto.ContractDTO;
import io.partnerize.service.dto.CurrencyDTO;
import io.partnerize.service.dto.PartnerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contract} and its DTO {@link ContractDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContractMapper extends EntityMapper<ContractDTO, Contract> {
    @Mapping(target = "currency", source = "currency", qualifiedByName = "currencyId")
    @Mapping(target = "partner", source = "partner", qualifiedByName = "partnerId")
    @Mapping(target = "company", source = "company", qualifiedByName = "companyId")
    ContractDTO toDto(Contract s);

    @Named("currencyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CurrencyDTO toDtoCurrencyId(Currency currency);

    @Named("partnerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PartnerDTO toDtoPartnerId(Partner partner);

    @Named("companyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyDTO toDtoCompanyId(Company company);
}
