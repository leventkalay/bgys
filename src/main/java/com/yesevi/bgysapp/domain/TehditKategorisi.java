package com.yesevi.bgysapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TehditKategorisi.
 */
@Entity
@Table(name = "tehdit_kategorisi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TehditKategorisi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "adi")
    private String adi;

    @Column(name = "aciklama")
    private String aciklama;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TehditKategorisi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdi() {
        return this.adi;
    }

    public TehditKategorisi adi(String adi) {
        this.setAdi(adi);
        return this;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getAciklama() {
        return this.aciklama;
    }

    public TehditKategorisi aciklama(String aciklama) {
        this.setAciklama(aciklama);
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TehditKategorisi)) {
            return false;
        }
        return id != null && id.equals(((TehditKategorisi) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TehditKategorisi{" +
            "id=" + getId() +
            ", adi='" + getAdi() + "'" +
            ", aciklama='" + getAciklama() + "'" +
            "}";
    }
}
