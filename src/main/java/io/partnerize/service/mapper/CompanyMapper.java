package io.partnerize.service.mapper;

import io.partnerize.domain.Card;
import io.partnerize.domain.Company;
import io.partnerize.service.dto.CardDTO;
import io.partnerize.service.dto.CompanyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {
    @Mapping(target = "card", source = "card", qualifiedByName = "cardId")
    CompanyDTO toDto(Company s);

    @Named("cardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CardDTO toDtoCardId(Card card);
}
