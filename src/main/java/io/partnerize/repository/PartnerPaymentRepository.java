package io.partnerize.repository;

import io.partnerize.domain.PartnerPayment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PartnerPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartnerPaymentRepository extends JpaRepository<PartnerPayment, Long> {}
