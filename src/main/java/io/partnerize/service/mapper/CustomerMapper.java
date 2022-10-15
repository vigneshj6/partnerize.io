package io.partnerize.service.mapper;

import io.partnerize.domain.Company;
import io.partnerize.domain.Customer;
import io.partnerize.domain.Partner;
import io.partnerize.service.dto.CompanyDTO;
import io.partnerize.service.dto.CustomerDTO;
import io.partnerize.service.dto.PartnerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(target = "partner", source = "partner", qualifiedByName = "partnerId")
    @Mapping(target = "company", source = "company", qualifiedByName = "companyId")
    CustomerDTO toDto(Customer s);

    @Named("partnerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PartnerDTO toDtoPartnerId(Partner partner);

    @Named("companyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyDTO toDtoCompanyId(Company company);
}
