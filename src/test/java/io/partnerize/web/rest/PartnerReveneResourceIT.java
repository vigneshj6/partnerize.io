package io.partnerize.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.partnerize.IntegrationTest;
import io.partnerize.domain.PartnerRevene;
import io.partnerize.repository.PartnerReveneRepository;
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
 * Integration tests for the {@link PartnerReveneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PartnerReveneResourceIT {

    private static final String ENTITY_API_URL = "/api/partner-revenes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PartnerReveneRepository partnerReveneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartnerReveneMockMvc;

    private PartnerRevene partnerRevene;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartnerRevene createEntity(EntityManager em) {
        PartnerRevene partnerRevene = new PartnerRevene();
        return partnerRevene;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartnerRevene createUpdatedEntity(EntityManager em) {
        PartnerRevene partnerRevene = new PartnerRevene();
        return partnerRevene;
    }

    @BeforeEach
    public void initTest() {
        partnerRevene = createEntity(em);
    }

    @Test
    @Transactional
    void createPartnerRevene() throws Exception {
        int databaseSizeBeforeCreate = partnerReveneRepository.findAll().size();
        // Create the PartnerRevene
        restPartnerReveneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partnerRevene))
            )
            .andExpect(status().isCreated());

        // Validate the PartnerRevene in the database
        List<PartnerRevene> partnerReveneList = partnerReveneRepository.findAll();
        assertThat(partnerReveneList).hasSize(databaseSizeBeforeCreate + 1);
        PartnerRevene testPartnerRevene = partnerReveneList.get(partnerReveneList.size() - 1);
    }

    @Test
    @Transactional
    void createPartnerReveneWithExistingId() throws Exception {
        // Create the PartnerRevene with an existing ID
        partnerRevene.setId(1L);

        int databaseSizeBeforeCreate = partnerReveneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartnerReveneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partnerRevene))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerRevene in the database
        List<PartnerRevene> partnerReveneList = partnerReveneRepository.findAll();
        assertThat(partnerReveneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPartnerRevenes() throws Exception {
        // Initialize the database
        partnerReveneRepository.saveAndFlush(partnerRevene);

        // Get all the partnerReveneList
        restPartnerReveneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partnerRevene.getId().intValue())));
    }

    @Test
    @Transactional
    void getPartnerRevene() throws Exception {
        // Initialize the database
        partnerReveneRepository.saveAndFlush(partnerRevene);

        // Get the partnerRevene
        restPartnerReveneMockMvc
            .perform(get(ENTITY_API_URL_ID, partnerRevene.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partnerRevene.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPartnerRevene() throws Exception {
        // Get the partnerRevene
        restPartnerReveneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPartnerRevene() throws Exception {
        // Initialize the database
        partnerReveneRepository.saveAndFlush(partnerRevene);

        int databaseSizeBeforeUpdate = partnerReveneRepository.findAll().size();

        // Update the partnerRevene
        PartnerRevene updatedPartnerRevene = partnerReveneRepository.findById(partnerRevene.getId()).get();
        // Disconnect from session so that the updates on updatedPartnerRevene are not directly saved in db
        em.detach(updatedPartnerRevene);

        restPartnerReveneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPartnerRevene.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPartnerRevene))
            )
            .andExpect(status().isOk());

        // Validate the PartnerRevene in the database
        List<PartnerRevene> partnerReveneList = partnerReveneRepository.findAll();
        assertThat(partnerReveneList).hasSize(databaseSizeBeforeUpdate);
        PartnerRevene testPartnerRevene = partnerReveneList.get(partnerReveneList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingPartnerRevene() throws Exception {
        int databaseSizeBeforeUpdate = partnerReveneRepository.findAll().size();
        partnerRevene.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartnerReveneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partnerRevene.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partnerRevene))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerRevene in the database
        List<PartnerRevene> partnerReveneList = partnerReveneRepository.findAll();
        assertThat(partnerReveneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartnerRevene() throws Exception {
        int databaseSizeBeforeUpdate = partnerReveneRepository.findAll().size();
        partnerRevene.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerReveneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partnerRevene))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerRevene in the database
        List<PartnerRevene> partnerReveneList = partnerReveneRepository.findAll();
        assertThat(partnerReveneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartnerRevene() throws Exception {
        int databaseSizeBeforeUpdate = partnerReveneRepository.findAll().size();
        partnerRevene.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerReveneMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partnerRevene))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartnerRevene in the database
        List<PartnerRevene> partnerReveneList = partnerReveneRepository.findAll();
        assertThat(partnerReveneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartnerReveneWithPatch() throws Exception {
        // Initialize the database
        partnerReveneRepository.saveAndFlush(partnerRevene);

        int databaseSizeBeforeUpdate = partnerReveneRepository.findAll().size();

        // Update the partnerRevene using partial update
        PartnerRevene partialUpdatedPartnerRevene = new PartnerRevene();
        partialUpdatedPartnerRevene.setId(partnerRevene.getId());

        restPartnerReveneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartnerRevene.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartnerRevene))
            )
            .andExpect(status().isOk());

        // Validate the PartnerRevene in the database
        List<PartnerRevene> partnerReveneList = partnerReveneRepository.findAll();
        assertThat(partnerReveneList).hasSize(databaseSizeBeforeUpdate);
        PartnerRevene testPartnerRevene = partnerReveneList.get(partnerReveneList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdatePartnerReveneWithPatch() throws Exception {
        // Initialize the database
        partnerReveneRepository.saveAndFlush(partnerRevene);

        int databaseSizeBeforeUpdate = partnerReveneRepository.findAll().size();

        // Update the partnerRevene using partial update
        PartnerRevene partialUpdatedPartnerRevene = new PartnerRevene();
        partialUpdatedPartnerRevene.setId(partnerRevene.getId());

        restPartnerReveneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartnerRevene.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartnerRevene))
            )
            .andExpect(status().isOk());

        // Validate the PartnerRevene in the database
        List<PartnerRevene> partnerReveneList = partnerReveneRepository.findAll();
        assertThat(partnerReveneList).hasSize(databaseSizeBeforeUpdate);
        PartnerRevene testPartnerRevene = partnerReveneList.get(partnerReveneList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingPartnerRevene() throws Exception {
        int databaseSizeBeforeUpdate = partnerReveneRepository.findAll().size();
        partnerRevene.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartnerReveneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partnerRevene.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partnerRevene))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerRevene in the database
        List<PartnerRevene> partnerReveneList = partnerReveneRepository.findAll();
        assertThat(partnerReveneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartnerRevene() throws Exception {
        int databaseSizeBeforeUpdate = partnerReveneRepository.findAll().size();
        partnerRevene.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerReveneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partnerRevene))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerRevene in the database
        List<PartnerRevene> partnerReveneList = partnerReveneRepository.findAll();
        assertThat(partnerReveneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartnerRevene() throws Exception {
        int databaseSizeBeforeUpdate = partnerReveneRepository.findAll().size();
        partnerRevene.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerReveneMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partnerRevene))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartnerRevene in the database
        List<PartnerRevene> partnerReveneList = partnerReveneRepository.findAll();
        assertThat(partnerReveneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartnerRevene() throws Exception {
        // Initialize the database
        partnerReveneRepository.saveAndFlush(partnerRevene);

        int databaseSizeBeforeDelete = partnerReveneRepository.findAll().size();

        // Delete the partnerRevene
        restPartnerReveneMockMvc
            .perform(delete(ENTITY_API_URL_ID, partnerRevene.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PartnerRevene> partnerReveneList = partnerReveneRepository.findAll();
        assertThat(partnerReveneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
