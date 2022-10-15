package io.partnerize.repository;

import io.partnerize.domain.PaymentEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentEventRepository extends JpaRepository<PaymentEvent, Long> {}
