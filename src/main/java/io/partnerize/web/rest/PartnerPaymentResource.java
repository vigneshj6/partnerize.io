package io.partnerize.web.rest;

import io.partnerize.domain.PartnerPayment;
import io.partnerize.repository.PartnerPaymentRepository;
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
 * REST controller for managing {@link io.partnerize.domain.PartnerPayment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PartnerPaymentResource {

    private final Logger log = LoggerFactory.getLogger(PartnerPaymentResource.class);

    private static final String ENTITY_NAME = "partnerPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartnerPaymentRepository partnerPaymentRepository;

    public PartnerPaymentResource(PartnerPaymentRepository partnerPaymentRepository) {
        this.partnerPaymentRepository = partnerPaymentRepository;
    }

    /**
     * {@code POST  /partner-payments} : Create a new partnerPayment.
     *
     * @param partnerPayment the partnerPayment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partnerPayment, or with status {@code 400 (Bad Request)} if the partnerPayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/partner-payments")
    public ResponseEntity<PartnerPayment> createPartnerPayment(@RequestBody PartnerPayment partnerPayment) throws URISyntaxException {
        log.debug("REST request to save PartnerPayment : {}", partnerPayment);
        if (partnerPayment.getId() != null) {
            throw new BadRequestAlertException("A new partnerPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PartnerPayment result = partnerPaymentRepository.save(partnerPayment);
        return ResponseEntity
            .created(new URI("/api/partner-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /partner-payments/:id} : Updates an existing partnerPayment.
     *
     * @param id the id of the partnerPayment to save.
     * @param partnerPayment the partnerPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partnerPayment,
     * or with status {@code 400 (Bad Request)} if the partnerPayment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partnerPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/partner-payments/{id}")
    public ResponseEntity<PartnerPayment> updatePartnerPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PartnerPayment partnerPayment
    ) throws URISyntaxException {
        log.debug("REST request to update PartnerPayment : {}, {}", id, partnerPayment);
        if (partnerPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partnerPayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partnerPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PartnerPayment result = partnerPaymentRepository.save(partnerPayment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partnerPayment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /partner-payments/:id} : Partial updates given fields of an existing partnerPayment, field will ignore if it is null
     *
     * @param id the id of the partnerPayment to save.
     * @param partnerPayment the partnerPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partnerPayment,
     * or with status {@code 400 (Bad Request)} if the partnerPayment is not valid,
     * or with status {@code 404 (Not Found)} if the partnerPayment is not found,
     * or with status {@code 500 (Internal Server Error)} if the partnerPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/partner-payments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartnerPayment> partialUpdatePartnerPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PartnerPayment partnerPayment
    ) throws URISyntaxException {
        log.debug("REST request to partial update PartnerPayment partially : {}, {}", id, partnerPayment);
        if (partnerPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partnerPayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partnerPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartnerPayment> result = partnerPaymentRepository
            .findById(partnerPayment.getId())
            .map(existingPartnerPayment -> {
                if (partnerPayment.getInvoice() != null) {
                    existingPartnerPayment.setInvoice(partnerPayment.getInvoice());
                }
                if (partnerPayment.getTotalAmount() != null) {
                    existingPartnerPayment.setTotalAmount(partnerPayment.getTotalAmount());
                }
                if (partnerPayment.getTax() != null) {
                    existingPartnerPayment.setTax(partnerPayment.getTax());
                }
                if (partnerPayment.getOn() != null) {
                    existingPartnerPayment.setOn(partnerPayment.getOn());
                }
                if (partnerPayment.getStatus() != null) {
                    existingPartnerPayment.setStatus(partnerPayment.getStatus());
                }
                if (partnerPayment.getReason() != null) {
                    existingPartnerPayment.setReason(partnerPayment.getReason());
                }

                return existingPartnerPayment;
            })
            .map(partnerPaymentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partnerPayment.getId().toString())
        );
    }

    /**
     * {@code GET  /partner-payments} : get all the partnerPayments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of partnerPayments in body.
     */
    @GetMapping("/partner-payments")
    public List<PartnerPayment> getAllPartnerPayments() {
        log.debug("REST request to get all PartnerPayments");
        return partnerPaymentRepository.findAll();
    }

    /**
     * {@code GET  /partner-payments/:id} : get the "id" partnerPayment.
     *
     * @param id the id of the partnerPayment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partnerPayment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/partner-payments/{id}")
    public ResponseEntity<PartnerPayment> getPartnerPayment(@PathVariable Long id) {
        log.debug("REST request to get PartnerPayment : {}", id);
        Optional<PartnerPayment> partnerPayment = partnerPaymentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(partnerPayment);
    }

    /**
     * {@code DELETE  /partner-payments/:id} : delete the "id" partnerPayment.
     *
     * @param id the id of the partnerPayment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/partner-payments/{id}")
    public ResponseEntity<Void> deletePartnerPayment(@PathVariable Long id) {
        log.debug("REST request to delete PartnerPayment : {}", id);
        partnerPaymentRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
