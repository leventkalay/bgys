package com.yesevi.bgysapp.service.impl;

import com.yesevi.bgysapp.domain.Aksiyon;
import com.yesevi.bgysapp.repository.AksiyonRepository;
import com.yesevi.bgysapp.service.AksiyonService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Aksiyon}.
 */
@Service
@Transactional
public class AksiyonServiceImpl implements AksiyonService {

    private final Logger log = LoggerFactory.getLogger(AksiyonServiceImpl.class);

    private final AksiyonRepository aksiyonRepository;

    public AksiyonServiceImpl(AksiyonRepository aksiyonRepository) {
        this.aksiyonRepository = aksiyonRepository;
    }

    @Override
    public Aksiyon save(Aksiyon aksiyon) {
        log.debug("Request to save Aksiyon : {}", aksiyon);
        return aksiyonRepository.save(aksiyon);
    }

    @Override
    public Optional<Aksiyon> partialUpdate(Aksiyon aksiyon) {
        log.debug("Request to partially update Aksiyon : {}", aksiyon);

        return aksiyonRepository
            .findById(aksiyon.getId())
            .map(existingAksiyon -> {
                if (aksiyon.getAdi() != null) {
                    existingAksiyon.setAdi(aksiyon.getAdi());
                }
                if (aksiyon.getAciklama() != null) {
                    existingAksiyon.setAciklama(aksiyon.getAciklama());
                }
                if (aksiyon.getOnayDurumu() != null) {
                    existingAksiyon.setOnayDurumu(aksiyon.getOnayDurumu());
                }

                return existingAksiyon;
            })
            .map(aksiyonRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Aksiyon> findAll(Pageable pageable) {
        log.debug("Request to get all Aksiyons");
        return aksiyonRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Aksiyon> findOne(Long id) {
        log.debug("Request to get Aksiyon : {}", id);
        return aksiyonRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Aksiyon : {}", id);
        aksiyonRepository.deleteById(id);
    }
}
