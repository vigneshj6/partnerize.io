package io.partnerize.web.rest;

import static io.partnerize.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.partnerize.IntegrationTest;
import io.partnerize.domain.PartnerPayment;
import io.partnerize.domain.enumeration.PaymentStatus;
import io.partnerize.repository.PartnerPaymentRepository;
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
 * Integration tests for the {@link PartnerPaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PartnerPaymentResourceIT {

    private static final String DEFAULT_INVOICE = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TAX = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAX = new BigDecimal(2);

    private static final Instant DEFAULT_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final PaymentStatus DEFAULT_STATUS = PaymentStatus.COMPLETED;
    private static final PaymentStatus UPDATED_STATUS = PaymentStatus.CREATED;

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/partner-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PartnerPaymentRepository partnerPaymentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartnerPaymentMockMvc;

    private PartnerPayment partnerPayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartnerPayment createEntity(EntityManager em) {
        PartnerPayment partnerPayment = new PartnerPayment()
            .invoice(DEFAULT_INVOICE)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .tax(DEFAULT_TAX)
            .on(DEFAULT_ON)
            .status(DEFAULT_STATUS)
            .reason(DEFAULT_REASON);
        return partnerPayment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartnerPayment createUpdatedEntity(EntityManager em) {
        PartnerPayment partnerPayment = new PartnerPayment()
            .invoice(UPDATED_INVOICE)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .tax(UPDATED_TAX)
            .on(UPDATED_ON)
            .status(UPDATED_STATUS)
            .reason(UPDATED_REASON);
        return partnerPayment;
    }

    @BeforeEach
    public void initTest() {
        partnerPayment = createEntity(em);
    }

    @Test
    @Transactional
    void createPartnerPayment() throws Exception {
        int databaseSizeBeforeCreate = partnerPaymentRepository.findAll().size();
        // Create the PartnerPayment
        restPartnerPaymentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partnerPayment))
            )
            .andExpect(status().isCreated());

        // Validate the PartnerPayment in the database
        List<PartnerPayment> partnerPaymentList = partnerPaymentRepository.findAll();
        assertThat(partnerPaymentList).hasSize(databaseSizeBeforeCreate + 1);
        PartnerPayment testPartnerPayment = partnerPaymentList.get(partnerPaymentList.size() - 1);
        assertThat(testPartnerPayment.getInvoice()).isEqualTo(DEFAULT_INVOICE);
        assertThat(testPartnerPayment.getTotalAmount()).isEqualByComparingTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testPartnerPayment.getTax()).isEqualByComparingTo(DEFAULT_TAX);
        assertThat(testPartnerPayment.getOn()).isEqualTo(DEFAULT_ON);
        assertThat(testPartnerPayment.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPartnerPayment.getReason()).isEqualTo(DEFAULT_REASON);
    }

    @Test
    @Transactional
    void createPartnerPaymentWithExistingId() throws Exception {
        // Create the PartnerPayment with an existing ID
        partnerPayment.setId(1L);

        int databaseSizeBeforeCreate = partnerPaymentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartnerPaymentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partnerPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerPayment in the database
        List<PartnerPayment> partnerPaymentList = partnerPaymentRepository.findAll();
        assertThat(partnerPaymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPartnerPayments() throws Exception {
        // Initialize the database
        partnerPaymentRepository.saveAndFlush(partnerPayment);

        // Get all the partnerPaymentList
        restPartnerPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partnerPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoice").value(hasItem(DEFAULT_INVOICE)))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(sameNumber(DEFAULT_TAX))))
            .andExpect(jsonPath("$.[*].on").value(hasItem(DEFAULT_ON.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)));
    }

    @Test
    @Transactional
    void getPartnerPayment() throws Exception {
        // Initialize the database
        partnerPaymentRepository.saveAndFlush(partnerPayment);

        // Get the partnerPayment
        restPartnerPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, partnerPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partnerPayment.getId().intValue()))
            .andExpect(jsonPath("$.invoice").value(DEFAULT_INVOICE))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .andExpect(jsonPath("$.tax").value(sameNumber(DEFAULT_TAX)))
            .andExpect(jsonPath("$.on").value(DEFAULT_ON.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON));
    }

    @Test
    @Transactional
    void getNonExistingPartnerPayment() throws Exception {
        // Get the partnerPayment
        restPartnerPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPartnerPayment() throws Exception {
        // Initialize the database
        partnerPaymentRepository.saveAndFlush(partnerPayment);

        int databaseSizeBeforeUpdate = partnerPaymentRepository.findAll().size();

        // Update the partnerPayment
        PartnerPayment updatedPartnerPayment = partnerPaymentRepository.findById(partnerPayment.getId()).get();
        // Disconnect from session so that the updates on updatedPartnerPayment are not directly saved in db
        em.detach(updatedPartnerPayment);
        updatedPartnerPayment
            .invoice(UPDATED_INVOICE)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .tax(UPDATED_TAX)
            .on(UPDATED_ON)
            .status(UPDATED_STATUS)
            .reason(UPDATED_REASON);

        restPartnerPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPartnerPayment.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPartnerPayment))
            )
            .andExpect(status().isOk());

        // Validate the PartnerPayment in the database
        List<PartnerPayment> partnerPaymentList = partnerPaymentRepository.findAll();
        assertThat(partnerPaymentList).hasSize(databaseSizeBeforeUpdate);
        PartnerPayment testPartnerPayment = partnerPaymentList.get(partnerPaymentList.size() - 1);
        assertThat(testPartnerPayment.getInvoice()).isEqualTo(UPDATED_INVOICE);
        assertThat(testPartnerPayment.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testPartnerPayment.getTax()).isEqualByComparingTo(UPDATED_TAX);
        assertThat(testPartnerPayment.getOn()).isEqualTo(UPDATED_ON);
        assertThat(testPartnerPayment.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPartnerPayment.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void putNonExistingPartnerPayment() throws Exception {
        int databaseSizeBeforeUpdate = partnerPaymentRepository.findAll().size();
        partnerPayment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartnerPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partnerPayment.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partnerPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerPayment in the database
        List<PartnerPayment> partnerPaymentList = partnerPaymentRepository.findAll();
        assertThat(partnerPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartnerPayment() throws Exception {
        int databaseSizeBeforeUpdate = partnerPaymentRepository.findAll().size();
        partnerPayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partnerPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerPayment in the database
        List<PartnerPayment> partnerPaymentList = partnerPaymentRepository.findAll();
        assertThat(partnerPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartnerPayment() throws Exception {
        int databaseSizeBeforeUpdate = partnerPaymentRepository.findAll().size();
        partnerPayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerPaymentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partnerPayment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartnerPayment in the database
        List<PartnerPayment> partnerPaymentList = partnerPaymentRepository.findAll();
        assertThat(partnerPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartnerPaymentWithPatch() throws Exception {
        // Initialize the database
        partnerPaymentRepository.saveAndFlush(partnerPayment);

        int databaseSizeBeforeUpdate = partnerPaymentRepository.findAll().size();

        // Update the partnerPayment using partial update
        PartnerPayment partialUpdatedPartnerPayment = new PartnerPayment();
        partialUpdatedPartnerPayment.setId(partnerPayment.getId());

        partialUpdatedPartnerPayment.invoice(UPDATED_INVOICE).tax(UPDATED_TAX).reason(UPDATED_REASON);

        restPartnerPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartnerPayment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartnerPayment))
            )
            .andExpect(status().isOk());

        // Validate the PartnerPayment in the database
        List<PartnerPayment> partnerPaymentList = partnerPaymentRepository.findAll();
        assertThat(partnerPaymentList).hasSize(databaseSizeBeforeUpdate);
        PartnerPayment testPartnerPayment = partnerPaymentList.get(partnerPaymentList.size() - 1);
        assertThat(testPartnerPayment.getInvoice()).isEqualTo(UPDATED_INVOICE);
        assertThat(testPartnerPayment.getTotalAmount()).isEqualByComparingTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testPartnerPayment.getTax()).isEqualByComparingTo(UPDATED_TAX);
        assertThat(testPartnerPayment.getOn()).isEqualTo(DEFAULT_ON);
        assertThat(testPartnerPayment.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPartnerPayment.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void fullUpdatePartnerPaymentWithPatch() throws Exception {
        // Initialize the database
        partnerPaymentRepository.saveAndFlush(partnerPayment);

        int databaseSizeBeforeUpdate = partnerPaymentRepository.findAll().size();

        // Update the partnerPayment using partial update
        PartnerPayment partialUpdatedPartnerPayment = new PartnerPayment();
        partialUpdatedPartnerPayment.setId(partnerPayment.getId());

        partialUpdatedPartnerPayment
            .invoice(UPDATED_INVOICE)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .tax(UPDATED_TAX)
            .on(UPDATED_ON)
            .status(UPDATED_STATUS)
            .reason(UPDATED_REASON);

        restPartnerPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartnerPayment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartnerPayment))
            )
            .andExpect(status().isOk());

        // Validate the PartnerPayment in the database
        List<PartnerPayment> partnerPaymentList = partnerPaymentRepository.findAll();
        assertThat(partnerPaymentList).hasSize(databaseSizeBeforeUpdate);
        PartnerPayment testPartnerPayment = partnerPaymentList.get(partnerPaymentList.size() - 1);
        assertThat(testPartnerPayment.getInvoice()).isEqualTo(UPDATED_INVOICE);
        assertThat(testPartnerPayment.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testPartnerPayment.getTax()).isEqualByComparingTo(UPDATED_TAX);
        assertThat(testPartnerPayment.getOn()).isEqualTo(UPDATED_ON);
        assertThat(testPartnerPayment.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPartnerPayment.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void patchNonExistingPartnerPayment() throws Exception {
        int databaseSizeBeforeUpdate = partnerPaymentRepository.findAll().size();
        partnerPayment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartnerPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partnerPayment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partnerPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerPayment in the database
        List<PartnerPayment> partnerPaymentList = partnerPaymentRepository.findAll();
        assertThat(partnerPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartnerPayment() throws Exception {
        int databaseSizeBeforeUpdate = partnerPaymentRepository.findAll().size();
        partnerPayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partnerPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerPayment in the database
        List<PartnerPayment> partnerPaymentList = partnerPaymentRepository.findAll();
        assertThat(partnerPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartnerPayment() throws Exception {
        int databaseSizeBeforeUpdate = partnerPaymentRepository.findAll().size();
        partnerPayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partnerPayment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartnerPayment in the database
        List<PartnerPayment> partnerPaymentList = partnerPaymentRepository.findAll();
        assertThat(partnerPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartnerPayment() throws Exception {
        // Initialize the database
        partnerPaymentRepository.saveAndFlush(partnerPayment);

        int databaseSizeBeforeDelete = partnerPaymentRepository.findAll().size();

        // Delete the partnerPayment
        restPartnerPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, partnerPayment.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PartnerPayment> partnerPaymentList = partnerPaymentRepository.findAll();
        assertThat(partnerPaymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
