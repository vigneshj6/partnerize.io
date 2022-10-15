package io.partnerize.web.rest;

import io.partnerize.domain.Sale;
import io.partnerize.repository.SaleRepository;
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
 * REST controller for managing {@link io.partnerize.domain.Sale}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SaleResource {

    private final Logger log = LoggerFactory.getLogger(SaleResource.class);

    private static final String ENTITY_NAME = "sale";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SaleRepository saleRepository;

    public SaleResource(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    /**
     * {@code POST  /sales} : Create a new sale.
     *
     * @param sale the sale to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sale, or with status {@code 400 (Bad Request)} if the sale has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sales")
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale) throws URISyntaxException {
        log.debug("REST request to save Sale : {}", sale);
        if (sale.getId() != null) {
            throw new BadRequestAlertException("A new sale cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sale result = saleRepository.save(sale);
        return ResponseEntity
            .created(new URI("/api/sales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sales/:id} : Updates an existing sale.
     *
     * @param id the id of the sale to save.
     * @param sale the sale to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sale,
     * or with status {@code 400 (Bad Request)} if the sale is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sale couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sales/{id}")
    public ResponseEntity<Sale> updateSale(@PathVariable(value = "id", required = false) final Long id, @RequestBody Sale sale)
        throws URISyntaxException {
        log.debug("REST request to update Sale : {}, {}", id, sale);
        if (sale.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sale.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sale result = saleRepository.save(sale);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sale.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sales/:id} : Partial updates given fields of an existing sale, field will ignore if it is null
     *
     * @param id the id of the sale to save.
     * @param sale the sale to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sale,
     * or with status {@code 400 (Bad Request)} if the sale is not valid,
     * or with status {@code 404 (Not Found)} if the sale is not found,
     * or with status {@code 500 (Internal Server Error)} if the sale couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sales/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Sale> partialUpdateSale(@PathVariable(value = "id", required = false) final Long id, @RequestBody Sale sale)
        throws URISyntaxException {
        log.debug("REST request to partial update Sale partially : {}, {}", id, sale);
        if (sale.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sale.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Sale> result = saleRepository
            .findById(sale.getId())
            .map(existingSale -> {
                if (sale.getSaleId() != null) {
                    existingSale.setSaleId(sale.getSaleId());
                }
                if (sale.getInvoice() != null) {
                    existingSale.setInvoice(sale.getInvoice());
                }
                if (sale.getTotalAmount() != null) {
                    existingSale.setTotalAmount(sale.getTotalAmount());
                }
                if (sale.getTax() != null) {
                    existingSale.setTax(sale.getTax());
                }
                if (sale.getCouponCode() != null) {
                    existingSale.setCouponCode(sale.getCouponCode());
                }
                if (sale.getCouponAmount() != null) {
                    existingSale.setCouponAmount(sale.getCouponAmount());
                }
                if (sale.getOn() != null) {
                    existingSale.setOn(sale.getOn());
                }

                return existingSale;
            })
            .map(saleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sale.getId().toString())
        );
    }

    /**
     * {@code GET  /sales} : get all the sales.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sales in body.
     */
    @GetMapping("/sales")
    public List<Sale> getAllSales() {
        log.debug("REST request to get all Sales");
        return saleRepository.findAll();
    }

    /**
     * {@code GET  /sales/:id} : get the "id" sale.
     *
     * @param id the id of the sale to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sale, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sales/{id}")
    public ResponseEntity<Sale> getSale(@PathVariable Long id) {
        log.debug("REST request to get Sale : {}", id);
        Optional<Sale> sale = saleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sale);
    }

    /**
     * {@code DELETE  /sales/:id} : delete the "id" sale.
     *
     * @param id the id of the sale to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sales/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        log.debug("REST request to delete Sale : {}", id);
        saleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
