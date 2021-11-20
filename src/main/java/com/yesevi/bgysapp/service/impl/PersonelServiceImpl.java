package com.yesevi.bgysapp.service.impl;

import com.yesevi.bgysapp.domain.Personel;
import com.yesevi.bgysapp.repository.PersonelRepository;
import com.yesevi.bgysapp.service.PersonelService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Personel}.
 */
@Service
@Transactional
public class PersonelServiceImpl implements PersonelService {

    private final Logger log = LoggerFactory.getLogger(PersonelServiceImpl.class);

    private final PersonelRepository personelRepository;

    public PersonelServiceImpl(PersonelRepository personelRepository) {
        this.personelRepository = personelRepository;
    }

    @Override
    public Personel save(Personel personel) {
        log.debug("Request to save Personel : {}", personel);
        return personelRepository.save(personel);
    }

    @Override
    public Optional<Personel> partialUpdate(Personel personel) {
        log.debug("Request to partially update Personel : {}", personel);

        return personelRepository
            .findById(personel.getId())
            .map(existingPersonel -> {
                if (personel.getAdi() != null) {
                    existingPersonel.setAdi(personel.getAdi());
                }
                if (personel.getSoyadi() != null) {
                    existingPersonel.setSoyadi(personel.getSoyadi());
                }

                return existingPersonel;
            })
            .map(personelRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Personel> findAll(Pageable pageable) {
        log.debug("Request to get all Personels");
        return personelRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Personel> findOne(Long id) {
        log.debug("Request to get Personel : {}", id);
        return personelRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Personel : {}", id);
        personelRepository.deleteById(id);
    }
}
