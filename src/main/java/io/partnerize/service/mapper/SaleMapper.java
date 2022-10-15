package io.partnerize.service.mapper;

import io.partnerize.domain.Company;
import io.partnerize.domain.Currency;
import io.partnerize.domain.Customer;
import io.partnerize.domain.Partner;
import io.partnerize.domain.Sale;
import io.partnerize.service.dto.CompanyDTO;
import io.partnerize.service.dto.CurrencyDTO;
import io.partnerize.service.dto.CustomerDTO;
import io.partnerize.service.dto.PartnerDTO;
import io.partnerize.service.dto.SaleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sale} and its DTO {@link SaleDTO}.
 */
@Mapper(componentModel = "spring")
public interface SaleMapper extends EntityMapper<SaleDTO, Sale> {
    @Mapping(target = "currency", source = "currency", qualifiedByName = "currencyId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "partner", source = "partner", qualifiedByName = "partnerId")
    @Mapping(target = "company", source = "company", qualifiedByName = "companyId")
    SaleDTO toDto(Sale s);

    @Named("currencyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CurrencyDTO toDtoCurrencyId(Currency currency);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);

    @Named("partnerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PartnerDTO toDtoPartnerId(Partner partner);

    @Named("companyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyDTO toDtoCompanyId(Company company);
}
