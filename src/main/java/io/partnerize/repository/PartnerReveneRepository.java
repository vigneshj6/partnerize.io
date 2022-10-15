package io.partnerize.repository;

import io.partnerize.domain.PartnerRevene;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PartnerRevene entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartnerReveneRepository extends JpaRepository<PartnerRevene, Long> {}
