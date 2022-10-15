package io.partnerize.service.mapper;

import io.partnerize.domain.PartnerRevene;
import io.partnerize.service.dto.PartnerReveneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PartnerRevene} and its DTO {@link PartnerReveneDTO}.
 */
@Mapper(componentModel = "spring")
public interface PartnerReveneMapper extends EntityMapper<PartnerReveneDTO, PartnerRevene> {}
