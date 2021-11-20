package com.yesevi.bgysapp.service.impl;

import com.yesevi.bgysapp.domain.Varlik;
import com.yesevi.bgysapp.repository.VarlikRepository;
import com.yesevi.bgysapp.service.VarlikService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Varlik}.
 */
@Service
@Transactional
public class VarlikServiceImpl implements VarlikService {

    private final Logger log = LoggerFactory.getLogger(VarlikServiceImpl.class);

    private final VarlikRepository varlikRepository;

    public VarlikServiceImpl(VarlikRepository varlikRepository) {
        this.varlikRepository = varlikRepository;
    }

    @Override
    public Varlik save(Varlik varlik) {
        log.debug("Request to save Varlik : {}", varlik);
        return varlikRepository.save(varlik);
    }

    @Override
    public Optional<Varlik> partialUpdate(Varlik varlik) {
        log.debug("Request to partially update Varlik : {}", varlik);

        return varlikRepository
            .findById(varlik.getId())
            .map(existingVarlik -> {
                if (varlik.getAdi() != null) {
                    existingVarlik.setAdi(varlik.getAdi());
                }
                if (varlik.getYeri() != null) {
                    existingVarlik.setYeri(varlik.getYeri());
                }
                if (varlik.getAciklama() != null) {
                    existingVarlik.setAciklama(varlik.getAciklama());
                }
                if (varlik.getGizlilik() != null) {
                    existingVarlik.setGizlilik(varlik.getGizlilik());
                }
                if (varlik.getButunluk() != null) {
                    existingVarlik.setButunluk(varlik.getButunluk());
                }
                if (varlik.getErisebilirlik() != null) {
                    existingVarlik.setErisebilirlik(varlik.getErisebilirlik());
                }
                if (varlik.getSiniflandirma() != null) {
                    existingVarlik.setSiniflandirma(varlik.getSiniflandirma());
                }
                if (varlik.getOnayDurumu() != null) {
                    existingVarlik.setOnayDurumu(varlik.getOnayDurumu());
                }
                if (varlik.getDurumu() != null) {
                    existingVarlik.setDurumu(varlik.getDurumu());
                }
                if (varlik.getKategoriRiskleri() != null) {
                    existingVarlik.setKategoriRiskleri(varlik.getKategoriRiskleri());
                }

                return existingVarlik;
            })
            .map(varlikRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Varlik> findAll(Pageable pageable) {
        log.debug("Request to get all Varliks");
        return varlikRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Varlik> findOne(Long id) {
        log.debug("Request to get Varlik : {}", id);
        return varlikRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Varlik : {}", id);
        varlikRepository.deleteById(id);
    }
}
