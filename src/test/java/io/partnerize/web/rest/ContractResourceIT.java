package io.partnerize.web.rest;

import static io.partnerize.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.partnerize.IntegrationTest;
import io.partnerize.domain.Contract;
import io.partnerize.domain.enumeration.BillingCycle;
import io.partnerize.domain.enumeration.ContractType;
import io.partnerize.repository.ContractRepository;
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
 * Integration tests for the {@link ContractResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContractResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ContractType DEFAULT_TYPE = ContractType.FIXED;
    private static final ContractType UPDATED_TYPE = ContractType.COMMISSION_PER_SALE;

    private static final Instant DEFAULT_START_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BillingCycle DEFAULT_BILLING_CYCLE = BillingCycle.WEEK;
    private static final BillingCycle UPDATED_BILLING_CYCLE = BillingCycle.MONTH;

    private static final BigDecimal DEFAULT_FIXED_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_FIXED_RATE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_COMMISION_PERCENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_COMMISION_PERCENT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_COMMISION_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_COMMISION_RATE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractMockMvc;

    private Contract contract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createEntity(EntityManager em) {
        Contract contract = new Contract()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .startAt(DEFAULT_START_AT)
            .endAt(DEFAULT_END_AT)
            .billingCycle(DEFAULT_BILLING_CYCLE)
            .fixedRate(DEFAULT_FIXED_RATE)
            .commisionPercent(DEFAULT_COMMISION_PERCENT)
            .commisionRate(DEFAULT_COMMISION_RATE);
        return contract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createUpdatedEntity(EntityManager em) {
        Contract contract = new Contract()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .billingCycle(UPDATED_BILLING_CYCLE)
            .fixedRate(UPDATED_FIXED_RATE)
            .commisionPercent(UPDATED_COMMISION_PERCENT)
            .commisionRate(UPDATED_COMMISION_RATE);
        return contract;
    }

    @BeforeEach
    public void initTest() {
        contract = createEntity(em);
    }

    @Test
    @Transactional
    void createContract() throws Exception {
        int databaseSizeBeforeCreate = contractRepository.findAll().size();
        // Create the Contract
        restContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isCreated());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate + 1);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContract.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testContract.getStartAt()).isEqualTo(DEFAULT_START_AT);
        assertThat(testContract.getEndAt()).isEqualTo(DEFAULT_END_AT);
        assertThat(testContract.getBillingCycle()).isEqualTo(DEFAULT_BILLING_CYCLE);
        assertThat(testContract.getFixedRate()).isEqualByComparingTo(DEFAULT_FIXED_RATE);
        assertThat(testContract.getCommisionPercent()).isEqualByComparingTo(DEFAULT_COMMISION_PERCENT);
        assertThat(testContract.getCommisionRate()).isEqualByComparingTo(DEFAULT_COMMISION_RATE);
    }

    @Test
    @Transactional
    void createContractWithExistingId() throws Exception {
        // Create the Contract with an existing ID
        contract.setId(1L);

        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContracts() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startAt").value(hasItem(DEFAULT_START_AT.toString())))
            .andExpect(jsonPath("$.[*].endAt").value(hasItem(DEFAULT_END_AT.toString())))
            .andExpect(jsonPath("$.[*].billingCycle").value(hasItem(DEFAULT_BILLING_CYCLE.toString())))
            .andExpect(jsonPath("$.[*].fixedRate").value(hasItem(sameNumber(DEFAULT_FIXED_RATE))))
            .andExpect(jsonPath("$.[*].commisionPercent").value(hasItem(sameNumber(DEFAULT_COMMISION_PERCENT))))
            .andExpect(jsonPath("$.[*].commisionRate").value(hasItem(sameNumber(DEFAULT_COMMISION_RATE))));
    }

    @Test
    @Transactional
    void getContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc
            .perform(get(ENTITY_API_URL_ID, contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.startAt").value(DEFAULT_START_AT.toString()))
            .andExpect(jsonPath("$.endAt").value(DEFAULT_END_AT.toString()))
            .andExpect(jsonPath("$.billingCycle").value(DEFAULT_BILLING_CYCLE.toString()))
            .andExpect(jsonPath("$.fixedRate").value(sameNumber(DEFAULT_FIXED_RATE)))
            .andExpect(jsonPath("$.commisionPercent").value(sameNumber(DEFAULT_COMMISION_PERCENT)))
            .andExpect(jsonPath("$.commisionRate").value(sameNumber(DEFAULT_COMMISION_RATE)));
    }

    @Test
    @Transactional
    void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract
        Contract updatedContract = contractRepository.findById(contract.getId()).get();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        updatedContract
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .billingCycle(UPDATED_BILLING_CYCLE)
            .fixedRate(UPDATED_FIXED_RATE)
            .commisionPercent(UPDATED_COMMISION_PERCENT)
            .commisionRate(UPDATED_COMMISION_RATE);

        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContract.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContract.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContract.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testContract.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testContract.getBillingCycle()).isEqualTo(UPDATED_BILLING_CYCLE);
        assertThat(testContract.getFixedRate()).isEqualByComparingTo(UPDATED_FIXED_RATE);
        assertThat(testContract.getCommisionPercent()).isEqualByComparingTo(UPDATED_COMMISION_PERCENT);
        assertThat(testContract.getCommisionRate()).isEqualByComparingTo(UPDATED_COMMISION_RATE);
    }

    @Test
    @Transactional
    void putNonExistingContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contract.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContractWithPatch() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract.type(UPDATED_TYPE).startAt(UPDATED_START_AT).fixedRate(UPDATED_FIXED_RATE);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContract.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContract.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testContract.getEndAt()).isEqualTo(DEFAULT_END_AT);
        assertThat(testContract.getBillingCycle()).isEqualTo(DEFAULT_BILLING_CYCLE);
        assertThat(testContract.getFixedRate()).isEqualByComparingTo(UPDATED_FIXED_RATE);
        assertThat(testContract.getCommisionPercent()).isEqualByComparingTo(DEFAULT_COMMISION_PERCENT);
        assertThat(testContract.getCommisionRate()).isEqualByComparingTo(DEFAULT_COMMISION_RATE);
    }

    @Test
    @Transactional
    void fullUpdateContractWithPatch() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .billingCycle(UPDATED_BILLING_CYCLE)
            .fixedRate(UPDATED_FIXED_RATE)
            .commisionPercent(UPDATED_COMMISION_PERCENT)
            .commisionRate(UPDATED_COMMISION_RATE);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContract.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContract.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testContract.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testContract.getBillingCycle()).isEqualTo(UPDATED_BILLING_CYCLE);
        assertThat(testContract.getFixedRate()).isEqualByComparingTo(UPDATED_FIXED_RATE);
        assertThat(testContract.getCommisionPercent()).isEqualByComparingTo(UPDATED_COMMISION_PERCENT);
        assertThat(testContract.getCommisionRate()).isEqualByComparingTo(UPDATED_COMMISION_RATE);
    }

    @Test
    @Transactional
    void patchNonExistingContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contract.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeDelete = contractRepository.findAll().size();

        // Delete the contract
        restContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, contract.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
