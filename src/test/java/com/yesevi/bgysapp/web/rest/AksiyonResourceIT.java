package com.yesevi.bgysapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.bgysapp.IntegrationTest;
import com.yesevi.bgysapp.domain.Aksiyon;
import com.yesevi.bgysapp.domain.enumeration.Onay;
import com.yesevi.bgysapp.repository.AksiyonRepository;
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
 * Integration tests for the {@link AksiyonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AksiyonResourceIT {

    private static final String DEFAULT_ADI = "AAAAAAAAAA";
    private static final String UPDATED_ADI = "BBBBBBBBBB";

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final Onay DEFAULT_ONAY_DURUMU = Onay.ONAYLANMADI;
    private static final Onay UPDATED_ONAY_DURUMU = Onay.ONAY_BEKLENMEDE;

    private static final String ENTITY_API_URL = "/api/aksiyons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AksiyonRepository aksiyonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAksiyonMockMvc;

    private Aksiyon aksiyon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aksiyon createEntity(EntityManager em) {
        Aksiyon aksiyon = new Aksiyon().adi(DEFAULT_ADI).aciklama(DEFAULT_ACIKLAMA).onayDurumu(DEFAULT_ONAY_DURUMU);
        return aksiyon;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aksiyon createUpdatedEntity(EntityManager em) {
        Aksiyon aksiyon = new Aksiyon().adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA).onayDurumu(UPDATED_ONAY_DURUMU);
        return aksiyon;
    }

    @BeforeEach
    public void initTest() {
        aksiyon = createEntity(em);
    }

    @Test
    @Transactional
    void createAksiyon() throws Exception {
        int databaseSizeBeforeCreate = aksiyonRepository.findAll().size();
        // Create the Aksiyon
        restAksiyonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aksiyon)))
            .andExpect(status().isCreated());

        // Validate the Aksiyon in the database
        List<Aksiyon> aksiyonList = aksiyonRepository.findAll();
        assertThat(aksiyonList).hasSize(databaseSizeBeforeCreate + 1);
        Aksiyon testAksiyon = aksiyonList.get(aksiyonList.size() - 1);
        assertThat(testAksiyon.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testAksiyon.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testAksiyon.getOnayDurumu()).isEqualTo(DEFAULT_ONAY_DURUMU);
    }

    @Test
    @Transactional
    void createAksiyonWithExistingId() throws Exception {
        // Create the Aksiyon with an existing ID
        aksiyon.setId(1L);

        int databaseSizeBeforeCreate = aksiyonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAksiyonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aksiyon)))
            .andExpect(status().isBadRequest());

        // Validate the Aksiyon in the database
        List<Aksiyon> aksiyonList = aksiyonRepository.findAll();
        assertThat(aksiyonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAdiIsRequired() throws Exception {
        int databaseSizeBeforeTest = aksiyonRepository.findAll().size();
        // set the field null
        aksiyon.setAdi(null);

        // Create the Aksiyon, which fails.

        restAksiyonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aksiyon)))
            .andExpect(status().isBadRequest());

        List<Aksiyon> aksiyonList = aksiyonRepository.findAll();
        assertThat(aksiyonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAksiyons() throws Exception {
        // Initialize the database
        aksiyonRepository.saveAndFlush(aksiyon);

        // Get all the aksiyonList
        restAksiyonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aksiyon.getId().intValue())))
            .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI)))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA)))
            .andExpect(jsonPath("$.[*].onayDurumu").value(hasItem(DEFAULT_ONAY_DURUMU.toString())));
    }

    @Test
    @Transactional
    void getAksiyon() throws Exception {
        // Initialize the database
        aksiyonRepository.saveAndFlush(aksiyon);

        // Get the aksiyon
        restAksiyonMockMvc
            .perform(get(ENTITY_API_URL_ID, aksiyon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aksiyon.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA))
            .andExpect(jsonPath("$.onayDurumu").value(DEFAULT_ONAY_DURUMU.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAksiyon() throws Exception {
        // Get the aksiyon
        restAksiyonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAksiyon() throws Exception {
        // Initialize the database
        aksiyonRepository.saveAndFlush(aksiyon);

        int databaseSizeBeforeUpdate = aksiyonRepository.findAll().size();

        // Update the aksiyon
        Aksiyon updatedAksiyon = aksiyonRepository.findById(aksiyon.getId()).get();
        // Disconnect from session so that the updates on updatedAksiyon are not directly saved in db
        em.detach(updatedAksiyon);
        updatedAksiyon.adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA).onayDurumu(UPDATED_ONAY_DURUMU);

        restAksiyonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAksiyon.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAksiyon))
            )
            .andExpect(status().isOk());

        // Validate the Aksiyon in the database
        List<Aksiyon> aksiyonList = aksiyonRepository.findAll();
        assertThat(aksiyonList).hasSize(databaseSizeBeforeUpdate);
        Aksiyon testAksiyon = aksiyonList.get(aksiyonList.size() - 1);
        assertThat(testAksiyon.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testAksiyon.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testAksiyon.getOnayDurumu()).isEqualTo(UPDATED_ONAY_DURUMU);
    }

    @Test
    @Transactional
    void putNonExistingAksiyon() throws Exception {
        int databaseSizeBeforeUpdate = aksiyonRepository.findAll().size();
        aksiyon.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAksiyonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aksiyon.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aksiyon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aksiyon in the database
        List<Aksiyon> aksiyonList = aksiyonRepository.findAll();
        assertThat(aksiyonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAksiyon() throws Exception {
        int databaseSizeBeforeUpdate = aksiyonRepository.findAll().size();
        aksiyon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAksiyonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aksiyon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aksiyon in the database
        List<Aksiyon> aksiyonList = aksiyonRepository.findAll();
        assertThat(aksiyonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAksiyon() throws Exception {
        int databaseSizeBeforeUpdate = aksiyonRepository.findAll().size();
        aksiyon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAksiyonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aksiyon)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aksiyon in the database
        List<Aksiyon> aksiyonList = aksiyonRepository.findAll();
        assertThat(aksiyonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAksiyonWithPatch() throws Exception {
        // Initialize the database
        aksiyonRepository.saveAndFlush(aksiyon);

        int databaseSizeBeforeUpdate = aksiyonRepository.findAll().size();

        // Update the aksiyon using partial update
        Aksiyon partialUpdatedAksiyon = new Aksiyon();
        partialUpdatedAksiyon.setId(aksiyon.getId());

        restAksiyonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAksiyon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAksiyon))
            )
            .andExpect(status().isOk());

        // Validate the Aksiyon in the database
        List<Aksiyon> aksiyonList = aksiyonRepository.findAll();
        assertThat(aksiyonList).hasSize(databaseSizeBeforeUpdate);
        Aksiyon testAksiyon = aksiyonList.get(aksiyonList.size() - 1);
        assertThat(testAksiyon.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testAksiyon.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testAksiyon.getOnayDurumu()).isEqualTo(DEFAULT_ONAY_DURUMU);
    }

    @Test
    @Transactional
    void fullUpdateAksiyonWithPatch() throws Exception {
        // Initialize the database
        aksiyonRepository.saveAndFlush(aksiyon);

        int databaseSizeBeforeUpdate = aksiyonRepository.findAll().size();

        // Update the aksiyon using partial update
        Aksiyon partialUpdatedAksiyon = new Aksiyon();
        partialUpdatedAksiyon.setId(aksiyon.getId());

        partialUpdatedAksiyon.adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA).onayDurumu(UPDATED_ONAY_DURUMU);

        restAksiyonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAksiyon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAksiyon))
            )
            .andExpect(status().isOk());

        // Validate the Aksiyon in the database
        List<Aksiyon> aksiyonList = aksiyonRepository.findAll();
        assertThat(aksiyonList).hasSize(databaseSizeBeforeUpdate);
        Aksiyon testAksiyon = aksiyonList.get(aksiyonList.size() - 1);
        assertThat(testAksiyon.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testAksiyon.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testAksiyon.getOnayDurumu()).isEqualTo(UPDATED_ONAY_DURUMU);
    }

    @Test
    @Transactional
    void patchNonExistingAksiyon() throws Exception {
        int databaseSizeBeforeUpdate = aksiyonRepository.findAll().size();
        aksiyon.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAksiyonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aksiyon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aksiyon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aksiyon in the database
        List<Aksiyon> aksiyonList = aksiyonRepository.findAll();
        assertThat(aksiyonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAksiyon() throws Exception {
        int databaseSizeBeforeUpdate = aksiyonRepository.findAll().size();
        aksiyon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAksiyonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aksiyon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aksiyon in the database
        List<Aksiyon> aksiyonList = aksiyonRepository.findAll();
        assertThat(aksiyonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAksiyon() throws Exception {
        int databaseSizeBeforeUpdate = aksiyonRepository.findAll().size();
        aksiyon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAksiyonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aksiyon)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aksiyon in the database
        List<Aksiyon> aksiyonList = aksiyonRepository.findAll();
        assertThat(aksiyonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAksiyon() throws Exception {
        // Initialize the database
        aksiyonRepository.saveAndFlush(aksiyon);

        int databaseSizeBeforeDelete = aksiyonRepository.findAll().size();

        // Delete the aksiyon
        restAksiyonMockMvc
            .perform(delete(ENTITY_API_URL_ID, aksiyon.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Aksiyon> aksiyonList = aksiyonRepository.findAll();
        assertThat(aksiyonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
