package io.partnerize.web.rest;

import io.partnerize.domain.PartnerRevene;
import io.partnerize.repository.PartnerReveneRepository;
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
 * REST controller for managing {@link io.partnerize.domain.PartnerRevene}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PartnerReveneResource {

    private final Logger log = LoggerFactory.getLogger(PartnerReveneResource.class);

    private static final String ENTITY_NAME = "partnerRevene";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartnerReveneRepository partnerReveneRepository;

    public PartnerReveneResource(PartnerReveneRepository partnerReveneRepository) {
        this.partnerReveneRepository = partnerReveneRepository;
    }

    /**
     * {@code POST  /partner-revenes} : Create a new partnerRevene.
     *
     * @param partnerRevene the partnerRevene to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partnerRevene, or with status {@code 400 (Bad Request)} if the partnerRevene has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/partner-revenes")
    public ResponseEntity<PartnerRevene> createPartnerRevene(@RequestBody PartnerRevene partnerRevene) throws URISyntaxException {
        log.debug("REST request to save PartnerRevene : {}", partnerRevene);
        if (partnerRevene.getId() != null) {
            throw new BadRequestAlertException("A new partnerRevene cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PartnerRevene result = partnerReveneRepository.save(partnerRevene);
        return ResponseEntity
            .created(new URI("/api/partner-revenes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /partner-revenes/:id} : Updates an existing partnerRevene.
     *
     * @param id the id of the partnerRevene to save.
     * @param partnerRevene the partnerRevene to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partnerRevene,
     * or with status {@code 400 (Bad Request)} if the partnerRevene is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partnerRevene couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/partner-revenes/{id}")
    public ResponseEntity<PartnerRevene> updatePartnerRevene(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PartnerRevene partnerRevene
    ) throws URISyntaxException {
        log.debug("REST request to update PartnerRevene : {}, {}", id, partnerRevene);
        if (partnerRevene.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partnerRevene.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partnerReveneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // no save call needed as we have no fields that can be updated
        PartnerRevene result = partnerRevene;
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partnerRevene.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /partner-revenes/:id} : Partial updates given fields of an existing partnerRevene, field will ignore if it is null
     *
     * @param id the id of the partnerRevene to save.
     * @param partnerRevene the partnerRevene to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partnerRevene,
     * or with status {@code 400 (Bad Request)} if the partnerRevene is not valid,
     * or with status {@code 404 (Not Found)} if the partnerRevene is not found,
     * or with status {@code 500 (Internal Server Error)} if the partnerRevene couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/partner-revenes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartnerRevene> partialUpdatePartnerRevene(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PartnerRevene partnerRevene
    ) throws URISyntaxException {
        log.debug("REST request to partial update PartnerRevene partially : {}, {}", id, partnerRevene);
        if (partnerRevene.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partnerRevene.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partnerReveneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartnerRevene> result = partnerReveneRepository
            .findById(partnerRevene.getId())
            .map(existingPartnerRevene -> {
                return existingPartnerRevene;
            })// .map(partnerReveneRepository::save)
        ;

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partnerRevene.getId().toString())
        );
    }

    /**
     * {@code GET  /partner-revenes} : get all the partnerRevenes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of partnerRevenes in body.
     */
    @GetMapping("/partner-revenes")
    public List<PartnerRevene> getAllPartnerRevenes() {
        log.debug("REST request to get all PartnerRevenes");
        return partnerReveneRepository.findAll();
    }

    /**
     * {@code GET  /partner-revenes/:id} : get the "id" partnerRevene.
     *
     * @param id the id of the partnerRevene to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partnerRevene, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/partner-revenes/{id}")
    public ResponseEntity<PartnerRevene> getPartnerRevene(@PathVariable Long id) {
        log.debug("REST request to get PartnerRevene : {}", id);
        Optional<PartnerRevene> partnerRevene = partnerReveneRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(partnerRevene);
    }

    /**
     * {@code DELETE  /partner-revenes/:id} : delete the "id" partnerRevene.
     *
     * @param id the id of the partnerRevene to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/partner-revenes/{id}")
    public ResponseEntity<Void> deletePartnerRevene(@PathVariable Long id) {
        log.debug("REST request to delete PartnerRevene : {}", id);
        partnerReveneRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
