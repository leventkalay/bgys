package com.yesevi.bgysapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IsAkisi.
 */
@Entity
@Table(name = "is_akisi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IsAkisi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "konu")
    private String konu;

    @Column(name = "aciklama")
    private String aciklama;

    @Column(name = "son_tarih")
    private LocalDate sonTarih;

    @ManyToOne
    @JsonIgnoreProperties(value = { "internalUser", "birim" }, allowSetters = true)
    private Personel personel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IsAkisi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKonu() {
        return this.konu;
    }

    public IsAkisi konu(String konu) {
        this.setKonu(konu);
        return this;
    }

    public void setKonu(String konu) {
        this.konu = konu;
    }

    public String getAciklama() {
        return this.aciklama;
    }

    public IsAkisi aciklama(String aciklama) {
        this.setAciklama(aciklama);
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public LocalDate getSonTarih() {
        return this.sonTarih;
    }

    public IsAkisi sonTarih(LocalDate sonTarih) {
        this.setSonTarih(sonTarih);
        return this;
    }

    public void setSonTarih(LocalDate sonTarih) {
        this.sonTarih = sonTarih;
    }

    public Personel getPersonel() {
        return this.personel;
    }

    public void setPersonel(Personel personel) {
        this.personel = personel;
    }

    public IsAkisi personel(Personel personel) {
        this.setPersonel(personel);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IsAkisi)) {
            return false;
        }
        return id != null && id.equals(((IsAkisi) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IsAkisi{" +
            "id=" + getId() +
            ", konu='" + getKonu() + "'" +
            ", aciklama='" + getAciklama() + "'" +
            ", sonTarih='" + getSonTarih() + "'" +
            "}";
    }
}
