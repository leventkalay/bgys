package com.yesevi.bgysapp.service.impl;

import com.yesevi.bgysapp.domain.TehditKategorisi;
import com.yesevi.bgysapp.repository.TehditKategorisiRepository;
import com.yesevi.bgysapp.service.TehditKategorisiService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TehditKategorisi}.
 */
@Service
@Transactional
public class TehditKategorisiServiceImpl implements TehditKategorisiService {

    private final Logger log = LoggerFactory.getLogger(TehditKategorisiServiceImpl.class);

    private final TehditKategorisiRepository tehditKategorisiRepository;

    public TehditKategorisiServiceImpl(TehditKategorisiRepository tehditKategorisiRepository) {
        this.tehditKategorisiRepository = tehditKategorisiRepository;
    }

    @Override
    public TehditKategorisi save(TehditKategorisi tehditKategorisi) {
        log.debug("Request to save TehditKategorisi : {}", tehditKategorisi);
        return tehditKategorisiRepository.save(tehditKategorisi);
    }

    @Override
    public Optional<TehditKategorisi> partialUpdate(TehditKategorisi tehditKategorisi) {
        log.debug("Request to partially update TehditKategorisi : {}", tehditKategorisi);

        return tehditKategorisiRepository
            .findById(tehditKategorisi.getId())
            .map(existingTehditKategorisi -> {
                if (tehditKategorisi.getAdi() != null) {
                    existingTehditKategorisi.setAdi(tehditKategorisi.getAdi());
                }
                if (tehditKategorisi.getAciklama() != null) {
                    existingTehditKategorisi.setAciklama(tehditKategorisi.getAciklama());
                }

                return existingTehditKategorisi;
            })
            .map(tehditKategorisiRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TehditKategorisi> findAll(Pageable pageable) {
        log.debug("Request to get all TehditKategorisis");
        return tehditKategorisiRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TehditKategorisi> findOne(Long id) {
        log.debug("Request to get TehditKategorisi : {}", id);
        return tehditKategorisiRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TehditKategorisi : {}", id);
        tehditKategorisiRepository.deleteById(id);
    }
}
