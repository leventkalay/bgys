package com.yesevi.bgysapp.service.impl;

import com.yesevi.bgysapp.domain.VarlikKategorisi;
import com.yesevi.bgysapp.repository.VarlikKategorisiRepository;
import com.yesevi.bgysapp.service.VarlikKategorisiService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VarlikKategorisi}.
 */
@Service
@Transactional
public class VarlikKategorisiServiceImpl implements VarlikKategorisiService {

    private final Logger log = LoggerFactory.getLogger(VarlikKategorisiServiceImpl.class);

    private final VarlikKategorisiRepository varlikKategorisiRepository;

    public VarlikKategorisiServiceImpl(VarlikKategorisiRepository varlikKategorisiRepository) {
        this.varlikKategorisiRepository = varlikKategorisiRepository;
    }

    @Override
    public VarlikKategorisi save(VarlikKategorisi varlikKategorisi) {
        log.debug("Request to save VarlikKategorisi : {}", varlikKategorisi);
        return varlikKategorisiRepository.save(varlikKategorisi);
    }

    @Override
    public Optional<VarlikKategorisi> partialUpdate(VarlikKategorisi varlikKategorisi) {
        log.debug("Request to partially update VarlikKategorisi : {}", varlikKategorisi);

        return varlikKategorisiRepository
            .findById(varlikKategorisi.getId())
            .map(existingVarlikKategorisi -> {
                if (varlikKategorisi.getAdi() != null) {
                    existingVarlikKategorisi.setAdi(varlikKategorisi.getAdi());
                }
                if (varlikKategorisi.getAciklama() != null) {
                    existingVarlikKategorisi.setAciklama(varlikKategorisi.getAciklama());
                }

                return existingVarlikKategorisi;
            })
            .map(varlikKategorisiRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VarlikKategorisi> findAll(Pageable pageable) {
        log.debug("Request to get all VarlikKategorisis");
        return varlikKategorisiRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VarlikKategorisi> findOne(Long id) {
        log.debug("Request to get VarlikKategorisi : {}", id);
        return varlikKategorisiRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VarlikKategorisi : {}", id);
        varlikKategorisiRepository.deleteById(id);
    }
}
