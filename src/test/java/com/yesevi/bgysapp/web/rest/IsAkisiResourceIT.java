package com.yesevi.bgysapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.bgysapp.IntegrationTest;
import com.yesevi.bgysapp.domain.IsAkisi;
import com.yesevi.bgysapp.repository.IsAkisiRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link IsAkisiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IsAkisiResourceIT {

    private static final String DEFAULT_KONU = "AAAAAAAAAA";
    private static final String UPDATED_KONU = "BBBBBBBBBB";

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SON_TARIH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SON_TARIH = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/is-akisis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IsAkisiRepository isAkisiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIsAkisiMockMvc;

    private IsAkisi isAkisi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IsAkisi createEntity(EntityManager em) {
        IsAkisi isAkisi = new IsAkisi().konu(DEFAULT_KONU).aciklama(DEFAULT_ACIKLAMA).sonTarih(DEFAULT_SON_TARIH);
        return isAkisi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IsAkisi createUpdatedEntity(EntityManager em) {
        IsAkisi isAkisi = new IsAkisi().konu(UPDATED_KONU).aciklama(UPDATED_ACIKLAMA).sonTarih(UPDATED_SON_TARIH);
        return isAkisi;
    }

    @BeforeEach
    public void initTest() {
        isAkisi = createEntity(em);
    }

    @Test
    @Transactional
    void createIsAkisi() throws Exception {
        int databaseSizeBeforeCreate = isAkisiRepository.findAll().size();
        // Create the IsAkisi
        restIsAkisiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(isAkisi)))
            .andExpect(status().isCreated());

        // Validate the IsAkisi in the database
        List<IsAkisi> isAkisiList = isAkisiRepository.findAll();
        assertThat(isAkisiList).hasSize(databaseSizeBeforeCreate + 1);
        IsAkisi testIsAkisi = isAkisiList.get(isAkisiList.size() - 1);
        assertThat(testIsAkisi.getKonu()).isEqualTo(DEFAULT_KONU);
        assertThat(testIsAkisi.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testIsAkisi.getSonTarih()).isEqualTo(DEFAULT_SON_TARIH);
    }

    @Test
    @Transactional
    void createIsAkisiWithExistingId() throws Exception {
        // Create the IsAkisi with an existing ID
        isAkisi.setId(1L);

        int databaseSizeBeforeCreate = isAkisiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIsAkisiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(isAkisi)))
            .andExpect(status().isBadRequest());

        // Validate the IsAkisi in the database
        List<IsAkisi> isAkisiList = isAkisiRepository.findAll();
        assertThat(isAkisiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIsAkisis() throws Exception {
        // Initialize the database
        isAkisiRepository.saveAndFlush(isAkisi);

        // Get all the isAkisiList
        restIsAkisiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(isAkisi.getId().intValue())))
            .andExpect(jsonPath("$.[*].konu").value(hasItem(DEFAULT_KONU)))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA)))
            .andExpect(jsonPath("$.[*].sonTarih").value(hasItem(DEFAULT_SON_TARIH.toString())));
    }

    @Test
    @Transactional
    void getIsAkisi() throws Exception {
        // Initialize the database
        isAkisiRepository.saveAndFlush(isAkisi);

        // Get the isAkisi
        restIsAkisiMockMvc
            .perform(get(ENTITY_API_URL_ID, isAkisi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(isAkisi.getId().intValue()))
            .andExpect(jsonPath("$.konu").value(DEFAULT_KONU))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA))
            .andExpect(jsonPath("$.sonTarih").value(DEFAULT_SON_TARIH.toString()));
    }

    @Test
    @Transactional
    void getNonExistingIsAkisi() throws Exception {
        // Get the isAkisi
        restIsAkisiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIsAkisi() throws Exception {
        // Initialize the database
        isAkisiRepository.saveAndFlush(isAkisi);

        int databaseSizeBeforeUpdate = isAkisiRepository.findAll().size();

        // Update the isAkisi
        IsAkisi updatedIsAkisi = isAkisiRepository.findById(isAkisi.getId()).get();
        // Disconnect from session so that the updates on updatedIsAkisi are not directly saved in db
        em.detach(updatedIsAkisi);
        updatedIsAkisi.konu(UPDATED_KONU).aciklama(UPDATED_ACIKLAMA).sonTarih(UPDATED_SON_TARIH);

        restIsAkisiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIsAkisi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIsAkisi))
            )
            .andExpect(status().isOk());

        // Validate the IsAkisi in the database
        List<IsAkisi> isAkisiList = isAkisiRepository.findAll();
        assertThat(isAkisiList).hasSize(databaseSizeBeforeUpdate);
        IsAkisi testIsAkisi = isAkisiList.get(isAkisiList.size() - 1);
        assertThat(testIsAkisi.getKonu()).isEqualTo(UPDATED_KONU);
        assertThat(testIsAkisi.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testIsAkisi.getSonTarih()).isEqualTo(UPDATED_SON_TARIH);
    }

    @Test
    @Transactional
    void putNonExistingIsAkisi() throws Exception {
        int databaseSizeBeforeUpdate = isAkisiRepository.findAll().size();
        isAkisi.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIsAkisiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, isAkisi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isAkisi))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsAkisi in the database
        List<IsAkisi> isAkisiList = isAkisiRepository.findAll();
        assertThat(isAkisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIsAkisi() throws Exception {
        int databaseSizeBeforeUpdate = isAkisiRepository.findAll().size();
        isAkisi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsAkisiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isAkisi))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsAkisi in the database
        List<IsAkisi> isAkisiList = isAkisiRepository.findAll();
        assertThat(isAkisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIsAkisi() throws Exception {
        int databaseSizeBeforeUpdate = isAkisiRepository.findAll().size();
        isAkisi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsAkisiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(isAkisi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IsAkisi in the database
        List<IsAkisi> isAkisiList = isAkisiRepository.findAll();
        assertThat(isAkisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIsAkisiWithPatch() throws Exception {
        // Initialize the database
        isAkisiRepository.saveAndFlush(isAkisi);

        int databaseSizeBeforeUpdate = isAkisiRepository.findAll().size();

        // Update the isAkisi using partial update
        IsAkisi partialUpdatedIsAkisi = new IsAkisi();
        partialUpdatedIsAkisi.setId(isAkisi.getId());

        partialUpdatedIsAkisi.aciklama(UPDATED_ACIKLAMA);

        restIsAkisiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIsAkisi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIsAkisi))
            )
            .andExpect(status().isOk());

        // Validate the IsAkisi in the database
        List<IsAkisi> isAkisiList = isAkisiRepository.findAll();
        assertThat(isAkisiList).hasSize(databaseSizeBeforeUpdate);
        IsAkisi testIsAkisi = isAkisiList.get(isAkisiList.size() - 1);
        assertThat(testIsAkisi.getKonu()).isEqualTo(DEFAULT_KONU);
        assertThat(testIsAkisi.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testIsAkisi.getSonTarih()).isEqualTo(DEFAULT_SON_TARIH);
    }

    @Test
    @Transactional
    void fullUpdateIsAkisiWithPatch() throws Exception {
        // Initialize the database
        isAkisiRepository.saveAndFlush(isAkisi);

        int databaseSizeBeforeUpdate = isAkisiRepository.findAll().size();

        // Update the isAkisi using partial update
        IsAkisi partialUpdatedIsAkisi = new IsAkisi();
        partialUpdatedIsAkisi.setId(isAkisi.getId());

        partialUpdatedIsAkisi.konu(UPDATED_KONU).aciklama(UPDATED_ACIKLAMA).sonTarih(UPDATED_SON_TARIH);

        restIsAkisiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIsAkisi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIsAkisi))
            )
            .andExpect(status().isOk());

        // Validate the IsAkisi in the database
        List<IsAkisi> isAkisiList = isAkisiRepository.findAll();
        assertThat(isAkisiList).hasSize(databaseSizeBeforeUpdate);
        IsAkisi testIsAkisi = isAkisiList.get(isAkisiList.size() - 1);
        assertThat(testIsAkisi.getKonu()).isEqualTo(UPDATED_KONU);
        assertThat(testIsAkisi.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testIsAkisi.getSonTarih()).isEqualTo(UPDATED_SON_TARIH);
    }

    @Test
    @Transactional
    void patchNonExistingIsAkisi() throws Exception {
        int databaseSizeBeforeUpdate = isAkisiRepository.findAll().size();
        isAkisi.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIsAkisiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, isAkisi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(isAkisi))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsAkisi in the database
        List<IsAkisi> isAkisiList = isAkisiRepository.findAll();
        assertThat(isAkisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIsAkisi() throws Exception {
        int databaseSizeBeforeUpdate = isAkisiRepository.findAll().size();
        isAkisi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsAkisiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(isAkisi))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsAkisi in the database
        List<IsAkisi> isAkisiList = isAkisiRepository.findAll();
        assertThat(isAkisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIsAkisi() throws Exception {
        int databaseSizeBeforeUpdate = isAkisiRepository.findAll().size();
        isAkisi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsAkisiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(isAkisi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IsAkisi in the database
        List<IsAkisi> isAkisiList = isAkisiRepository.findAll();
        assertThat(isAkisiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIsAkisi() throws Exception {
        // Initialize the database
        isAkisiRepository.saveAndFlush(isAkisi);

        int databaseSizeBeforeDelete = isAkisiRepository.findAll().size();

        // Delete the isAkisi
        restIsAkisiMockMvc
            .perform(delete(ENTITY_API_URL_ID, isAkisi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IsAkisi> isAkisiList = isAkisiRepository.findAll();
        assertThat(isAkisiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
