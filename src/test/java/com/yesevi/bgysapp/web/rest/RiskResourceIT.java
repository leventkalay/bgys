package com.yesevi.bgysapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.bgysapp.IntegrationTest;
import com.yesevi.bgysapp.domain.Risk;
import com.yesevi.bgysapp.domain.enumeration.Onay;
import com.yesevi.bgysapp.repository.RiskRepository;
import com.yesevi.bgysapp.service.RiskService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RiskResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RiskResourceIT {

    private static final String DEFAULT_ADI = "AAAAAAAAAA";
    private static final String UPDATED_ADI = "BBBBBBBBBB";

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final Onay DEFAULT_ONAY_DURUMU = Onay.ONAYLANMADI;
    private static final Onay UPDATED_ONAY_DURUMU = Onay.ONAY_BEKLENMEDE;

    private static final String ENTITY_API_URL = "/api/risks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RiskRepository riskRepository;

    @Mock
    private RiskRepository riskRepositoryMock;

    @Mock
    private RiskService riskServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRiskMockMvc;

    private Risk risk;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Risk createEntity(EntityManager em) {
        Risk risk = new Risk().adi(DEFAULT_ADI).aciklama(DEFAULT_ACIKLAMA).onayDurumu(DEFAULT_ONAY_DURUMU);
        return risk;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Risk createUpdatedEntity(EntityManager em) {
        Risk risk = new Risk().adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA).onayDurumu(UPDATED_ONAY_DURUMU);
        return risk;
    }

    @BeforeEach
    public void initTest() {
        risk = createEntity(em);
    }

    @Test
    @Transactional
    void createRisk() throws Exception {
        int databaseSizeBeforeCreate = riskRepository.findAll().size();
        // Create the Risk
        restRiskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(risk)))
            .andExpect(status().isCreated());

        // Validate the Risk in the database
        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeCreate + 1);
        Risk testRisk = riskList.get(riskList.size() - 1);
        assertThat(testRisk.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testRisk.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testRisk.getOnayDurumu()).isEqualTo(DEFAULT_ONAY_DURUMU);
    }

    @Test
    @Transactional
    void createRiskWithExistingId() throws Exception {
        // Create the Risk with an existing ID
        risk.setId(1L);

        int databaseSizeBeforeCreate = riskRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRiskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(risk)))
            .andExpect(status().isBadRequest());

        // Validate the Risk in the database
        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAdiIsRequired() throws Exception {
        int databaseSizeBeforeTest = riskRepository.findAll().size();
        // set the field null
        risk.setAdi(null);

        // Create the Risk, which fails.

        restRiskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(risk)))
            .andExpect(status().isBadRequest());

        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRisks() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList
        restRiskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(risk.getId().intValue())))
            .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI)))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA)))
            .andExpect(jsonPath("$.[*].onayDurumu").value(hasItem(DEFAULT_ONAY_DURUMU.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRisksWithEagerRelationshipsIsEnabled() throws Exception {
        when(riskServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRiskMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(riskServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRisksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(riskServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRiskMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(riskServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getRisk() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get the risk
        restRiskMockMvc
            .perform(get(ENTITY_API_URL_ID, risk.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(risk.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA))
            .andExpect(jsonPath("$.onayDurumu").value(DEFAULT_ONAY_DURUMU.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRisk() throws Exception {
        // Get the risk
        restRiskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRisk() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        int databaseSizeBeforeUpdate = riskRepository.findAll().size();

        // Update the risk
        Risk updatedRisk = riskRepository.findById(risk.getId()).get();
        // Disconnect from session so that the updates on updatedRisk are not directly saved in db
        em.detach(updatedRisk);
        updatedRisk.adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA).onayDurumu(UPDATED_ONAY_DURUMU);

        restRiskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRisk.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRisk))
            )
            .andExpect(status().isOk());

        // Validate the Risk in the database
        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeUpdate);
        Risk testRisk = riskList.get(riskList.size() - 1);
        assertThat(testRisk.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testRisk.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testRisk.getOnayDurumu()).isEqualTo(UPDATED_ONAY_DURUMU);
    }

    @Test
    @Transactional
    void putNonExistingRisk() throws Exception {
        int databaseSizeBeforeUpdate = riskRepository.findAll().size();
        risk.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRiskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, risk.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(risk))
            )
            .andExpect(status().isBadRequest());

        // Validate the Risk in the database
        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRisk() throws Exception {
        int databaseSizeBeforeUpdate = riskRepository.findAll().size();
        risk.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRiskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(risk))
            )
            .andExpect(status().isBadRequest());

        // Validate the Risk in the database
        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRisk() throws Exception {
        int databaseSizeBeforeUpdate = riskRepository.findAll().size();
        risk.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRiskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(risk)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Risk in the database
        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRiskWithPatch() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        int databaseSizeBeforeUpdate = riskRepository.findAll().size();

        // Update the risk using partial update
        Risk partialUpdatedRisk = new Risk();
        partialUpdatedRisk.setId(risk.getId());

        partialUpdatedRisk.onayDurumu(UPDATED_ONAY_DURUMU);

        restRiskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRisk.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRisk))
            )
            .andExpect(status().isOk());

        // Validate the Risk in the database
        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeUpdate);
        Risk testRisk = riskList.get(riskList.size() - 1);
        assertThat(testRisk.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testRisk.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testRisk.getOnayDurumu()).isEqualTo(UPDATED_ONAY_DURUMU);
    }

    @Test
    @Transactional
    void fullUpdateRiskWithPatch() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        int databaseSizeBeforeUpdate = riskRepository.findAll().size();

        // Update the risk using partial update
        Risk partialUpdatedRisk = new Risk();
        partialUpdatedRisk.setId(risk.getId());

        partialUpdatedRisk.adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA).onayDurumu(UPDATED_ONAY_DURUMU);

        restRiskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRisk.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRisk))
            )
            .andExpect(status().isOk());

        // Validate the Risk in the database
        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeUpdate);
        Risk testRisk = riskList.get(riskList.size() - 1);
        assertThat(testRisk.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testRisk.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testRisk.getOnayDurumu()).isEqualTo(UPDATED_ONAY_DURUMU);
    }

    @Test
    @Transactional
    void patchNonExistingRisk() throws Exception {
        int databaseSizeBeforeUpdate = riskRepository.findAll().size();
        risk.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRiskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, risk.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(risk))
            )
            .andExpect(status().isBadRequest());

        // Validate the Risk in the database
        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRisk() throws Exception {
        int databaseSizeBeforeUpdate = riskRepository.findAll().size();
        risk.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRiskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(risk))
            )
            .andExpect(status().isBadRequest());

        // Validate the Risk in the database
        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRisk() throws Exception {
        int databaseSizeBeforeUpdate = riskRepository.findAll().size();
        risk.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRiskMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(risk)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Risk in the database
        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRisk() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        int databaseSizeBeforeDelete = riskRepository.findAll().size();

        // Delete the risk
        restRiskMockMvc
            .perform(delete(ENTITY_API_URL_ID, risk.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
