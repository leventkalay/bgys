package com.yesevi.bgysapp.domain;

import com.yesevi.bgysapp.domain.enumeration.Onay;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tehdit.
 */
@Entity
@Table(name = "tehdit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tehdit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "adi", nullable = false)
    private String adi;

    @Column(name = "aciklama")
    private String aciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "onay_durumu")
    private Onay onayDurumu;

    @ManyToOne
    private TehditKategorisi kategori;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tehdit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdi() {
        return this.adi;
    }

    public Tehdit adi(String adi) {
        this.setAdi(adi);
        return this;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getAciklama() {
        return this.aciklama;
    }

    public Tehdit aciklama(String aciklama) {
        this.setAciklama(aciklama);
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Onay getOnayDurumu() {
        return this.onayDurumu;
    }

    public Tehdit onayDurumu(Onay onayDurumu) {
        this.setOnayDurumu(onayDurumu);
        return this;
    }

    public void setOnayDurumu(Onay onayDurumu) {
        this.onayDurumu = onayDurumu;
    }

    public TehditKategorisi getKategori() {
        return this.kategori;
    }

    public void setKategori(TehditKategorisi tehditKategorisi) {
        this.kategori = tehditKategorisi;
    }

    public Tehdit kategori(TehditKategorisi tehditKategorisi) {
        this.setKategori(tehditKategorisi);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tehdit)) {
            return false;
        }
        return id != null && id.equals(((Tehdit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tehdit{" +
            "id=" + getId() +
            ", adi='" + getAdi() + "'" +
            ", aciklama='" + getAciklama() + "'" +
            ", onayDurumu='" + getOnayDurumu() + "'" +
            "}";
    }
}
