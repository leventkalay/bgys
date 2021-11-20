package com.yesevi.bgysapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yesevi.bgysapp.domain.enumeration.Onay;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Risk.
 */
@Entity
@Table(name = "risk")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Risk implements Serializable {

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

    @ManyToMany
    @JoinTable(name = "rel_risk__varlik", joinColumns = @JoinColumn(name = "risk_id"), inverseJoinColumns = @JoinColumn(name = "varlik_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "kategori", "surec", "ilkSahibi", "ikinciSahibi", "risks" }, allowSetters = true)
    private Set<Varlik> varliks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Risk id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdi() {
        return this.adi;
    }

    public Risk adi(String adi) {
        this.setAdi(adi);
        return this;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getAciklama() {
        return this.aciklama;
    }

    public Risk aciklama(String aciklama) {
        this.setAciklama(aciklama);
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Onay getOnayDurumu() {
        return this.onayDurumu;
    }

    public Risk onayDurumu(Onay onayDurumu) {
        this.setOnayDurumu(onayDurumu);
        return this;
    }

    public void setOnayDurumu(Onay onayDurumu) {
        this.onayDurumu = onayDurumu;
    }

    public Set<Varlik> getVarliks() {
        return this.varliks;
    }

    public void setVarliks(Set<Varlik> varliks) {
        this.varliks = varliks;
    }

    public Risk varliks(Set<Varlik> varliks) {
        this.setVarliks(varliks);
        return this;
    }

    public Risk addVarlik(Varlik varlik) {
        this.varliks.add(varlik);
        varlik.getRisks().add(this);
        return this;
    }

    public Risk removeVarlik(Varlik varlik) {
        this.varliks.remove(varlik);
        varlik.getRisks().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Risk)) {
            return false;
        }
        return id != null && id.equals(((Risk) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Risk{" +
            "id=" + getId() +
            ", adi='" + getAdi() + "'" +
            ", aciklama='" + getAciklama() + "'" +
            ", onayDurumu='" + getOnayDurumu() + "'" +
            "}";
    }
}
