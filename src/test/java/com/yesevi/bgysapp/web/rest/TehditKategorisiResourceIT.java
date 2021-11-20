package com.yesevi.bgysapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.bgysapp.IntegrationTest;
import com.yesevi.bgysapp.domain.TehditKategorisi;
import com.yesevi.bgysapp.repository.TehditKategorisiRepository;
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
 * Integration tests for the {@link TehditKategorisiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TehditKategorisiResourceIT {

    private static final String DEFAULT_ADI = "AAAAAAAAAA";
    private static final String UPDATED_ADI = "BBBBBBBBBB";

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tehdit-kategorisis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TehditKategorisiRepository tehditKategorisiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTehditKategorisiMockMvc;

    private TehditKategorisi tehditKategorisi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TehditKategorisi createEntity(EntityManager em) {
        TehditKategorisi tehditKategorisi = new TehditKategorisi().adi(DEFAULT_ADI).aciklama(DEFAULT_ACIKLAMA);
        return tehditKategorisi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TehditKategorisi createUpdatedEntity(EntityManager em) {
        TehditKategorisi tehditKategorisi = new TehditKategorisi().adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA);
        return tehditKategorisi;
    }

    @BeforeEach
    public void initTest() {
        tehditKategorisi = createEntity(em);
    }

    @Test
    @Transactional
    void createTehditKategorisi() throws Exception {
        int databaseSizeBeforeCreate = tehditKategorisiRepository.findAll().size();
        // Create the TehditKategorisi
        restTehditKategorisiMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tehditKategorisi))
            )
            .andExpect(status().isCreated());

        // Validate the TehditKategorisi in the database
        List<TehditKategorisi> tehditKategorisiList = tehditKategorisiRepository.findAll();
        assertThat(tehditKategorisiList).hasSize(databaseSizeBeforeCreate + 1);
        TehditKategorisi testTehditKategorisi = tehditKategorisiList.get(tehditKategorisiList.size() - 1);
        assertThat(testTehditKategorisi.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testTehditKategorisi.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
    }

    @Test
    @Transactional
    void createTehditKategorisiWithExistingId() throws Exception {
        // Create the TehditKategorisi with an existing ID
        tehditKategorisi.setId(1L);

        int databaseSizeBeforeCreate = tehditKategorisiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTehditKategorisiMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tehditKategorisi))
            )
            .andExpect(status().isBadRequest());

        // Validate the TehditKategorisi in the database
        List<TehditKategorisi> tehditKategorisiList = tehditKategorisiRepository.findAll();
        assertThat(tehditKategorisiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTehditKategorisis() throws Exception {
        // Initialize the database
        tehditKategorisiRepository.saveAndFlush(tehditKategorisi);

        // Get all the tehditKategorisiList
        restTehditKategorisiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tehditKategorisi.getId().intValue())))
            .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI)))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA)));
    }

    @Test
    @Transactional
    void getTehditKategorisi() throws Exception {
        // Initialize the database
        tehditKategorisiRepository.saveAndFlush(tehditKategorisi);

        // Get the tehditKategorisi
        restTehditKategorisiMockMvc
            .perform(get(ENTITY_API_URL_ID, tehditKategorisi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tehditKategorisi.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA));
    }

    @Test
    @Transactional
    void getNonExistingTehditKategorisi() throws Exception {
        // Get the tehditKategorisi
        restTehditKategorisiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTehditKategorisi() throws Exception {
        // Initialize the database
        tehditKategorisiRepository.saveAndFlush(tehditKategorisi);

        int databaseSizeBeforeUpdate = tehditKategorisiRepository.findAll().size();

        // Update the tehditKategorisi
        TehditKategorisi updatedTehditKategorisi = tehditKategorisiRepository.findById(tehditKategorisi.getId()).get();
        // Disconnect from session so that the updates on updatedTehditKategorisi are not directly saved in db
        em.detach(updatedTehditKategorisi);
        updatedTehditKategorisi.adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA);

        restTehditKategorisiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTehditKategorisi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTehditKategorisi))
            )
            .andExpect(status().isOk());

        // Validate the TehditKategorisi in the database
        List<TehditKategorisi> tehditKategorisiList = tehditKategorisiRepository.findAll();
        assertThat(tehditKategorisiList).hasSize(databaseSizeBeforeUpdate);
        TehditKategorisi testTehditKategorisi = tehditKategorisiList.get(tehditKategorisiList.size() - 1);
        assertThat(testTehditKategorisi.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testTehditKategorisi.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
    }

    @Test
    @Transactional
    void putNonExistingTehditKategorisi() throws Exception {
        int databaseSizeBeforeUpdate = tehditKategorisiRepository.findAll().size();
        tehditKategorisi.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTehditKategorisiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tehditKategorisi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tehditKategorisi))
            )
            .andExpect(status().isBadRequest());

        // Validate the TehditKategorisi in the database
        List<TehditKategorisi> tehditKategorisiList = tehditKategorisiRepository.findAll();
        assertThat(tehditKategorisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTehditKategorisi() throws Exception {
        int databaseSizeBeforeUpdate = tehditKategorisiRepository.findAll().size();
        tehditKategorisi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTehditKategorisiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tehditKategorisi))
            )
            .andExpect(status().isBadRequest());

        // Validate the TehditKategorisi in the database
        List<TehditKategorisi> tehditKategorisiList = tehditKategorisiRepository.findAll();
        assertThat(tehditKategorisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTehditKategorisi() throws Exception {
        int databaseSizeBeforeUpdate = tehditKategorisiRepository.findAll().size();
        tehditKategorisi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTehditKategorisiMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tehditKategorisi))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TehditKategorisi in the database
        List<TehditKategorisi> tehditKategorisiList = tehditKategorisiRepository.findAll();
        assertThat(tehditKategorisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTehditKategorisiWithPatch() throws Exception {
        // Initialize the database
        tehditKategorisiRepository.saveAndFlush(tehditKategorisi);

        int databaseSizeBeforeUpdate = tehditKategorisiRepository.findAll().size();

        // Update the tehditKategorisi using partial update
        TehditKategorisi partialUpdatedTehditKategorisi = new TehditKategorisi();
        partialUpdatedTehditKategorisi.setId(tehditKategorisi.getId());

        partialUpdatedTehditKategorisi.adi(UPDATED_ADI);

        restTehditKategorisiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTehditKategorisi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTehditKategorisi))
            )
            .andExpect(status().isOk());

        // Validate the TehditKategorisi in the database
        List<TehditKategorisi> tehditKategorisiList = tehditKategorisiRepository.findAll();
        assertThat(tehditKategorisiList).hasSize(databaseSizeBeforeUpdate);
        TehditKategorisi testTehditKategorisi = tehditKategorisiList.get(tehditKategorisiList.size() - 1);
        assertThat(testTehditKategorisi.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testTehditKategorisi.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
    }

    @Test
    @Transactional
    void fullUpdateTehditKategorisiWithPatch() throws Exception {
        // Initialize the database
        tehditKategorisiRepository.saveAndFlush(tehditKategorisi);

        int databaseSizeBeforeUpdate = tehditKategorisiRepository.findAll().size();

        // Update the tehditKategorisi using partial update
        TehditKategorisi partialUpdatedTehditKategorisi = new TehditKategorisi();
        partialUpdatedTehditKategorisi.setId(tehditKategorisi.getId());

        partialUpdatedTehditKategorisi.adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA);

        restTehditKategorisiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTehditKategorisi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTehditKategorisi))
            )
            .andExpect(status().isOk());

        // Validate the TehditKategorisi in the database
        List<TehditKategorisi> tehditKategorisiList = tehditKategorisiRepository.findAll();
        assertThat(tehditKategorisiList).hasSize(databaseSizeBeforeUpdate);
        TehditKategorisi testTehditKategorisi = tehditKategorisiList.get(tehditKategorisiList.size() - 1);
        assertThat(testTehditKategorisi.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testTehditKategorisi.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
    }

    @Test
    @Transactional
    void patchNonExistingTehditKategorisi() throws Exception {
        int databaseSizeBeforeUpdate = tehditKategorisiRepository.findAll().size();
        tehditKategorisi.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTehditKategorisiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tehditKategorisi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tehditKategorisi))
            )
            .andExpect(status().isBadRequest());

        // Validate the TehditKategorisi in the database
        List<TehditKategorisi> tehditKategorisiList = tehditKategorisiRepository.findAll();
        assertThat(tehditKategorisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTehditKategorisi() throws Exception {
        int databaseSizeBeforeUpdate = tehditKategorisiRepository.findAll().size();
        tehditKategorisi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTehditKategorisiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tehditKategorisi))
            )
            .andExpect(status().isBadRequest());

        // Validate the TehditKategorisi in the database
        List<TehditKategorisi> tehditKategorisiList = tehditKategorisiRepository.findAll();
        assertThat(tehditKategorisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTehditKategorisi() throws Exception {
        int databaseSizeBeforeUpdate = tehditKategorisiRepository.findAll().size();
        tehditKategorisi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTehditKategorisiMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tehditKategorisi))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TehditKategorisi in the database
        List<TehditKategorisi> tehditKategorisiList = tehditKategorisiRepository.findAll();
        assertThat(tehditKategorisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTehditKategorisi() throws Exception {
        // Initialize the database
        tehditKategorisiRepository.saveAndFlush(tehditKategorisi);

        int databaseSizeBeforeDelete = tehditKategorisiRepository.findAll().size();

        // Delete the tehditKategorisi
        restTehditKategorisiMockMvc
            .perform(delete(ENTITY_API_URL_ID, tehditKategorisi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TehditKategorisi> tehditKategorisiList = tehditKategorisiRepository.findAll();
        assertThat(tehditKategorisiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
