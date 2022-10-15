package io.partnerize.web.rest;

import static io.partnerize.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.partnerize.IntegrationTest;
import io.partnerize.domain.PaymentEvent;
import io.partnerize.domain.enumeration.PaymentStatus;
import io.partnerize.repository.PaymentEventRepository;
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
 * Integration tests for the {@link PaymentEventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentEventResourceIT {

    private static final PaymentStatus DEFAULT_STATUS = PaymentStatus.COMPLETED;
    private static final PaymentStatus UPDATED_STATUS = PaymentStatus.CREATED;

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final Instant DEFAULT_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_INVOICE = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payment-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentEventRepository paymentEventRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentEventMockMvc;

    private PaymentEvent paymentEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentEvent createEntity(EntityManager em) {
        PaymentEvent paymentEvent = new PaymentEvent()
            .status(DEFAULT_STATUS)
            .reason(DEFAULT_REASON)
            .on(DEFAULT_ON)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .invoice(DEFAULT_INVOICE);
        return paymentEvent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentEvent createUpdatedEntity(EntityManager em) {
        PaymentEvent paymentEvent = new PaymentEvent()
            .status(UPDATED_STATUS)
            .reason(UPDATED_REASON)
            .on(UPDATED_ON)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .invoice(UPDATED_INVOICE);
        return paymentEvent;
    }

    @BeforeEach
    public void initTest() {
        paymentEvent = createEntity(em);
    }

    @Test
    @Transactional
    void createPaymentEvent() throws Exception {
        int databaseSizeBeforeCreate = paymentEventRepository.findAll().size();
        // Create the PaymentEvent
        restPaymentEventMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentEvent))
            )
            .andExpect(status().isCreated());

        // Validate the PaymentEvent in the database
        List<PaymentEvent> paymentEventList = paymentEventRepository.findAll();
        assertThat(paymentEventList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentEvent testPaymentEvent = paymentEventList.get(paymentEventList.size() - 1);
        assertThat(testPaymentEvent.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPaymentEvent.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testPaymentEvent.getOn()).isEqualTo(DEFAULT_ON);
        assertThat(testPaymentEvent.getTotalAmount()).isEqualByComparingTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testPaymentEvent.getInvoice()).isEqualTo(DEFAULT_INVOICE);
    }

    @Test
    @Transactional
    void createPaymentEventWithExistingId() throws Exception {
        // Create the PaymentEvent with an existing ID
        paymentEvent.setId(1L);

        int databaseSizeBeforeCreate = paymentEventRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentEventMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentEvent in the database
        List<PaymentEvent> paymentEventList = paymentEventRepository.findAll();
        assertThat(paymentEventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPaymentEvents() throws Exception {
        // Initialize the database
        paymentEventRepository.saveAndFlush(paymentEvent);

        // Get all the paymentEventList
        restPaymentEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].on").value(hasItem(DEFAULT_ON.toString())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].invoice").value(hasItem(DEFAULT_INVOICE)));
    }

    @Test
    @Transactional
    void getPaymentEvent() throws Exception {
        // Initialize the database
        paymentEventRepository.saveAndFlush(paymentEvent);

        // Get the paymentEvent
        restPaymentEventMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentEvent.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.on").value(DEFAULT_ON.toString()))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .andExpect(jsonPath("$.invoice").value(DEFAULT_INVOICE));
    }

    @Test
    @Transactional
    void getNonExistingPaymentEvent() throws Exception {
        // Get the paymentEvent
        restPaymentEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentEvent() throws Exception {
        // Initialize the database
        paymentEventRepository.saveAndFlush(paymentEvent);

        int databaseSizeBeforeUpdate = paymentEventRepository.findAll().size();

        // Update the paymentEvent
        PaymentEvent updatedPaymentEvent = paymentEventRepository.findById(paymentEvent.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentEvent are not directly saved in db
        em.detach(updatedPaymentEvent);
        updatedPaymentEvent
            .status(UPDATED_STATUS)
            .reason(UPDATED_REASON)
            .on(UPDATED_ON)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .invoice(UPDATED_INVOICE);

        restPaymentEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentEvent.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPaymentEvent))
            )
            .andExpect(status().isOk());

        // Validate the PaymentEvent in the database
        List<PaymentEvent> paymentEventList = paymentEventRepository.findAll();
        assertThat(paymentEventList).hasSize(databaseSizeBeforeUpdate);
        PaymentEvent testPaymentEvent = paymentEventList.get(paymentEventList.size() - 1);
        assertThat(testPaymentEvent.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPaymentEvent.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testPaymentEvent.getOn()).isEqualTo(UPDATED_ON);
        assertThat(testPaymentEvent.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testPaymentEvent.getInvoice()).isEqualTo(UPDATED_INVOICE);
    }

    @Test
    @Transactional
    void putNonExistingPaymentEvent() throws Exception {
        int databaseSizeBeforeUpdate = paymentEventRepository.findAll().size();
        paymentEvent.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentEvent.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentEvent in the database
        List<PaymentEvent> paymentEventList = paymentEventRepository.findAll();
        assertThat(paymentEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentEvent() throws Exception {
        int databaseSizeBeforeUpdate = paymentEventRepository.findAll().size();
        paymentEvent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentEvent in the database
        List<PaymentEvent> paymentEventList = paymentEventRepository.findAll();
        assertThat(paymentEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentEvent() throws Exception {
        int databaseSizeBeforeUpdate = paymentEventRepository.findAll().size();
        paymentEvent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentEventMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentEvent))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentEvent in the database
        List<PaymentEvent> paymentEventList = paymentEventRepository.findAll();
        assertThat(paymentEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentEventWithPatch() throws Exception {
        // Initialize the database
        paymentEventRepository.saveAndFlush(paymentEvent);

        int databaseSizeBeforeUpdate = paymentEventRepository.findAll().size();

        // Update the paymentEvent using partial update
        PaymentEvent partialUpdatedPaymentEvent = new PaymentEvent();
        partialUpdatedPaymentEvent.setId(paymentEvent.getId());

        partialUpdatedPaymentEvent.status(UPDATED_STATUS).reason(UPDATED_REASON).invoice(UPDATED_INVOICE);

        restPaymentEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentEvent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentEvent))
            )
            .andExpect(status().isOk());

        // Validate the PaymentEvent in the database
        List<PaymentEvent> paymentEventList = paymentEventRepository.findAll();
        assertThat(paymentEventList).hasSize(databaseSizeBeforeUpdate);
        PaymentEvent testPaymentEvent = paymentEventList.get(paymentEventList.size() - 1);
        assertThat(testPaymentEvent.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPaymentEvent.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testPaymentEvent.getOn()).isEqualTo(DEFAULT_ON);
        assertThat(testPaymentEvent.getTotalAmount()).isEqualByComparingTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testPaymentEvent.getInvoice()).isEqualTo(UPDATED_INVOICE);
    }

    @Test
    @Transactional
    void fullUpdatePaymentEventWithPatch() throws Exception {
        // Initialize the database
        paymentEventRepository.saveAndFlush(paymentEvent);

        int databaseSizeBeforeUpdate = paymentEventRepository.findAll().size();

        // Update the paymentEvent using partial update
        PaymentEvent partialUpdatedPaymentEvent = new PaymentEvent();
        partialUpdatedPaymentEvent.setId(paymentEvent.getId());

        partialUpdatedPaymentEvent
            .status(UPDATED_STATUS)
            .reason(UPDATED_REASON)
            .on(UPDATED_ON)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .invoice(UPDATED_INVOICE);

        restPaymentEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentEvent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentEvent))
            )
            .andExpect(status().isOk());

        // Validate the PaymentEvent in the database
        List<PaymentEvent> paymentEventList = paymentEventRepository.findAll();
        assertThat(paymentEventList).hasSize(databaseSizeBeforeUpdate);
        PaymentEvent testPaymentEvent = paymentEventList.get(paymentEventList.size() - 1);
        assertThat(testPaymentEvent.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPaymentEvent.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testPaymentEvent.getOn()).isEqualTo(UPDATED_ON);
        assertThat(testPaymentEvent.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testPaymentEvent.getInvoice()).isEqualTo(UPDATED_INVOICE);
    }

    @Test
    @Transactional
    void patchNonExistingPaymentEvent() throws Exception {
        int databaseSizeBeforeUpdate = paymentEventRepository.findAll().size();
        paymentEvent.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentEvent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentEvent in the database
        List<PaymentEvent> paymentEventList = paymentEventRepository.findAll();
        assertThat(paymentEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentEvent() throws Exception {
        int databaseSizeBeforeUpdate = paymentEventRepository.findAll().size();
        paymentEvent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentEvent in the database
        List<PaymentEvent> paymentEventList = paymentEventRepository.findAll();
        assertThat(paymentEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentEvent() throws Exception {
        int databaseSizeBeforeUpdate = paymentEventRepository.findAll().size();
        paymentEvent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentEventMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentEvent))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentEvent in the database
        List<PaymentEvent> paymentEventList = paymentEventRepository.findAll();
        assertThat(paymentEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentEvent() throws Exception {
        // Initialize the database
        paymentEventRepository.saveAndFlush(paymentEvent);

        int databaseSizeBeforeDelete = paymentEventRepository.findAll().size();

        // Delete the paymentEvent
        restPaymentEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentEvent.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentEvent> paymentEventList = paymentEventRepository.findAll();
        assertThat(paymentEventList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
