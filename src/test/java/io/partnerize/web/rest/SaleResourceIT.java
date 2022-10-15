package io.partnerize.web.rest;

import static io.partnerize.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.partnerize.IntegrationTest;
import io.partnerize.domain.Sale;
import io.partnerize.repository.SaleRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SaleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SaleResourceIT {

    private static final String DEFAULT_SALE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SALE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_INVOICE = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TAX = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAX = new BigDecimal(2);

    private static final String DEFAULT_COUPON_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUPON_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_COUPON_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_COUPON_AMOUNT = new BigDecimal(2);

    private static final Instant DEFAULT_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/sales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSaleMockMvc;

    private Sale sale;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sale createEntity(EntityManager em) {
        Sale sale = new Sale()
            .saleId(DEFAULT_SALE_ID)
            .invoice(DEFAULT_INVOICE)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .tax(DEFAULT_TAX)
            .couponCode(DEFAULT_COUPON_CODE)
            .couponAmount(DEFAULT_COUPON_AMOUNT)
            .on(DEFAULT_ON);
        return sale;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sale createUpdatedEntity(EntityManager em) {
        Sale sale = new Sale()
            .saleId(UPDATED_SALE_ID)
            .invoice(UPDATED_INVOICE)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .tax(UPDATED_TAX)
            .couponCode(UPDATED_COUPON_CODE)
            .couponAmount(UPDATED_COUPON_AMOUNT)
            .on(UPDATED_ON);
        return sale;
    }

    @BeforeEach
    public void initTest() {
        sale = createEntity(em);
    }

    @Test
    @Transactional
    void createSale() throws Exception {
        int databaseSizeBeforeCreate = saleRepository.findAll().size();
        // Create the Sale
        restSaleMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sale))
            )
            .andExpect(status().isCreated());

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll();
        assertThat(saleList).hasSize(databaseSizeBeforeCreate + 1);
        Sale testSale = saleList.get(saleList.size() - 1);
        assertThat(testSale.getSaleId()).isEqualTo(DEFAULT_SALE_ID);
        assertThat(testSale.getInvoice()).isEqualTo(DEFAULT_INVOICE);
        assertThat(testSale.getTotalAmount()).isEqualByComparingTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testSale.getTax()).isEqualByComparingTo(DEFAULT_TAX);
        assertThat(testSale.getCouponCode()).isEqualTo(DEFAULT_COUPON_CODE);
        assertThat(testSale.getCouponAmount()).isEqualByComparingTo(DEFAULT_COUPON_AMOUNT);
        assertThat(testSale.getOn()).isEqualTo(DEFAULT_ON);
    }

    @Test
    @Transactional
    void createSaleWithExistingId() throws Exception {
        // Create the Sale with an existing ID
        sale.setId(1L);

        int databaseSizeBeforeCreate = saleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaleMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sale))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll();
        assertThat(saleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSales() throws Exception {
        // Initialize the database
        saleRepository.saveAndFlush(sale);

        // Get all the saleList
        restSaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sale.getId().intValue())))
            .andExpect(jsonPath("$.[*].saleId").value(hasItem(DEFAULT_SALE_ID)))
            .andExpect(jsonPath("$.[*].invoice").value(hasItem(DEFAULT_INVOICE)))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(sameNumber(DEFAULT_TAX))))
            .andExpect(jsonPath("$.[*].couponCode").value(hasItem(DEFAULT_COUPON_CODE)))
            .andExpect(jsonPath("$.[*].couponAmount").value(hasItem(sameNumber(DEFAULT_COUPON_AMOUNT))))
            .andExpect(jsonPath("$.[*].on").value(hasItem(DEFAULT_ON.toString())));
    }

    @Test
    @Transactional
    void getSale() throws Exception {
        // Initialize the database
        saleRepository.saveAndFlush(sale);

        // Get the sale
        restSaleMockMvc
            .perform(get(ENTITY_API_URL_ID, sale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sale.getId().intValue()))
            .andExpect(jsonPath("$.saleId").value(DEFAULT_SALE_ID))
            .andExpect(jsonPath("$.invoice").value(DEFAULT_INVOICE))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .andExpect(jsonPath("$.tax").value(sameNumber(DEFAULT_TAX)))
            .andExpect(jsonPath("$.couponCode").value(DEFAULT_COUPON_CODE))
            .andExpect(jsonPath("$.couponAmount").value(sameNumber(DEFAULT_COUPON_AMOUNT)))
            .andExpect(jsonPath("$.on").value(DEFAULT_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSale() throws Exception {
        // Get the sale
        restSaleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSale() throws Exception {
        // Initialize the database
        saleRepository.saveAndFlush(sale);

        int databaseSizeBeforeUpdate = saleRepository.findAll().size();

        // Update the sale
        Sale updatedSale = saleRepository.findById(sale.getId()).get();
        // Disconnect from session so that the updates on updatedSale are not directly saved in db
        em.detach(updatedSale);
        updatedSale
            .saleId(UPDATED_SALE_ID)
            .invoice(UPDATED_INVOICE)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .tax(UPDATED_TAX)
            .couponCode(UPDATED_COUPON_CODE)
            .couponAmount(UPDATED_COUPON_AMOUNT)
            .on(UPDATED_ON);

        restSaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSale.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSale))
            )
            .andExpect(status().isOk());

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
        Sale testSale = saleList.get(saleList.size() - 1);
        assertThat(testSale.getSaleId()).isEqualTo(UPDATED_SALE_ID);
        assertThat(testSale.getInvoice()).isEqualTo(UPDATED_INVOICE);
        assertThat(testSale.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testSale.getTax()).isEqualByComparingTo(UPDATED_TAX);
        assertThat(testSale.getCouponCode()).isEqualTo(UPDATED_COUPON_CODE);
        assertThat(testSale.getCouponAmount()).isEqualByComparingTo(UPDATED_COUPON_AMOUNT);
        assertThat(testSale.getOn()).isEqualTo(UPDATED_ON);
    }

    @Test
    @Transactional
    void putNonExistingSale() throws Exception {
        int databaseSizeBeforeUpdate = saleRepository.findAll().size();
        sale.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sale.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sale))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSale() throws Exception {
        int databaseSizeBeforeUpdate = saleRepository.findAll().size();
        sale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sale))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSale() throws Exception {
        int databaseSizeBeforeUpdate = saleRepository.findAll().size();
        sale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaleMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sale))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSaleWithPatch() throws Exception {
        // Initialize the database
        saleRepository.saveAndFlush(sale);

        int databaseSizeBeforeUpdate = saleRepository.findAll().size();

        // Update the sale using partial update
        Sale partialUpdatedSale = new Sale();
        partialUpdatedSale.setId(sale.getId());

        partialUpdatedSale
            .invoice(UPDATED_INVOICE)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .tax(UPDATED_TAX)
            .couponCode(UPDATED_COUPON_CODE)
            .couponAmount(UPDATED_COUPON_AMOUNT)
            .on(UPDATED_ON);

        restSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSale.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSale))
            )
            .andExpect(status().isOk());

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
        Sale testSale = saleList.get(saleList.size() - 1);
        assertThat(testSale.getSaleId()).isEqualTo(DEFAULT_SALE_ID);
        assertThat(testSale.getInvoice()).isEqualTo(UPDATED_INVOICE);
        assertThat(testSale.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testSale.getTax()).isEqualByComparingTo(UPDATED_TAX);
        assertThat(testSale.getCouponCode()).isEqualTo(UPDATED_COUPON_CODE);
        assertThat(testSale.getCouponAmount()).isEqualByComparingTo(UPDATED_COUPON_AMOUNT);
        assertThat(testSale.getOn()).isEqualTo(UPDATED_ON);
    }

    @Test
    @Transactional
    void fullUpdateSaleWithPatch() throws Exception {
        // Initialize the database
        saleRepository.saveAndFlush(sale);

        int databaseSizeBeforeUpdate = saleRepository.findAll().size();

        // Update the sale using partial update
        Sale partialUpdatedSale = new Sale();
        partialUpdatedSale.setId(sale.getId());

        partialUpdatedSale
            .saleId(UPDATED_SALE_ID)
            .invoice(UPDATED_INVOICE)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .tax(UPDATED_TAX)
            .couponCode(UPDATED_COUPON_CODE)
            .couponAmount(UPDATED_COUPON_AMOUNT)
            .on(UPDATED_ON);

        restSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSale.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSale))
            )
            .andExpect(status().isOk());

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
        Sale testSale = saleList.get(saleList.size() - 1);
        assertThat(testSale.getSaleId()).isEqualTo(UPDATED_SALE_ID);
        assertThat(testSale.getInvoice()).isEqualTo(UPDATED_INVOICE);
        assertThat(testSale.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testSale.getTax()).isEqualByComparingTo(UPDATED_TAX);
        assertThat(testSale.getCouponCode()).isEqualTo(UPDATED_COUPON_CODE);
        assertThat(testSale.getCouponAmount()).isEqualByComparingTo(UPDATED_COUPON_AMOUNT);
        assertThat(testSale.getOn()).isEqualTo(UPDATED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingSale() throws Exception {
        int databaseSizeBeforeUpdate = saleRepository.findAll().size();
        sale.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sale.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sale))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSale() throws Exception {
        int databaseSizeBeforeUpdate = saleRepository.findAll().size();
        sale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sale))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSale() throws Exception {
        int databaseSizeBeforeUpdate = saleRepository.findAll().size();
        sale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sale))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSale() throws Exception {
        // Initialize the database
        saleRepository.saveAndFlush(sale);

        int databaseSizeBeforeDelete = saleRepository.findAll().size();

        // Delete the sale
        restSaleMockMvc
            .perform(delete(ENTITY_API_URL_ID, sale.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sale> saleList = saleRepository.findAll();
        assertThat(saleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
