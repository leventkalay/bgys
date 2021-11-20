package com.yesevi.bgysapp.service.impl;

import com.yesevi.bgysapp.domain.Birim;
import com.yesevi.bgysapp.repository.BirimRepository;
import com.yesevi.bgysapp.service.BirimService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Birim}.
 */
@Service
@Transactional
public class BirimServiceImpl implements BirimService {

    private final Logger log = LoggerFactory.getLogger(BirimServiceImpl.class);

    private final BirimRepository birimRepository;

    public BirimServiceImpl(BirimRepository birimRepository) {
        this.birimRepository = birimRepository;
    }

    @Override
    public Birim save(Birim birim) {
        log.debug("Request to save Birim : {}", birim);
        return birimRepository.save(birim);
    }

    @Override
    public Optional<Birim> partialUpdate(Birim birim) {
        log.debug("Request to partially update Birim : {}", birim);

        return birimRepository
            .findById(birim.getId())
            .map(existingBirim -> {
                if (birim.getAdi() != null) {
                    existingBirim.setAdi(birim.getAdi());
                }
                if (birim.getSoyadi() != null) {
                    existingBirim.setSoyadi(birim.getSoyadi());
                }

                return existingBirim;
            })
            .map(birimRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Birim> findAll(Pageable pageable) {
        log.debug("Request to get all Birims");
        return birimRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Birim> findOne(Long id) {
        log.debug("Request to get Birim : {}", id);
        return birimRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Birim : {}", id);
        birimRepository.deleteById(id);
    }
}
