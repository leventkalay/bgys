package com.yesevi.bgysapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.bgysapp.IntegrationTest;
import com.yesevi.bgysapp.domain.Tehdit;
import com.yesevi.bgysapp.domain.enumeration.Onay;
import com.yesevi.bgysapp.repository.TehditRepository;
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
 * Integration tests for the {@link TehditResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TehditResourceIT {

    private static final String DEFAULT_ADI = "AAAAAAAAAA";
    private static final String UPDATED_ADI = "BBBBBBBBBB";

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final Onay DEFAULT_ONAY_DURUMU = Onay.ONAYLANMADI;
    private static final Onay UPDATED_ONAY_DURUMU = Onay.ONAY_BEKLENMEDE;

    private static final String ENTITY_API_URL = "/api/tehdits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TehditRepository tehditRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTehditMockMvc;

    private Tehdit tehdit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tehdit createEntity(EntityManager em) {
        Tehdit tehdit = new Tehdit().adi(DEFAULT_ADI).aciklama(DEFAULT_ACIKLAMA).onayDurumu(DEFAULT_ONAY_DURUMU);
        return tehdit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tehdit createUpdatedEntity(EntityManager em) {
        Tehdit tehdit = new Tehdit().adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA).onayDurumu(UPDATED_ONAY_DURUMU);
        return tehdit;
    }

    @BeforeEach
    public void initTest() {
        tehdit = createEntity(em);
    }

    @Test
    @Transactional
    void createTehdit() throws Exception {
        int databaseSizeBeforeCreate = tehditRepository.findAll().size();
        // Create the Tehdit
        restTehditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tehdit)))
            .andExpect(status().isCreated());

        // Validate the Tehdit in the database
        List<Tehdit> tehditList = tehditRepository.findAll();
        assertThat(tehditList).hasSize(databaseSizeBeforeCreate + 1);
        Tehdit testTehdit = tehditList.get(tehditList.size() - 1);
        assertThat(testTehdit.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testTehdit.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testTehdit.getOnayDurumu()).isEqualTo(DEFAULT_ONAY_DURUMU);
    }

    @Test
    @Transactional
    void createTehditWithExistingId() throws Exception {
        // Create the Tehdit with an existing ID
        tehdit.setId(1L);

        int databaseSizeBeforeCreate = tehditRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTehditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tehdit)))
            .andExpect(status().isBadRequest());

        // Validate the Tehdit in the database
        List<Tehdit> tehditList = tehditRepository.findAll();
        assertThat(tehditList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAdiIsRequired() throws Exception {
        int databaseSizeBeforeTest = tehditRepository.findAll().size();
        // set the field null
        tehdit.setAdi(null);

        // Create the Tehdit, which fails.

        restTehditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tehdit)))
            .andExpect(status().isBadRequest());

        List<Tehdit> tehditList = tehditRepository.findAll();
        assertThat(tehditList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTehdits() throws Exception {
        // Initialize the database
        tehditRepository.saveAndFlush(tehdit);

        // Get all the tehditList
        restTehditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tehdit.getId().intValue())))
            .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI)))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA)))
            .andExpect(jsonPath("$.[*].onayDurumu").value(hasItem(DEFAULT_ONAY_DURUMU.toString())));
    }

    @Test
    @Transactional
    void getTehdit() throws Exception {
        // Initialize the database
        tehditRepository.saveAndFlush(tehdit);

        // Get the tehdit
        restTehditMockMvc
            .perform(get(ENTITY_API_URL_ID, tehdit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tehdit.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA))
            .andExpect(jsonPath("$.onayDurumu").value(DEFAULT_ONAY_DURUMU.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTehdit() throws Exception {
        // Get the tehdit
        restTehditMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTehdit() throws Exception {
        // Initialize the database
        tehditRepository.saveAndFlush(tehdit);

        int databaseSizeBeforeUpdate = tehditRepository.findAll().size();

        // Update the tehdit
        Tehdit updatedTehdit = tehditRepository.findById(tehdit.getId()).get();
        // Disconnect from session so that the updates on updatedTehdit are not directly saved in db
        em.detach(updatedTehdit);
        updatedTehdit.adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA).onayDurumu(UPDATED_ONAY_DURUMU);

        restTehditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTehdit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTehdit))
            )
            .andExpect(status().isOk());

        // Validate the Tehdit in the database
        List<Tehdit> tehditList = tehditRepository.findAll();
        assertThat(tehditList).hasSize(databaseSizeBeforeUpdate);
        Tehdit testTehdit = tehditList.get(tehditList.size() - 1);
        assertThat(testTehdit.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testTehdit.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testTehdit.getOnayDurumu()).isEqualTo(UPDATED_ONAY_DURUMU);
    }

    @Test
    @Transactional
    void putNonExistingTehdit() throws Exception {
        int databaseSizeBeforeUpdate = tehditRepository.findAll().size();
        tehdit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTehditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tehdit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tehdit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tehdit in the database
        List<Tehdit> tehditList = tehditRepository.findAll();
        assertThat(tehditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTehdit() throws Exception {
        int databaseSizeBeforeUpdate = tehditRepository.findAll().size();
        tehdit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTehditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tehdit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tehdit in the database
        List<Tehdit> tehditList = tehditRepository.findAll();
        assertThat(tehditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTehdit() throws Exception {
        int databaseSizeBeforeUpdate = tehditRepository.findAll().size();
        tehdit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTehditMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tehdit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tehdit in the database
        List<Tehdit> tehditList = tehditRepository.findAll();
        assertThat(tehditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTehditWithPatch() throws Exception {
        // Initialize the database
        tehditRepository.saveAndFlush(tehdit);

        int databaseSizeBeforeUpdate = tehditRepository.findAll().size();

        // Update the tehdit using partial update
        Tehdit partialUpdatedTehdit = new Tehdit();
        partialUpdatedTehdit.setId(tehdit.getId());

        partialUpdatedTehdit.onayDurumu(UPDATED_ONAY_DURUMU);

        restTehditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTehdit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTehdit))
            )
            .andExpect(status().isOk());

        // Validate the Tehdit in the database
        List<Tehdit> tehditList = tehditRepository.findAll();
        assertThat(tehditList).hasSize(databaseSizeBeforeUpdate);
        Tehdit testTehdit = tehditList.get(tehditList.size() - 1);
        assertThat(testTehdit.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testTehdit.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testTehdit.getOnayDurumu()).isEqualTo(UPDATED_ONAY_DURUMU);
    }

    @Test
    @Transactional
    void fullUpdateTehditWithPatch() throws Exception {
        // Initialize the database
        tehditRepository.saveAndFlush(tehdit);

        int databaseSizeBeforeUpdate = tehditRepository.findAll().size();

        // Update the tehdit using partial update
        Tehdit partialUpdatedTehdit = new Tehdit();
        partialUpdatedTehdit.setId(tehdit.getId());

        partialUpdatedTehdit.adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA).onayDurumu(UPDATED_ONAY_DURUMU);

        restTehditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTehdit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTehdit))
            )
            .andExpect(status().isOk());

        // Validate the Tehdit in the database
        List<Tehdit> tehditList = tehditRepository.findAll();
        assertThat(tehditList).hasSize(databaseSizeBeforeUpdate);
        Tehdit testTehdit = tehditList.get(tehditList.size() - 1);
        assertThat(testTehdit.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testTehdit.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testTehdit.getOnayDurumu()).isEqualTo(UPDATED_ONAY_DURUMU);
    }

    @Test
    @Transactional
    void patchNonExistingTehdit() throws Exception {
        int databaseSizeBeforeUpdate = tehditRepository.findAll().size();
        tehdit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTehditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tehdit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tehdit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tehdit in the database
        List<Tehdit> tehditList = tehditRepository.findAll();
        assertThat(tehditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTehdit() throws Exception {
        int databaseSizeBeforeUpdate = tehditRepository.findAll().size();
        tehdit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTehditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tehdit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tehdit in the database
        List<Tehdit> tehditList = tehditRepository.findAll();
        assertThat(tehditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTehdit() throws Exception {
        int databaseSizeBeforeUpdate = tehditRepository.findAll().size();
        tehdit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTehditMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tehdit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tehdit in the database
        List<Tehdit> tehditList = tehditRepository.findAll();
        assertThat(tehditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTehdit() throws Exception {
        // Initialize the database
        tehditRepository.saveAndFlush(tehdit);

        int databaseSizeBeforeDelete = tehditRepository.findAll().size();

        // Delete the tehdit
        restTehditMockMvc
            .perform(delete(ENTITY_API_URL_ID, tehdit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tehdit> tehditList = tehditRepository.findAll();
        assertThat(tehditList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
