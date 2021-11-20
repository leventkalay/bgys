package com.yesevi.bgysapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.bgysapp.IntegrationTest;
import com.yesevi.bgysapp.domain.Birim;
import com.yesevi.bgysapp.repository.BirimRepository;
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
 * Integration tests for the {@link BirimResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BirimResourceIT {

    private static final String DEFAULT_ADI = "AAAAAAAAAA";
    private static final String UPDATED_ADI = "BBBBBBBBBB";

    private static final String DEFAULT_SOYADI = "AAAAAAAAAA";
    private static final String UPDATED_SOYADI = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/birims";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BirimRepository birimRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBirimMockMvc;

    private Birim birim;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Birim createEntity(EntityManager em) {
        Birim birim = new Birim().adi(DEFAULT_ADI).soyadi(DEFAULT_SOYADI);
        return birim;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Birim createUpdatedEntity(EntityManager em) {
        Birim birim = new Birim().adi(UPDATED_ADI).soyadi(UPDATED_SOYADI);
        return birim;
    }

    @BeforeEach
    public void initTest() {
        birim = createEntity(em);
    }

    @Test
    @Transactional
    void createBirim() throws Exception {
        int databaseSizeBeforeCreate = birimRepository.findAll().size();
        // Create the Birim
        restBirimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(birim)))
            .andExpect(status().isCreated());

        // Validate the Birim in the database
        List<Birim> birimList = birimRepository.findAll();
        assertThat(birimList).hasSize(databaseSizeBeforeCreate + 1);
        Birim testBirim = birimList.get(birimList.size() - 1);
        assertThat(testBirim.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testBirim.getSoyadi()).isEqualTo(DEFAULT_SOYADI);
    }

    @Test
    @Transactional
    void createBirimWithExistingId() throws Exception {
        // Create the Birim with an existing ID
        birim.setId(1L);

        int databaseSizeBeforeCreate = birimRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBirimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(birim)))
            .andExpect(status().isBadRequest());

        // Validate the Birim in the database
        List<Birim> birimList = birimRepository.findAll();
        assertThat(birimList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAdiIsRequired() throws Exception {
        int databaseSizeBeforeTest = birimRepository.findAll().size();
        // set the field null
        birim.setAdi(null);

        // Create the Birim, which fails.

        restBirimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(birim)))
            .andExpect(status().isBadRequest());

        List<Birim> birimList = birimRepository.findAll();
        assertThat(birimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSoyadiIsRequired() throws Exception {
        int databaseSizeBeforeTest = birimRepository.findAll().size();
        // set the field null
        birim.setSoyadi(null);

        // Create the Birim, which fails.

        restBirimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(birim)))
            .andExpect(status().isBadRequest());

        List<Birim> birimList = birimRepository.findAll();
        assertThat(birimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBirims() throws Exception {
        // Initialize the database
        birimRepository.saveAndFlush(birim);

        // Get all the birimList
        restBirimMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(birim.getId().intValue())))
            .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI)))
            .andExpect(jsonPath("$.[*].soyadi").value(hasItem(DEFAULT_SOYADI)));
    }

    @Test
    @Transactional
    void getBirim() throws Exception {
        // Initialize the database
        birimRepository.saveAndFlush(birim);

        // Get the birim
        restBirimMockMvc
            .perform(get(ENTITY_API_URL_ID, birim.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(birim.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI))
            .andExpect(jsonPath("$.soyadi").value(DEFAULT_SOYADI));
    }

    @Test
    @Transactional
    void getNonExistingBirim() throws Exception {
        // Get the birim
        restBirimMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBirim() throws Exception {
        // Initialize the database
        birimRepository.saveAndFlush(birim);

        int databaseSizeBeforeUpdate = birimRepository.findAll().size();

        // Update the birim
        Birim updatedBirim = birimRepository.findById(birim.getId()).get();
        // Disconnect from session so that the updates on updatedBirim are not directly saved in db
        em.detach(updatedBirim);
        updatedBirim.adi(UPDATED_ADI).soyadi(UPDATED_SOYADI);

        restBirimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBirim.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBirim))
            )
            .andExpect(status().isOk());

        // Validate the Birim in the database
        List<Birim> birimList = birimRepository.findAll();
        assertThat(birimList).hasSize(databaseSizeBeforeUpdate);
        Birim testBirim = birimList.get(birimList.size() - 1);
        assertThat(testBirim.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testBirim.getSoyadi()).isEqualTo(UPDATED_SOYADI);
    }

    @Test
    @Transactional
    void putNonExistingBirim() throws Exception {
        int databaseSizeBeforeUpdate = birimRepository.findAll().size();
        birim.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBirimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, birim.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(birim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Birim in the database
        List<Birim> birimList = birimRepository.findAll();
        assertThat(birimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBirim() throws Exception {
        int databaseSizeBeforeUpdate = birimRepository.findAll().size();
        birim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBirimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(birim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Birim in the database
        List<Birim> birimList = birimRepository.findAll();
        assertThat(birimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBirim() throws Exception {
        int databaseSizeBeforeUpdate = birimRepository.findAll().size();
        birim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBirimMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(birim)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Birim in the database
        List<Birim> birimList = birimRepository.findAll();
        assertThat(birimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBirimWithPatch() throws Exception {
        // Initialize the database
        birimRepository.saveAndFlush(birim);

        int databaseSizeBeforeUpdate = birimRepository.findAll().size();

        // Update the birim using partial update
        Birim partialUpdatedBirim = new Birim();
        partialUpdatedBirim.setId(birim.getId());

        partialUpdatedBirim.adi(UPDATED_ADI);

        restBirimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBirim.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBirim))
            )
            .andExpect(status().isOk());

        // Validate the Birim in the database
        List<Birim> birimList = birimRepository.findAll();
        assertThat(birimList).hasSize(databaseSizeBeforeUpdate);
        Birim testBirim = birimList.get(birimList.size() - 1);
        assertThat(testBirim.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testBirim.getSoyadi()).isEqualTo(DEFAULT_SOYADI);
    }

    @Test
    @Transactional
    void fullUpdateBirimWithPatch() throws Exception {
        // Initialize the database
        birimRepository.saveAndFlush(birim);

        int databaseSizeBeforeUpdate = birimRepository.findAll().size();

        // Update the birim using partial update
        Birim partialUpdatedBirim = new Birim();
        partialUpdatedBirim.setId(birim.getId());

        partialUpdatedBirim.adi(UPDATED_ADI).soyadi(UPDATED_SOYADI);

        restBirimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBirim.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBirim))
            )
            .andExpect(status().isOk());

        // Validate the Birim in the database
        List<Birim> birimList = birimRepository.findAll();
        assertThat(birimList).hasSize(databaseSizeBeforeUpdate);
        Birim testBirim = birimList.get(birimList.size() - 1);
        assertThat(testBirim.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testBirim.getSoyadi()).isEqualTo(UPDATED_SOYADI);
    }

    @Test
    @Transactional
    void patchNonExistingBirim() throws Exception {
        int databaseSizeBeforeUpdate = birimRepository.findAll().size();
        birim.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBirimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, birim.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(birim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Birim in the database
        List<Birim> birimList = birimRepository.findAll();
        assertThat(birimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBirim() throws Exception {
        int databaseSizeBeforeUpdate = birimRepository.findAll().size();
        birim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBirimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(birim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Birim in the database
        List<Birim> birimList = birimRepository.findAll();
        assertThat(birimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBirim() throws Exception {
        int databaseSizeBeforeUpdate = birimRepository.findAll().size();
        birim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBirimMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(birim)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Birim in the database
        List<Birim> birimList = birimRepository.findAll();
        assertThat(birimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBirim() throws Exception {
        // Initialize the database
        birimRepository.saveAndFlush(birim);

        int databaseSizeBeforeDelete = birimRepository.findAll().size();

        // Delete the birim
        restBirimMockMvc
            .perform(delete(ENTITY_API_URL_ID, birim.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Birim> birimList = birimRepository.findAll();
        assertThat(birimList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
