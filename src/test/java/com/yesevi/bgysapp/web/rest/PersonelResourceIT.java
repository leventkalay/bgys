package com.yesevi.bgysapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.bgysapp.IntegrationTest;
import com.yesevi.bgysapp.domain.Personel;
import com.yesevi.bgysapp.repository.PersonelRepository;
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
 * Integration tests for the {@link PersonelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonelResourceIT {

    private static final String DEFAULT_ADI = "AAAAAAAAAA";
    private static final String UPDATED_ADI = "BBBBBBBBBB";

    private static final String DEFAULT_SOYADI = "AAAAAAAAAA";
    private static final String UPDATED_SOYADI = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonelRepository personelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonelMockMvc;

    private Personel personel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personel createEntity(EntityManager em) {
        Personel personel = new Personel().adi(DEFAULT_ADI).soyadi(DEFAULT_SOYADI);
        return personel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personel createUpdatedEntity(EntityManager em) {
        Personel personel = new Personel().adi(UPDATED_ADI).soyadi(UPDATED_SOYADI);
        return personel;
    }

    @BeforeEach
    public void initTest() {
        personel = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonel() throws Exception {
        int databaseSizeBeforeCreate = personelRepository.findAll().size();
        // Create the Personel
        restPersonelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personel)))
            .andExpect(status().isCreated());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeCreate + 1);
        Personel testPersonel = personelList.get(personelList.size() - 1);
        assertThat(testPersonel.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testPersonel.getSoyadi()).isEqualTo(DEFAULT_SOYADI);
    }

    @Test
    @Transactional
    void createPersonelWithExistingId() throws Exception {
        // Create the Personel with an existing ID
        personel.setId(1L);

        int databaseSizeBeforeCreate = personelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personel)))
            .andExpect(status().isBadRequest());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAdiIsRequired() throws Exception {
        int databaseSizeBeforeTest = personelRepository.findAll().size();
        // set the field null
        personel.setAdi(null);

        // Create the Personel, which fails.

        restPersonelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personel)))
            .andExpect(status().isBadRequest());

        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSoyadiIsRequired() throws Exception {
        int databaseSizeBeforeTest = personelRepository.findAll().size();
        // set the field null
        personel.setSoyadi(null);

        // Create the Personel, which fails.

        restPersonelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personel)))
            .andExpect(status().isBadRequest());

        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonels() throws Exception {
        // Initialize the database
        personelRepository.saveAndFlush(personel);

        // Get all the personelList
        restPersonelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personel.getId().intValue())))
            .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI)))
            .andExpect(jsonPath("$.[*].soyadi").value(hasItem(DEFAULT_SOYADI)));
    }

    @Test
    @Transactional
    void getPersonel() throws Exception {
        // Initialize the database
        personelRepository.saveAndFlush(personel);

        // Get the personel
        restPersonelMockMvc
            .perform(get(ENTITY_API_URL_ID, personel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personel.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI))
            .andExpect(jsonPath("$.soyadi").value(DEFAULT_SOYADI));
    }

    @Test
    @Transactional
    void getNonExistingPersonel() throws Exception {
        // Get the personel
        restPersonelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonel() throws Exception {
        // Initialize the database
        personelRepository.saveAndFlush(personel);

        int databaseSizeBeforeUpdate = personelRepository.findAll().size();

        // Update the personel
        Personel updatedPersonel = personelRepository.findById(personel.getId()).get();
        // Disconnect from session so that the updates on updatedPersonel are not directly saved in db
        em.detach(updatedPersonel);
        updatedPersonel.adi(UPDATED_ADI).soyadi(UPDATED_SOYADI);

        restPersonelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonel))
            )
            .andExpect(status().isOk());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeUpdate);
        Personel testPersonel = personelList.get(personelList.size() - 1);
        assertThat(testPersonel.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testPersonel.getSoyadi()).isEqualTo(UPDATED_SOYADI);
    }

    @Test
    @Transactional
    void putNonExistingPersonel() throws Exception {
        int databaseSizeBeforeUpdate = personelRepository.findAll().size();
        personel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonel() throws Exception {
        int databaseSizeBeforeUpdate = personelRepository.findAll().size();
        personel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonel() throws Exception {
        int databaseSizeBeforeUpdate = personelRepository.findAll().size();
        personel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonelWithPatch() throws Exception {
        // Initialize the database
        personelRepository.saveAndFlush(personel);

        int databaseSizeBeforeUpdate = personelRepository.findAll().size();

        // Update the personel using partial update
        Personel partialUpdatedPersonel = new Personel();
        partialUpdatedPersonel.setId(personel.getId());

        restPersonelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonel))
            )
            .andExpect(status().isOk());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeUpdate);
        Personel testPersonel = personelList.get(personelList.size() - 1);
        assertThat(testPersonel.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testPersonel.getSoyadi()).isEqualTo(DEFAULT_SOYADI);
    }

    @Test
    @Transactional
    void fullUpdatePersonelWithPatch() throws Exception {
        // Initialize the database
        personelRepository.saveAndFlush(personel);

        int databaseSizeBeforeUpdate = personelRepository.findAll().size();

        // Update the personel using partial update
        Personel partialUpdatedPersonel = new Personel();
        partialUpdatedPersonel.setId(personel.getId());

        partialUpdatedPersonel.adi(UPDATED_ADI).soyadi(UPDATED_SOYADI);

        restPersonelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonel))
            )
            .andExpect(status().isOk());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeUpdate);
        Personel testPersonel = personelList.get(personelList.size() - 1);
        assertThat(testPersonel.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testPersonel.getSoyadi()).isEqualTo(UPDATED_SOYADI);
    }

    @Test
    @Transactional
    void patchNonExistingPersonel() throws Exception {
        int databaseSizeBeforeUpdate = personelRepository.findAll().size();
        personel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonel() throws Exception {
        int databaseSizeBeforeUpdate = personelRepository.findAll().size();
        personel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonel() throws Exception {
        int databaseSizeBeforeUpdate = personelRepository.findAll().size();
        personel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonel() throws Exception {
        // Initialize the database
        personelRepository.saveAndFlush(personel);

        int databaseSizeBeforeDelete = personelRepository.findAll().size();

        // Delete the personel
        restPersonelMockMvc
            .perform(delete(ENTITY_API_URL_ID, personel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
