package com.yesevi.bgysapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.bgysapp.IntegrationTest;
import com.yesevi.bgysapp.domain.VarlikKategorisi;
import com.yesevi.bgysapp.repository.VarlikKategorisiRepository;
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
 * Integration tests for the {@link VarlikKategorisiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VarlikKategorisiResourceIT {

    private static final String DEFAULT_ADI = "AAAAAAAAAA";
    private static final String UPDATED_ADI = "BBBBBBBBBB";

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/varlik-kategorisis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VarlikKategorisiRepository varlikKategorisiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVarlikKategorisiMockMvc;

    private VarlikKategorisi varlikKategorisi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VarlikKategorisi createEntity(EntityManager em) {
        VarlikKategorisi varlikKategorisi = new VarlikKategorisi().adi(DEFAULT_ADI).aciklama(DEFAULT_ACIKLAMA);
        return varlikKategorisi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VarlikKategorisi createUpdatedEntity(EntityManager em) {
        VarlikKategorisi varlikKategorisi = new VarlikKategorisi().adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA);
        return varlikKategorisi;
    }

    @BeforeEach
    public void initTest() {
        varlikKategorisi = createEntity(em);
    }

    @Test
    @Transactional
    void createVarlikKategorisi() throws Exception {
        int databaseSizeBeforeCreate = varlikKategorisiRepository.findAll().size();
        // Create the VarlikKategorisi
        restVarlikKategorisiMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(varlikKategorisi))
            )
            .andExpect(status().isCreated());

        // Validate the VarlikKategorisi in the database
        List<VarlikKategorisi> varlikKategorisiList = varlikKategorisiRepository.findAll();
        assertThat(varlikKategorisiList).hasSize(databaseSizeBeforeCreate + 1);
        VarlikKategorisi testVarlikKategorisi = varlikKategorisiList.get(varlikKategorisiList.size() - 1);
        assertThat(testVarlikKategorisi.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testVarlikKategorisi.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
    }

    @Test
    @Transactional
    void createVarlikKategorisiWithExistingId() throws Exception {
        // Create the VarlikKategorisi with an existing ID
        varlikKategorisi.setId(1L);

        int databaseSizeBeforeCreate = varlikKategorisiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVarlikKategorisiMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(varlikKategorisi))
            )
            .andExpect(status().isBadRequest());

        // Validate the VarlikKategorisi in the database
        List<VarlikKategorisi> varlikKategorisiList = varlikKategorisiRepository.findAll();
        assertThat(varlikKategorisiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVarlikKategorisis() throws Exception {
        // Initialize the database
        varlikKategorisiRepository.saveAndFlush(varlikKategorisi);

        // Get all the varlikKategorisiList
        restVarlikKategorisiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(varlikKategorisi.getId().intValue())))
            .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI)))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA)));
    }

    @Test
    @Transactional
    void getVarlikKategorisi() throws Exception {
        // Initialize the database
        varlikKategorisiRepository.saveAndFlush(varlikKategorisi);

        // Get the varlikKategorisi
        restVarlikKategorisiMockMvc
            .perform(get(ENTITY_API_URL_ID, varlikKategorisi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(varlikKategorisi.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA));
    }

    @Test
    @Transactional
    void getNonExistingVarlikKategorisi() throws Exception {
        // Get the varlikKategorisi
        restVarlikKategorisiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVarlikKategorisi() throws Exception {
        // Initialize the database
        varlikKategorisiRepository.saveAndFlush(varlikKategorisi);

        int databaseSizeBeforeUpdate = varlikKategorisiRepository.findAll().size();

        // Update the varlikKategorisi
        VarlikKategorisi updatedVarlikKategorisi = varlikKategorisiRepository.findById(varlikKategorisi.getId()).get();
        // Disconnect from session so that the updates on updatedVarlikKategorisi are not directly saved in db
        em.detach(updatedVarlikKategorisi);
        updatedVarlikKategorisi.adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA);

        restVarlikKategorisiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVarlikKategorisi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVarlikKategorisi))
            )
            .andExpect(status().isOk());

        // Validate the VarlikKategorisi in the database
        List<VarlikKategorisi> varlikKategorisiList = varlikKategorisiRepository.findAll();
        assertThat(varlikKategorisiList).hasSize(databaseSizeBeforeUpdate);
        VarlikKategorisi testVarlikKategorisi = varlikKategorisiList.get(varlikKategorisiList.size() - 1);
        assertThat(testVarlikKategorisi.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testVarlikKategorisi.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
    }

    @Test
    @Transactional
    void putNonExistingVarlikKategorisi() throws Exception {
        int databaseSizeBeforeUpdate = varlikKategorisiRepository.findAll().size();
        varlikKategorisi.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVarlikKategorisiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, varlikKategorisi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(varlikKategorisi))
            )
            .andExpect(status().isBadRequest());

        // Validate the VarlikKategorisi in the database
        List<VarlikKategorisi> varlikKategorisiList = varlikKategorisiRepository.findAll();
        assertThat(varlikKategorisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVarlikKategorisi() throws Exception {
        int databaseSizeBeforeUpdate = varlikKategorisiRepository.findAll().size();
        varlikKategorisi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVarlikKategorisiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(varlikKategorisi))
            )
            .andExpect(status().isBadRequest());

        // Validate the VarlikKategorisi in the database
        List<VarlikKategorisi> varlikKategorisiList = varlikKategorisiRepository.findAll();
        assertThat(varlikKategorisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVarlikKategorisi() throws Exception {
        int databaseSizeBeforeUpdate = varlikKategorisiRepository.findAll().size();
        varlikKategorisi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVarlikKategorisiMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(varlikKategorisi))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VarlikKategorisi in the database
        List<VarlikKategorisi> varlikKategorisiList = varlikKategorisiRepository.findAll();
        assertThat(varlikKategorisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVarlikKategorisiWithPatch() throws Exception {
        // Initialize the database
        varlikKategorisiRepository.saveAndFlush(varlikKategorisi);

        int databaseSizeBeforeUpdate = varlikKategorisiRepository.findAll().size();

        // Update the varlikKategorisi using partial update
        VarlikKategorisi partialUpdatedVarlikKategorisi = new VarlikKategorisi();
        partialUpdatedVarlikKategorisi.setId(varlikKategorisi.getId());

        partialUpdatedVarlikKategorisi.aciklama(UPDATED_ACIKLAMA);

        restVarlikKategorisiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVarlikKategorisi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVarlikKategorisi))
            )
            .andExpect(status().isOk());

        // Validate the VarlikKategorisi in the database
        List<VarlikKategorisi> varlikKategorisiList = varlikKategorisiRepository.findAll();
        assertThat(varlikKategorisiList).hasSize(databaseSizeBeforeUpdate);
        VarlikKategorisi testVarlikKategorisi = varlikKategorisiList.get(varlikKategorisiList.size() - 1);
        assertThat(testVarlikKategorisi.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testVarlikKategorisi.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
    }

    @Test
    @Transactional
    void fullUpdateVarlikKategorisiWithPatch() throws Exception {
        // Initialize the database
        varlikKategorisiRepository.saveAndFlush(varlikKategorisi);

        int databaseSizeBeforeUpdate = varlikKategorisiRepository.findAll().size();

        // Update the varlikKategorisi using partial update
        VarlikKategorisi partialUpdatedVarlikKategorisi = new VarlikKategorisi();
        partialUpdatedVarlikKategorisi.setId(varlikKategorisi.getId());

        partialUpdatedVarlikKategorisi.adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA);

        restVarlikKategorisiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVarlikKategorisi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVarlikKategorisi))
            )
            .andExpect(status().isOk());

        // Validate the VarlikKategorisi in the database
        List<VarlikKategorisi> varlikKategorisiList = varlikKategorisiRepository.findAll();
        assertThat(varlikKategorisiList).hasSize(databaseSizeBeforeUpdate);
        VarlikKategorisi testVarlikKategorisi = varlikKategorisiList.get(varlikKategorisiList.size() - 1);
        assertThat(testVarlikKategorisi.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testVarlikKategorisi.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
    }

    @Test
    @Transactional
    void patchNonExistingVarlikKategorisi() throws Exception {
        int databaseSizeBeforeUpdate = varlikKategorisiRepository.findAll().size();
        varlikKategorisi.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVarlikKategorisiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, varlikKategorisi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(varlikKategorisi))
            )
            .andExpect(status().isBadRequest());

        // Validate the VarlikKategorisi in the database
        List<VarlikKategorisi> varlikKategorisiList = varlikKategorisiRepository.findAll();
        assertThat(varlikKategorisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVarlikKategorisi() throws Exception {
        int databaseSizeBeforeUpdate = varlikKategorisiRepository.findAll().size();
        varlikKategorisi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVarlikKategorisiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(varlikKategorisi))
            )
            .andExpect(status().isBadRequest());

        // Validate the VarlikKategorisi in the database
        List<VarlikKategorisi> varlikKategorisiList = varlikKategorisiRepository.findAll();
        assertThat(varlikKategorisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVarlikKategorisi() throws Exception {
        int databaseSizeBeforeUpdate = varlikKategorisiRepository.findAll().size();
        varlikKategorisi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVarlikKategorisiMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(varlikKategorisi))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VarlikKategorisi in the database
        List<VarlikKategorisi> varlikKategorisiList = varlikKategorisiRepository.findAll();
        assertThat(varlikKategorisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVarlikKategorisi() throws Exception {
        // Initialize the database
        varlikKategorisiRepository.saveAndFlush(varlikKategorisi);

        int databaseSizeBeforeDelete = varlikKategorisiRepository.findAll().size();

        // Delete the varlikKategorisi
        restVarlikKategorisiMockMvc
            .perform(delete(ENTITY_API_URL_ID, varlikKategorisi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VarlikKategorisi> varlikKategorisiList = varlikKategorisiRepository.findAll();
        assertThat(varlikKategorisiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
