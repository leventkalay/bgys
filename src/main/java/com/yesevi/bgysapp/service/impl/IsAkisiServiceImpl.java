package com.yesevi.bgysapp.service.impl;

import com.yesevi.bgysapp.domain.IsAkisi;
import com.yesevi.bgysapp.repository.IsAkisiRepository;
import com.yesevi.bgysapp.service.IsAkisiService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IsAkisi}.
 */
@Service
@Transactional
public class IsAkisiServiceImpl implements IsAkisiService {

    private final Logger log = LoggerFactory.getLogger(IsAkisiServiceImpl.class);

    private final IsAkisiRepository isAkisiRepository;

    public IsAkisiServiceImpl(IsAkisiRepository isAkisiRepository) {
        this.isAkisiRepository = isAkisiRepository;
    }

    @Override
    public IsAkisi save(IsAkisi isAkisi) {
        log.debug("Request to save IsAkisi : {}", isAkisi);
        return isAkisiRepository.save(isAkisi);
    }

    @Override
    public Optional<IsAkisi> partialUpdate(IsAkisi isAkisi) {
        log.debug("Request to partially update IsAkisi : {}", isAkisi);

        return isAkisiRepository
            .findById(isAkisi.getId())
            .map(existingIsAkisi -> {
                if (isAkisi.getKonu() != null) {
                    existingIsAkisi.setKonu(isAkisi.getKonu());
                }
                if (isAkisi.getAciklama() != null) {
                    existingIsAkisi.setAciklama(isAkisi.getAciklama());
                }
                if (isAkisi.getSonTarih() != null) {
                    existingIsAkisi.setSonTarih(isAkisi.getSonTarih());
                }

                return existingIsAkisi;
            })
            .map(isAkisiRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IsAkisi> findAll(Pageable pageable) {
        log.debug("Request to get all IsAkisis");
        return isAkisiRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IsAkisi> findOne(Long id) {
        log.debug("Request to get IsAkisi : {}", id);
        return isAkisiRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IsAkisi : {}", id);
        isAkisiRepository.deleteById(id);
    }
}
