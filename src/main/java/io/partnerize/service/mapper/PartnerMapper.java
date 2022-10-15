package io.partnerize.service.mapper;

import io.partnerize.domain.Card;
import io.partnerize.domain.Company;
import io.partnerize.domain.Partner;
import io.partnerize.service.dto.CardDTO;
import io.partnerize.service.dto.CompanyDTO;
import io.partnerize.service.dto.PartnerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Partner} and its DTO {@link PartnerDTO}.
 */
@Mapper(componentModel = "spring")
public interface PartnerMapper extends EntityMapper<PartnerDTO, Partner> {
    @Mapping(target = "card", source = "card", qualifiedByName = "cardId")
    @Mapping(target = "company", source = "company", qualifiedByName = "companyId")
    PartnerDTO toDto(Partner s);

    @Named("cardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CardDTO toDtoCardId(Card card);

    @Named("companyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyDTO toDtoCompanyId(Company company);
}
