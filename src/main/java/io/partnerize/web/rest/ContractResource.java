package io.partnerize.web.rest;

import io.partnerize.domain.Contract;
import io.partnerize.repository.ContractRepository;
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
 * REST controller for managing {@link io.partnerize.domain.Contract}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ContractResource {

    private final Logger log = LoggerFactory.getLogger(ContractResource.class);

    private static final String ENTITY_NAME = "contract";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractRepository contractRepository;

    public ContractResource(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    /**
     * {@code POST  /contracts} : Create a new contract.
     *
     * @param contract the contract to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contract, or with status {@code 400 (Bad Request)} if the contract has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contracts")
    public ResponseEntity<Contract> createContract(@RequestBody Contract contract) throws URISyntaxException {
        log.debug("REST request to save Contract : {}", contract);
        if (contract.getId() != null) {
            throw new BadRequestAlertException("A new contract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Contract result = contractRepository.save(contract);
        return ResponseEntity
            .created(new URI("/api/contracts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contracts/:id} : Updates an existing contract.
     *
     * @param id the id of the contract to save.
     * @param contract the contract to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contract,
     * or with status {@code 400 (Bad Request)} if the contract is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contract couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contracts/{id}")
    public ResponseEntity<Contract> updateContract(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Contract contract
    ) throws URISyntaxException {
        log.debug("REST request to update Contract : {}, {}", id, contract);
        if (contract.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contract.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Contract result = contractRepository.save(contract);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contract.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contracts/:id} : Partial updates given fields of an existing contract, field will ignore if it is null
     *
     * @param id the id of the contract to save.
     * @param contract the contract to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contract,
     * or with status {@code 400 (Bad Request)} if the contract is not valid,
     * or with status {@code 404 (Not Found)} if the contract is not found,
     * or with status {@code 500 (Internal Server Error)} if the contract couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contracts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Contract> partialUpdateContract(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Contract contract
    ) throws URISyntaxException {
        log.debug("REST request to partial update Contract partially : {}, {}", id, contract);
        if (contract.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contract.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Contract> result = contractRepository
            .findById(contract.getId())
            .map(existingContract -> {
                if (contract.getName() != null) {
                    existingContract.setName(contract.getName());
                }
                if (contract.getType() != null) {
                    existingContract.setType(contract.getType());
                }
                if (contract.getStartAt() != null) {
                    existingContract.setStartAt(contract.getStartAt());
                }
                if (contract.getEndAt() != null) {
                    existingContract.setEndAt(contract.getEndAt());
                }
                if (contract.getBillingCycle() != null) {
                    existingContract.setBillingCycle(contract.getBillingCycle());
                }
                if (contract.getFixedRate() != null) {
                    existingContract.setFixedRate(contract.getFixedRate());
                }
                if (contract.getCommisionPercent() != null) {
                    existingContract.setCommisionPercent(contract.getCommisionPercent());
                }
                if (contract.getCommisionRate() != null) {
                    existingContract.setCommisionRate(contract.getCommisionRate());
                }

                return existingContract;
            })
            .map(contractRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contract.getId().toString())
        );
    }

    /**
     * {@code GET  /contracts} : get all the contracts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contracts in body.
     */
    @GetMapping("/contracts")
    public List<Contract> getAllContracts() {
        log.debug("REST request to get all Contracts");
        return contractRepository.findAll();
    }

    /**
     * {@code GET  /contracts/:id} : get the "id" contract.
     *
     * @param id the id of the contract to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contract, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contracts/{id}")
    public ResponseEntity<Contract> getContract(@PathVariable Long id) {
        log.debug("REST request to get Contract : {}", id);
        Optional<Contract> contract = contractRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contract);
    }

    /**
     * {@code DELETE  /contracts/:id} : delete the "id" contract.
     *
     * @param id the id of the contract to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contracts/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        log.debug("REST request to delete Contract : {}", id);
        contractRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
