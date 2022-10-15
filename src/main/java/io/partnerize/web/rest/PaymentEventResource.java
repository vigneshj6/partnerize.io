package io.partnerize.web.rest;

import io.partnerize.domain.PaymentEvent;
import io.partnerize.repository.PaymentEventRepository;
import io.partnerize.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.partnerize.domain.PaymentEvent}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PaymentEventResource {

    private final Logger log = LoggerFactory.getLogger(PaymentEventResource.class);

    private static final String ENTITY_NAME = "paymentEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentEventRepository paymentEventRepository;

    public PaymentEventResource(PaymentEventRepository paymentEventRepository) {
        this.paymentEventRepository = paymentEventRepository;
    }

    /**
     * {@code POST  /payment-events} : Create a new paymentEvent.
     *
     * @param paymentEvent the paymentEvent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentEvent, or with status {@code 400 (Bad Request)} if the paymentEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-events")
    public ResponseEntity<PaymentEvent> createPaymentEvent(@RequestBody PaymentEvent paymentEvent) throws URISyntaxException {
        log.debug("REST request to save PaymentEvent : {}", paymentEvent);
        if (paymentEvent.getId() != null) {
            throw new BadRequestAlertException("A new paymentEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentEvent result = paymentEventRepository.save(paymentEvent);
        return ResponseEntity
            .created(new URI("/api/payment-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-events/:id} : Updates an existing paymentEvent.
     *
     * @param id the id of the paymentEvent to save.
     * @param paymentEvent the paymentEvent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentEvent,
     * or with status {@code 400 (Bad Request)} if the paymentEvent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentEvent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-events/{id}")
    public ResponseEntity<PaymentEvent> updatePaymentEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentEvent paymentEvent
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentEvent : {}, {}", id, paymentEvent);
        if (paymentEvent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentEvent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentEvent result = paymentEventRepository.save(paymentEvent);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentEvent.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /payment-events/:id} : Partial updates given fields of an existing paymentEvent, field will ignore if it is null
     *
     * @param id the id of the paymentEvent to save.
     * @param paymentEvent the paymentEvent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentEvent,
     * or with status {@code 400 (Bad Request)} if the paymentEvent is not valid,
     * or with status {@code 404 (Not Found)} if the paymentEvent is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentEvent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payment-events/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentEvent> partialUpdatePaymentEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentEvent paymentEvent
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentEvent partially : {}, {}", id, paymentEvent);
        if (paymentEvent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentEvent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentEvent> result = paymentEventRepository
            .findById(paymentEvent.getId())
            .map(existingPaymentEvent -> {
                if (paymentEvent.getStatus() != null) {
                    existingPaymentEvent.setStatus(paymentEvent.getStatus());
                }
                if (paymentEvent.getReason() != null) {
                    existingPaymentEvent.setReason(paymentEvent.getReason());
                }
                if (paymentEvent.getOn() != null) {
                    existingPaymentEvent.setOn(paymentEvent.getOn());
                }
                if (paymentEvent.getTotalAmount() != null) {
                    existingPaymentEvent.setTotalAmount(paymentEvent.getTotalAmount());
                }
                if (paymentEvent.getInvoice() != null) {
                    existingPaymentEvent.setInvoice(paymentEvent.getInvoice());
                }

                return existingPaymentEvent;
            })
            .map(paymentEventRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentEvent.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-events} : get all the paymentEvents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentEvents in body.
     */
    @GetMapping("/payment-events")
    public List<PaymentEvent> getAllPaymentEvents() {
        log.debug("REST request to get all PaymentEvents");
        return paymentEventRepository.findAll();
    }

    /**
     * {@code GET  /payment-events/:id} : get the "id" paymentEvent.
     *
     * @param id the id of the paymentEvent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentEvent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-events/{id}")
    public ResponseEntity<PaymentEvent> getPaymentEvent(@PathVariable Long id) {
        log.debug("REST request to get PaymentEvent : {}", id);
        Optional<PaymentEvent> paymentEvent = paymentEventRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(paymentEvent);
    }

    /**
     * {@code DELETE  /payment-events/:id} : delete the "id" paymentEvent.
     *
     * @param id the id of the paymentEvent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-events/{id}")
    public ResponseEntity<Void> deletePaymentEvent(@PathVariable Long id) {
        log.debug("REST request to delete PaymentEvent : {}", id);
        paymentEventRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
