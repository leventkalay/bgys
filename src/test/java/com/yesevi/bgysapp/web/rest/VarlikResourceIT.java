package com.yesevi.bgysapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.bgysapp.IntegrationTest;
import com.yesevi.bgysapp.domain.Varlik;
import com.yesevi.bgysapp.domain.enumeration.Durumu;
import com.yesevi.bgysapp.domain.enumeration.Onay;
import com.yesevi.bgysapp.domain.enumeration.Seviye;
import com.yesevi.bgysapp.domain.enumeration.Seviye;
import com.yesevi.bgysapp.domain.enumeration.Seviye;
import com.yesevi.bgysapp.domain.enumeration.Siniflandirma;
import com.yesevi.bgysapp.repository.VarlikRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link VarlikResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VarlikResourceIT {

    private static final String DEFAULT_ADI = "AAAAAAAAAA";
    private static final String UPDATED_ADI = "BBBBBBBBBB";

    private static final String DEFAULT_YERI = "AAAAAAAAAA";
    private static final String UPDATED_YERI = "BBBBBBBBBB";

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final Seviye DEFAULT_GIZLILIK = Seviye.COK_DUSUK;
    private static final Seviye UPDATED_GIZLILIK = Seviye.DUSUK;

    private static final Seviye DEFAULT_BUTUNLUK = Seviye.COK_DUSUK;
    private static final Seviye UPDATED_BUTUNLUK = Seviye.DUSUK;

    private static final Seviye DEFAULT_ERISEBILIRLIK = Seviye.COK_DUSUK;
    private static final Seviye UPDATED_ERISEBILIRLIK = Seviye.DUSUK;

    private static final Siniflandirma DEFAULT_SINIFLANDIRMA = Siniflandirma.COK_GIZLI;
    private static final Siniflandirma UPDATED_SINIFLANDIRMA = Siniflandirma.GIZLI;

    private static final Onay DEFAULT_ONAY_DURUMU = Onay.ONAYLANMADI;
    private static final Onay UPDATED_ONAY_DURUMU = Onay.ONAY_BEKLENMEDE;

    private static final Durumu DEFAULT_DURUMU = Durumu.AKTIF;
    private static final Durumu UPDATED_DURUMU = Durumu.PASIF;

    private static final Boolean DEFAULT_KATEGORI_RISKLERI = false;
    private static final Boolean UPDATED_KATEGORI_RISKLERI = true;

    private static final String ENTITY_API_URL = "/api/varliks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VarlikRepository varlikRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVarlikMockMvc;

    private Varlik varlik;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Varlik createEntity(EntityManager em) {
        Varlik varlik = new Varlik()
            .adi(DEFAULT_ADI)
            .yeri(DEFAULT_YERI)
            .aciklama(DEFAULT_ACIKLAMA)
            .gizlilik(DEFAULT_GIZLILIK)
            .butunluk(DEFAULT_BUTUNLUK)
            .erisebilirlik(DEFAULT_ERISEBILIRLIK)
            .siniflandirma(DEFAULT_SINIFLANDIRMA)
            .onayDurumu(DEFAULT_ONAY_DURUMU)
            .durumu(DEFAULT_DURUMU)
            .kategoriRiskleri(DEFAULT_KATEGORI_RISKLERI);
        return varlik;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Varlik createUpdatedEntity(EntityManager em) {
        Varlik varlik = new Varlik()
            .adi(UPDATED_ADI)
            .yeri(UPDATED_YERI)
            .aciklama(UPDATED_ACIKLAMA)
            .gizlilik(UPDATED_GIZLILIK)
            .butunluk(UPDATED_BUTUNLUK)
            .erisebilirlik(UPDATED_ERISEBILIRLIK)
            .siniflandirma(UPDATED_SINIFLANDIRMA)
            .onayDurumu(UPDATED_ONAY_DURUMU)
            .durumu(UPDATED_DURUMU)
            .kategoriRiskleri(UPDATED_KATEGORI_RISKLERI);
        return varlik;
    }

    @BeforeEach
    public void initTest() {
        varlik = createEntity(em);
    }

    @Test
    @Transactional
    void createVarlik() throws Exception {
        int databaseSizeBeforeCreate = varlikRepository.findAll().size();
        // Create the Varlik
        restVarlikMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(varlik)))
            .andExpect(status().isCreated());

        // Validate the Varlik in the database
        List<Varlik> varlikList = varlikRepository.findAll();
        assertThat(varlikList).hasSize(databaseSizeBeforeCreate + 1);
        Varlik testVarlik = varlikList.get(varlikList.size() - 1);
        assertThat(testVarlik.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testVarlik.getYeri()).isEqualTo(DEFAULT_YERI);
        assertThat(testVarlik.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testVarlik.getGizlilik()).isEqualTo(DEFAULT_GIZLILIK);
        assertThat(testVarlik.getButunluk()).isEqualTo(DEFAULT_BUTUNLUK);
        assertThat(testVarlik.getErisebilirlik()).isEqualTo(DEFAULT_ERISEBILIRLIK);
        assertThat(testVarlik.getSiniflandirma()).isEqualTo(DEFAULT_SINIFLANDIRMA);
        assertThat(testVarlik.getOnayDurumu()).isEqualTo(DEFAULT_ONAY_DURUMU);
        assertThat(testVarlik.getDurumu()).isEqualTo(DEFAULT_DURUMU);
        assertThat(testVarlik.getKategoriRiskleri()).isEqualTo(DEFAULT_KATEGORI_RISKLERI);
    }

    @Test
    @Transactional
    void createVarlikWithExistingId() throws Exception {
        // Create the Varlik with an existing ID
        varlik.setId(1L);

        int databaseSizeBeforeCreate = varlikRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVarlikMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(varlik)))
            .andExpect(status().isBadRequest());

        // Validate the Varlik in the database
        List<Varlik> varlikList = varlikRepository.findAll();
        assertThat(varlikList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAdiIsRequired() throws Exception {
        int databaseSizeBeforeTest = varlikRepository.findAll().size();
        // set the field null
        varlik.setAdi(null);

        // Create the Varlik, which fails.

        restVarlikMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(varlik)))
            .andExpect(status().isBadRequest());

        List<Varlik> varlikList = varlikRepository.findAll();
        assertThat(varlikList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGizlilikIsRequired() throws Exception {
        int databaseSizeBeforeTest = varlikRepository.findAll().size();
        // set the field null
        varlik.setGizlilik(null);

        // Create the Varlik, which fails.

        restVarlikMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(varlik)))
            .andExpect(status().isBadRequest());

        List<Varlik> varlikList = varlikRepository.findAll();
        assertThat(varlikList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkButunlukIsRequired() throws Exception {
        int databaseSizeBeforeTest = varlikRepository.findAll().size();
        // set the field null
        varlik.setButunluk(null);

        // Create the Varlik, which fails.

        restVarlikMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(varlik)))
            .andExpect(status().isBadRequest());

        List<Varlik> varlikList = varlikRepository.findAll();
        assertThat(varlikList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkErisebilirlikIsRequired() throws Exception {
        int databaseSizeBeforeTest = varlikRepository.findAll().size();
        // set the field null
        varlik.setErisebilirlik(null);

        // Create the Varlik, which fails.

        restVarlikMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(varlik)))
            .andExpect(status().isBadRequest());

        List<Varlik> varlikList = varlikRepository.findAll();
        assertThat(varlikList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVarliks() throws Exception {
        // Initialize the database
        varlikRepository.saveAndFlush(varlik);

        // Get all the varlikList
        restVarlikMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(varlik.getId().intValue())))
            .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI)))
            .andExpect(jsonPath("$.[*].yeri").value(hasItem(DEFAULT_YERI)))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA.toString())))
            .andExpect(jsonPath("$.[*].gizlilik").value(hasItem(DEFAULT_GIZLILIK.toString())))
            .andExpect(jsonPath("$.[*].butunluk").value(hasItem(DEFAULT_BUTUNLUK.toString())))
            .andExpect(jsonPath("$.[*].erisebilirlik").value(hasItem(DEFAULT_ERISEBILIRLIK.toString())))
            .andExpect(jsonPath("$.[*].siniflandirma").value(hasItem(DEFAULT_SINIFLANDIRMA.toString())))
            .andExpect(jsonPath("$.[*].onayDurumu").value(hasItem(DEFAULT_ONAY_DURUMU.toString())))
            .andExpect(jsonPath("$.[*].durumu").value(hasItem(DEFAULT_DURUMU.toString())))
            .andExpect(jsonPath("$.[*].kategoriRiskleri").value(hasItem(DEFAULT_KATEGORI_RISKLERI.booleanValue())));
    }

    @Test
    @Transactional
    void getVarlik() throws Exception {
        // Initialize the database
        varlikRepository.saveAndFlush(varlik);

        // Get the varlik
        restVarlikMockMvc
            .perform(get(ENTITY_API_URL_ID, varlik.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(varlik.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI))
            .andExpect(jsonPath("$.yeri").value(DEFAULT_YERI))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA.toString()))
            .andExpect(jsonPath("$.gizlilik").value(DEFAULT_GIZLILIK.toString()))
            .andExpect(jsonPath("$.butunluk").value(DEFAULT_BUTUNLUK.toString()))
            .andExpect(jsonPath("$.erisebilirlik").value(DEFAULT_ERISEBILIRLIK.toString()))
            .andExpect(jsonPath("$.siniflandirma").value(DEFAULT_SINIFLANDIRMA.toString()))
            .andExpect(jsonPath("$.onayDurumu").value(DEFAULT_ONAY_DURUMU.toString()))
            .andExpect(jsonPath("$.durumu").value(DEFAULT_DURUMU.toString()))
            .andExpect(jsonPath("$.kategoriRiskleri").value(DEFAULT_KATEGORI_RISKLERI.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingVarlik() throws Exception {
        // Get the varlik
        restVarlikMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVarlik() throws Exception {
        // Initialize the database
        varlikRepository.saveAndFlush(varlik);

        int databaseSizeBeforeUpdate = varlikRepository.findAll().size();

        // Update the varlik
        Varlik updatedVarlik = varlikRepository.findById(varlik.getId()).get();
        // Disconnect from session so that the updates on updatedVarlik are not directly saved in db
        em.detach(updatedVarlik);
        updatedVarlik
            .adi(UPDATED_ADI)
            .yeri(UPDATED_YERI)
            .aciklama(UPDATED_ACIKLAMA)
            .gizlilik(UPDATED_GIZLILIK)
            .butunluk(UPDATED_BUTUNLUK)
            .erisebilirlik(UPDATED_ERISEBILIRLIK)
            .siniflandirma(UPDATED_SINIFLANDIRMA)
            .onayDurumu(UPDATED_ONAY_DURUMU)
            .durumu(UPDATED_DURUMU)
            .kategoriRiskleri(UPDATED_KATEGORI_RISKLERI);

        restVarlikMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVarlik.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVarlik))
            )
            .andExpect(status().isOk());

        // Validate the Varlik in the database
        List<Varlik> varlikList = varlikRepository.findAll();
        assertThat(varlikList).hasSize(databaseSizeBeforeUpdate);
        Varlik testVarlik = varlikList.get(varlikList.size() - 1);
        assertThat(testVarlik.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testVarlik.getYeri()).isEqualTo(UPDATED_YERI);
        assertThat(testVarlik.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testVarlik.getGizlilik()).isEqualTo(UPDATED_GIZLILIK);
        assertThat(testVarlik.getButunluk()).isEqualTo(UPDATED_BUTUNLUK);
        assertThat(testVarlik.getErisebilirlik()).isEqualTo(UPDATED_ERISEBILIRLIK);
        assertThat(testVarlik.getSiniflandirma()).isEqualTo(UPDATED_SINIFLANDIRMA);
        assertThat(testVarlik.getOnayDurumu()).isEqualTo(UPDATED_ONAY_DURUMU);
        assertThat(testVarlik.getDurumu()).isEqualTo(UPDATED_DURUMU);
        assertThat(testVarlik.getKategoriRiskleri()).isEqualTo(UPDATED_KATEGORI_RISKLERI);
    }

    @Test
    @Transactional
    void putNonExistingVarlik() throws Exception {
        int databaseSizeBeforeUpdate = varlikRepository.findAll().size();
        varlik.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVarlikMockMvc
            .perform(
                put(ENTITY_API_URL_ID, varlik.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(varlik))
            )
            .andExpect(status().isBadRequest());

        // Validate the Varlik in the database
        List<Varlik> varlikList = varlikRepository.findAll();
        assertThat(varlikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVarlik() throws Exception {
        int databaseSizeBeforeUpdate = varlikRepository.findAll().size();
        varlik.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVarlikMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(varlik))
            )
            .andExpect(status().isBadRequest());

        // Validate the Varlik in the database
        List<Varlik> varlikList = varlikRepository.findAll();
        assertThat(varlikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVarlik() throws Exception {
        int databaseSizeBeforeUpdate = varlikRepository.findAll().size();
        varlik.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVarlikMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(varlik)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Varlik in the database
        List<Varlik> varlikList = varlikRepository.findAll();
        assertThat(varlikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVarlikWithPatch() throws Exception {
        // Initialize the database
        varlikRepository.saveAndFlush(varlik);

        int databaseSizeBeforeUpdate = varlikRepository.findAll().size();

        // Update the varlik using partial update
        Varlik partialUpdatedVarlik = new Varlik();
        partialUpdatedVarlik.setId(varlik.getId());

        partialUpdatedVarlik.adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA).gizlilik(UPDATED_GIZLILIK).butunluk(UPDATED_BUTUNLUK);

        restVarlikMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVarlik.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVarlik))
            )
            .andExpect(status().isOk());

        // Validate the Varlik in the database
        List<Varlik> varlikList = varlikRepository.findAll();
        assertThat(varlikList).hasSize(databaseSizeBeforeUpdate);
        Varlik testVarlik = varlikList.get(varlikList.size() - 1);
        assertThat(testVarlik.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testVarlik.getYeri()).isEqualTo(DEFAULT_YERI);
        assertThat(testVarlik.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testVarlik.getGizlilik()).isEqualTo(UPDATED_GIZLILIK);
        assertThat(testVarlik.getButunluk()).isEqualTo(UPDATED_BUTUNLUK);
        assertThat(testVarlik.getErisebilirlik()).isEqualTo(DEFAULT_ERISEBILIRLIK);
        assertThat(testVarlik.getSiniflandirma()).isEqualTo(DEFAULT_SINIFLANDIRMA);
        assertThat(testVarlik.getOnayDurumu()).isEqualTo(DEFAULT_ONAY_DURUMU);
        assertThat(testVarlik.getDurumu()).isEqualTo(DEFAULT_DURUMU);
        assertThat(testVarlik.getKategoriRiskleri()).isEqualTo(DEFAULT_KATEGORI_RISKLERI);
    }

    @Test
    @Transactional
    void fullUpdateVarlikWithPatch() throws Exception {
        // Initialize the database
        varlikRepository.saveAndFlush(varlik);

        int databaseSizeBeforeUpdate = varlikRepository.findAll().size();

        // Update the varlik using partial update
        Varlik partialUpdatedVarlik = new Varlik();
        partialUpdatedVarlik.setId(varlik.getId());

        partialUpdatedVarlik
            .adi(UPDATED_ADI)
            .yeri(UPDATED_YERI)
            .aciklama(UPDATED_ACIKLAMA)
            .gizlilik(UPDATED_GIZLILIK)
            .butunluk(UPDATED_BUTUNLUK)
            .erisebilirlik(UPDATED_ERISEBILIRLIK)
            .siniflandirma(UPDATED_SINIFLANDIRMA)
            .onayDurumu(UPDATED_ONAY_DURUMU)
            .durumu(UPDATED_DURUMU)
            .kategoriRiskleri(UPDATED_KATEGORI_RISKLERI);

        restVarlikMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVarlik.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVarlik))
            )
            .andExpect(status().isOk());

        // Validate the Varlik in the database
        List<Varlik> varlikList = varlikRepository.findAll();
        assertThat(varlikList).hasSize(databaseSizeBeforeUpdate);
        Varlik testVarlik = varlikList.get(varlikList.size() - 1);
        assertThat(testVarlik.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testVarlik.getYeri()).isEqualTo(UPDATED_YERI);
        assertThat(testVarlik.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testVarlik.getGizlilik()).isEqualTo(UPDATED_GIZLILIK);
        assertThat(testVarlik.getButunluk()).isEqualTo(UPDATED_BUTUNLUK);
        assertThat(testVarlik.getErisebilirlik()).isEqualTo(UPDATED_ERISEBILIRLIK);
        assertThat(testVarlik.getSiniflandirma()).isEqualTo(UPDATED_SINIFLANDIRMA);
        assertThat(testVarlik.getOnayDurumu()).isEqualTo(UPDATED_ONAY_DURUMU);
        assertThat(testVarlik.getDurumu()).isEqualTo(UPDATED_DURUMU);
        assertThat(testVarlik.getKategoriRiskleri()).isEqualTo(UPDATED_KATEGORI_RISKLERI);
    }

    @Test
    @Transactional
    void patchNonExistingVarlik() throws Exception {
        int databaseSizeBeforeUpdate = varlikRepository.findAll().size();
        varlik.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVarlikMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, varlik.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(varlik))
            )
            .andExpect(status().isBadRequest());

        // Validate the Varlik in the database
        List<Varlik> varlikList = varlikRepository.findAll();
        assertThat(varlikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVarlik() throws Exception {
        int databaseSizeBeforeUpdate = varlikRepository.findAll().size();
        varlik.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVarlikMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(varlik))
            )
            .andExpect(status().isBadRequest());

        // Validate the Varlik in the database
        List<Varlik> varlikList = varlikRepository.findAll();
        assertThat(varlikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVarlik() throws Exception {
        int databaseSizeBeforeUpdate = varlikRepository.findAll().size();
        varlik.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVarlikMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(varlik)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Varlik in the database
        List<Varlik> varlikList = varlikRepository.findAll();
        assertThat(varlikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVarlik() throws Exception {
        // Initialize the database
        varlikRepository.saveAndFlush(varlik);

        int databaseSizeBeforeDelete = varlikRepository.findAll().size();

        // Delete the varlik
        restVarlikMockMvc
            .perform(delete(ENTITY_API_URL_ID, varlik.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Varlik> varlikList = varlikRepository.findAll();
        assertThat(varlikList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
