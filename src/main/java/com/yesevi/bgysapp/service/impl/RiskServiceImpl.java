package com.yesevi.bgysapp.service.impl;

import com.yesevi.bgysapp.domain.Risk;
import com.yesevi.bgysapp.domain.enumeration.Onay;
import com.yesevi.bgysapp.repository.RiskRepository;
import com.yesevi.bgysapp.service.RiskService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Risk}.
 */
@Service
@Transactional
public class RiskServiceImpl implements RiskService {

    private final Logger log = LoggerFactory.getLogger(RiskServiceImpl.class);

    private final RiskRepository riskRepository;

    public RiskServiceImpl(RiskRepository riskRepository) {
        this.riskRepository = riskRepository;
    }

    @Override
    public Risk save(Risk risk) {
        log.debug("Request to save Risk : {}", risk);
        return riskRepository.save(risk);
    }

    @Override
    public Optional<Risk> partialUpdate(Risk risk) {
        log.debug("Request to partially update Risk : {}", risk);

        return riskRepository
            .findById(risk.getId())
            .map(existingRisk -> {
                if (risk.getAdi() != null) {
                    existingRisk.setAdi(risk.getAdi());
                }
                if (risk.getAciklama() != null) {
                    existingRisk.setAciklama(risk.getAciklama());
                }
                if (risk.getOnayDurumu() != null) {
                    existingRisk.setOnayDurumu(risk.getOnayDurumu());
                }

                return existingRisk;
            })
            .map(riskRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Risk> findAll(Pageable pageable) {
        log.debug("Request to get all Risks");
        return riskRepository.findByOnayDurumu(Onay.ONAYLANDI, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Risk> findByOnayDurumu(Pageable pageable) {
        log.debug("Request to get all Risks");
        return riskRepository.findByOnayDurumu(Onay.ONAY_BEKLENMEDE, pageable);
    }

    public Page<Risk> findAllWithEagerRelationships(Pageable pageable) {
        return riskRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Risk> findOne(Long id) {
        log.debug("Request to get Risk : {}", id);
        return riskRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Risk : {}", id);
        riskRepository.deleteById(id);
    }
}
