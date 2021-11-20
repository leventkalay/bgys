package com.yesevi.bgysapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.bgysapp.IntegrationTest;
import com.yesevi.bgysapp.domain.Surec;
import com.yesevi.bgysapp.repository.SurecRepository;
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
 * Integration tests for the {@link SurecResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SurecResourceIT {

    private static final String DEFAULT_ADI = "AAAAAAAAAA";
    private static final String UPDATED_ADI = "BBBBBBBBBB";

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/surecs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SurecRepository surecRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSurecMockMvc;

    private Surec surec;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Surec createEntity(EntityManager em) {
        Surec surec = new Surec().adi(DEFAULT_ADI).aciklama(DEFAULT_ACIKLAMA);
        return surec;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Surec createUpdatedEntity(EntityManager em) {
        Surec surec = new Surec().adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA);
        return surec;
    }

    @BeforeEach
    public void initTest() {
        surec = createEntity(em);
    }

    @Test
    @Transactional
    void createSurec() throws Exception {
        int databaseSizeBeforeCreate = surecRepository.findAll().size();
        // Create the Surec
        restSurecMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surec)))
            .andExpect(status().isCreated());

        // Validate the Surec in the database
        List<Surec> surecList = surecRepository.findAll();
        assertThat(surecList).hasSize(databaseSizeBeforeCreate + 1);
        Surec testSurec = surecList.get(surecList.size() - 1);
        assertThat(testSurec.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testSurec.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
    }

    @Test
    @Transactional
    void createSurecWithExistingId() throws Exception {
        // Create the Surec with an existing ID
        surec.setId(1L);

        int databaseSizeBeforeCreate = surecRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSurecMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surec)))
            .andExpect(status().isBadRequest());

        // Validate the Surec in the database
        List<Surec> surecList = surecRepository.findAll();
        assertThat(surecList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSurecs() throws Exception {
        // Initialize the database
        surecRepository.saveAndFlush(surec);

        // Get all the surecList
        restSurecMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(surec.getId().intValue())))
            .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI)))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA)));
    }

    @Test
    @Transactional
    void getSurec() throws Exception {
        // Initialize the database
        surecRepository.saveAndFlush(surec);

        // Get the surec
        restSurecMockMvc
            .perform(get(ENTITY_API_URL_ID, surec.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(surec.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA));
    }

    @Test
    @Transactional
    void getNonExistingSurec() throws Exception {
        // Get the surec
        restSurecMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSurec() throws Exception {
        // Initialize the database
        surecRepository.saveAndFlush(surec);

        int databaseSizeBeforeUpdate = surecRepository.findAll().size();

        // Update the surec
        Surec updatedSurec = surecRepository.findById(surec.getId()).get();
        // Disconnect from session so that the updates on updatedSurec are not directly saved in db
        em.detach(updatedSurec);
        updatedSurec.adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA);

        restSurecMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSurec.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSurec))
            )
            .andExpect(status().isOk());

        // Validate the Surec in the database
        List<Surec> surecList = surecRepository.findAll();
        assertThat(surecList).hasSize(databaseSizeBeforeUpdate);
        Surec testSurec = surecList.get(surecList.size() - 1);
        assertThat(testSurec.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testSurec.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
    }

    @Test
    @Transactional
    void putNonExistingSurec() throws Exception {
        int databaseSizeBeforeUpdate = surecRepository.findAll().size();
        surec.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurecMockMvc
            .perform(
                put(ENTITY_API_URL_ID, surec.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surec))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surec in the database
        List<Surec> surecList = surecRepository.findAll();
        assertThat(surecList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSurec() throws Exception {
        int databaseSizeBeforeUpdate = surecRepository.findAll().size();
        surec.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurecMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surec))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surec in the database
        List<Surec> surecList = surecRepository.findAll();
        assertThat(surecList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSurec() throws Exception {
        int databaseSizeBeforeUpdate = surecRepository.findAll().size();
        surec.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurecMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surec)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Surec in the database
        List<Surec> surecList = surecRepository.findAll();
        assertThat(surecList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSurecWithPatch() throws Exception {
        // Initialize the database
        surecRepository.saveAndFlush(surec);

        int databaseSizeBeforeUpdate = surecRepository.findAll().size();

        // Update the surec using partial update
        Surec partialUpdatedSurec = new Surec();
        partialUpdatedSurec.setId(surec.getId());

        partialUpdatedSurec.aciklama(UPDATED_ACIKLAMA);

        restSurecMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurec.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurec))
            )
            .andExpect(status().isOk());

        // Validate the Surec in the database
        List<Surec> surecList = surecRepository.findAll();
        assertThat(surecList).hasSize(databaseSizeBeforeUpdate);
        Surec testSurec = surecList.get(surecList.size() - 1);
        assertThat(testSurec.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testSurec.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
    }

    @Test
    @Transactional
    void fullUpdateSurecWithPatch() throws Exception {
        // Initialize the database
        surecRepository.saveAndFlush(surec);

        int databaseSizeBeforeUpdate = surecRepository.findAll().size();

        // Update the surec using partial update
        Surec partialUpdatedSurec = new Surec();
        partialUpdatedSurec.setId(surec.getId());

        partialUpdatedSurec.adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA);

        restSurecMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurec.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurec))
            )
            .andExpect(status().isOk());

        // Validate the Surec in the database
        List<Surec> surecList = surecRepository.findAll();
        assertThat(surecList).hasSize(databaseSizeBeforeUpdate);
        Surec testSurec = surecList.get(surecList.size() - 1);
        assertThat(testSurec.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testSurec.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
    }

    @Test
    @Transactional
    void patchNonExistingSurec() throws Exception {
        int databaseSizeBeforeUpdate = surecRepository.findAll().size();
        surec.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurecMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, surec.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(surec))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surec in the database
        List<Surec> surecList = surecRepository.findAll();
        assertThat(surecList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSurec() throws Exception {
        int databaseSizeBeforeUpdate = surecRepository.findAll().size();
        surec.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurecMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(surec))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surec in the database
        List<Surec> surecList = surecRepository.findAll();
        assertThat(surecList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSurec() throws Exception {
        int databaseSizeBeforeUpdate = surecRepository.findAll().size();
        surec.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurecMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(surec)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Surec in the database
        List<Surec> surecList = surecRepository.findAll();
        assertThat(surecList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSurec() throws Exception {
        // Initialize the database
        surecRepository.saveAndFlush(surec);

        int databaseSizeBeforeDelete = surecRepository.findAll().size();

        // Delete the surec
        restSurecMockMvc
            .perform(delete(ENTITY_API_URL_ID, surec.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Surec> surecList = surecRepository.findAll();
        assertThat(surecList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
