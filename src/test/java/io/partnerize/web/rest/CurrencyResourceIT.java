package io.partnerize.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.partnerize.IntegrationTest;
import io.partnerize.domain.Currency;
import io.partnerize.repository.CurrencyRepository;
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
 * Integration tests for the {@link CurrencyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CurrencyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/currencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCurrencyMockMvc;

    private Currency currency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Currency createEntity(EntityManager em) {
        Currency currency = new Currency().name(DEFAULT_NAME).code(DEFAULT_CODE);
        return currency;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Currency createUpdatedEntity(EntityManager em) {
        Currency currency = new Currency().name(UPDATED_NAME).code(UPDATED_CODE);
        return currency;
    }

    @BeforeEach
    public void initTest() {
        currency = createEntity(em);
    }

    @Test
    @Transactional
    void createCurrency() throws Exception {
        int databaseSizeBeforeCreate = currencyRepository.findAll().size();
        // Create the Currency
        restCurrencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isCreated());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeCreate + 1);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCurrency.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createCurrencyWithExistingId() throws Exception {
        // Create the Currency with an existing ID
        currency.setId(1L);

        int databaseSizeBeforeCreate = currencyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCurrencies() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currency.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get the currency
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL_ID, currency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(currency.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingCurrency() throws Exception {
        // Get the currency
        restCurrencyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

        // Update the currency
        Currency updatedCurrency = currencyRepository.findById(currency.getId()).get();
        // Disconnect from session so that the updates on updatedCurrency are not directly saved in db
        em.detach(updatedCurrency);
        updatedCurrency.name(UPDATED_NAME).code(UPDATED_CODE);

        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCurrency.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCurrency))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCurrency.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, currency.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCurrencyWithPatch() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

        // Update the currency using partial update
        Currency partialUpdatedCurrency = new Currency();
        partialUpdatedCurrency.setId(currency.getId());

        partialUpdatedCurrency.code(UPDATED_CODE);

        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrency.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrency))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCurrency.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void fullUpdateCurrencyWithPatch() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

        // Update the currency using partial update
        Currency partialUpdatedCurrency = new Currency();
        partialUpdatedCurrency.setId(currency.getId());

        partialUpdatedCurrency.name(UPDATED_NAME).code(UPDATED_CODE);

        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrency.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrency))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCurrency.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, currency.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        int databaseSizeBeforeDelete = currencyRepository.findAll().size();

        // Delete the currency
        restCurrencyMockMvc
            .perform(delete(ENTITY_API_URL_ID, currency.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
