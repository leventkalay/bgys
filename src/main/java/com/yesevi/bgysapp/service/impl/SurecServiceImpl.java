package com.yesevi.bgysapp.service.impl;

import com.yesevi.bgysapp.domain.Surec;
import com.yesevi.bgysapp.repository.SurecRepository;
import com.yesevi.bgysapp.service.SurecService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Surec}.
 */
@Service
@Transactional
public class SurecServiceImpl implements SurecService {

    private final Logger log = LoggerFactory.getLogger(SurecServiceImpl.class);

    private final SurecRepository surecRepository;

    public SurecServiceImpl(SurecRepository surecRepository) {
        this.surecRepository = surecRepository;
    }

    @Override
    public Surec save(Surec surec) {
        log.debug("Request to save Surec : {}", surec);
        return surecRepository.save(surec);
    }

    @Override
    public Optional<Surec> partialUpdate(Surec surec) {
        log.debug("Request to partially update Surec : {}", surec);

        return surecRepository
            .findById(surec.getId())
            .map(existingSurec -> {
                if (surec.getAdi() != null) {
                    existingSurec.setAdi(surec.getAdi());
                }
                if (surec.getAciklama() != null) {
                    existingSurec.setAciklama(surec.getAciklama());
                }

                return existingSurec;
            })
            .map(surecRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Surec> findAll(Pageable pageable) {
        log.debug("Request to get all Surecs");
        return surecRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Surec> findOne(Long id) {
        log.debug("Request to get Surec : {}", id);
        return surecRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Surec : {}", id);
        surecRepository.deleteById(id);
    }
}
