package com.yesevi.bgysapp.service.impl;

import com.yesevi.bgysapp.domain.Tehdit;
import com.yesevi.bgysapp.repository.TehditRepository;
import com.yesevi.bgysapp.service.TehditService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tehdit}.
 */
@Service
@Transactional
public class TehditServiceImpl implements TehditService {

    private final Logger log = LoggerFactory.getLogger(TehditServiceImpl.class);

    private final TehditRepository tehditRepository;

    public TehditServiceImpl(TehditRepository tehditRepository) {
        this.tehditRepository = tehditRepository;
    }

    @Override
    public Tehdit save(Tehdit tehdit) {
        log.debug("Request to save Tehdit : {}", tehdit);
        return tehditRepository.save(tehdit);
    }

    @Override
    public Optional<Tehdit> partialUpdate(Tehdit tehdit) {
        log.debug("Request to partially update Tehdit : {}", tehdit);

        return tehditRepository
            .findById(tehdit.getId())
            .map(existingTehdit -> {
                if (tehdit.getAdi() != null) {
                    existingTehdit.setAdi(tehdit.getAdi());
                }
                if (tehdit.getAciklama() != null) {
                    existingTehdit.setAciklama(tehdit.getAciklama());
                }
                if (tehdit.getOnayDurumu() != null) {
                    existingTehdit.setOnayDurumu(tehdit.getOnayDurumu());
                }

                return existingTehdit;
            })
            .map(tehditRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Tehdit> findAll(Pageable pageable) {
        log.debug("Request to get all Tehdits");
        return tehditRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tehdit> findOne(Long id) {
        log.debug("Request to get Tehdit : {}", id);
        return tehditRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tehdit : {}", id);
        tehditRepository.deleteById(id);
    }
}
