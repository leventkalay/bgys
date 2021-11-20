package com.yesevi.bgysapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yesevi.bgysapp.domain.enumeration.Durumu;
import com.yesevi.bgysapp.domain.enumeration.Onay;
import com.yesevi.bgysapp.domain.enumeration.Seviye;
import com.yesevi.bgysapp.domain.enumeration.Siniflandirma;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Varlik.
 */
@Entity
@Table(name = "varlik")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Varlik implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "adi", nullable = false)
    private String adi;

    @Column(name = "yeri")
    private String yeri;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "aciklama")
    private String aciklama;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gizlilik", nullable = false)
    private Seviye gizlilik;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "butunluk", nullable = false)
    private Seviye butunluk;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "erisebilirlik", nullable = false)
    private Seviye erisebilirlik;

    @Enumerated(EnumType.STRING)
    @Column(name = "siniflandirma")
    private Siniflandirma siniflandirma;

    @Enumerated(EnumType.STRING)
    @Column(name = "onay_durumu")
    private Onay onayDurumu;

    @Enumerated(EnumType.STRING)
    @Column(name = "durumu")
    private Durumu durumu;

    @Column(name = "kategori_riskleri")
    private Boolean kategoriRiskleri;

    @ManyToOne
    private VarlikKategorisi kategori;

    @ManyToOne
    private Surec surec;

    @ManyToOne
    @JsonIgnoreProperties(value = { "internalUser", "birim" }, allowSetters = true)
    private Personel ilkSahibi;

    @ManyToOne
    @JsonIgnoreProperties(value = { "internalUser", "birim" }, allowSetters = true)
    private Personel ikinciSahibi;

    @ManyToMany(mappedBy = "varliks")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "varliks" }, allowSetters = true)
    private Set<Risk> risks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Varlik id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdi() {
        return this.adi;
    }

    public Varlik adi(String adi) {
        this.setAdi(adi);
        return this;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getYeri() {
        return this.yeri;
    }

    public Varlik yeri(String yeri) {
        this.setYeri(yeri);
        return this;
    }

    public void setYeri(String yeri) {
        this.yeri = yeri;
    }

    public String getAciklama() {
        return this.aciklama;
    }

    public Varlik aciklama(String aciklama) {
        this.setAciklama(aciklama);
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Seviye getGizlilik() {
        return this.gizlilik;
    }

    public Varlik gizlilik(Seviye gizlilik) {
        this.setGizlilik(gizlilik);
        return this;
    }

    public void setGizlilik(Seviye gizlilik) {
        this.gizlilik = gizlilik;
    }

    public Seviye getButunluk() {
        return this.butunluk;
    }

    public Varlik butunluk(Seviye butunluk) {
        this.setButunluk(butunluk);
        return this;
    }

    public void setButunluk(Seviye butunluk) {
        this.butunluk = butunluk;
    }

    public Seviye getErisebilirlik() {
        return this.erisebilirlik;
    }

    public Varlik erisebilirlik(Seviye erisebilirlik) {
        this.setErisebilirlik(erisebilirlik);
        return this;
    }

    public void setErisebilirlik(Seviye erisebilirlik) {
        this.erisebilirlik = erisebilirlik;
    }

    public Siniflandirma getSiniflandirma() {
        return this.siniflandirma;
    }

    public Varlik siniflandirma(Siniflandirma siniflandirma) {
        this.setSiniflandirma(siniflandirma);
        return this;
    }

    public void setSiniflandirma(Siniflandirma siniflandirma) {
        this.siniflandirma = siniflandirma;
    }

    public Onay getOnayDurumu() {
        return this.onayDurumu;
    }

    public Varlik onayDurumu(Onay onayDurumu) {
        this.setOnayDurumu(onayDurumu);
        return this;
    }

    public void setOnayDurumu(Onay onayDurumu) {
        this.onayDurumu = onayDurumu;
    }

    public Durumu getDurumu() {
        return this.durumu;
    }

    public Varlik durumu(Durumu durumu) {
        this.setDurumu(durumu);
        return this;
    }

    public void setDurumu(Durumu durumu) {
        this.durumu = durumu;
    }

    public Boolean getKategoriRiskleri() {
        return this.kategoriRiskleri;
    }

    public Varlik kategoriRiskleri(Boolean kategoriRiskleri) {
        this.setKategoriRiskleri(kategoriRiskleri);
        return this;
    }

    public void setKategoriRiskleri(Boolean kategoriRiskleri) {
        this.kategoriRiskleri = kategoriRiskleri;
    }

    public VarlikKategorisi getKategori() {
        return this.kategori;
    }

    public void setKategori(VarlikKategorisi varlikKategorisi) {
        this.kategori = varlikKategorisi;
    }

    public Varlik kategori(VarlikKategorisi varlikKategorisi) {
        this.setKategori(varlikKategorisi);
        return this;
    }

    public Surec getSurec() {
        return this.surec;
    }

    public void setSurec(Surec surec) {
        this.surec = surec;
    }

    public Varlik surec(Surec surec) {
        this.setSurec(surec);
        return this;
    }

    public Personel getIlkSahibi() {
        return this.ilkSahibi;
    }

    public void setIlkSahibi(Personel personel) {
        this.ilkSahibi = personel;
    }

    public Varlik ilkSahibi(Personel personel) {
        this.setIlkSahibi(personel);
        return this;
    }

    public Personel getIkinciSahibi() {
        return this.ikinciSahibi;
    }

    public void setIkinciSahibi(Personel personel) {
        this.ikinciSahibi = personel;
    }

    public Varlik ikinciSahibi(Personel personel) {
        this.setIkinciSahibi(personel);
        return this;
    }

    public Set<Risk> getRisks() {
        return this.risks;
    }

    public void setRisks(Set<Risk> risks) {
        if (this.risks != null) {
            this.risks.forEach(i -> i.removeVarlik(this));
        }
        if (risks != null) {
            risks.forEach(i -> i.addVarlik(this));
        }
        this.risks = risks;
    }

    public Varlik risks(Set<Risk> risks) {
        this.setRisks(risks);
        return this;
    }

    public Varlik addRisk(Risk risk) {
        this.risks.add(risk);
        risk.getVarliks().add(this);
        return this;
    }

    public Varlik removeRisk(Risk risk) {
        this.risks.remove(risk);
        risk.getVarliks().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Varlik)) {
            return false;
        }
        return id != null && id.equals(((Varlik) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Varlik{" +
            "id=" + getId() +
            ", adi='" + getAdi() + "'" +
            ", yeri='" + getYeri() + "'" +
            ", aciklama='" + getAciklama() + "'" +
            ", gizlilik='" + getGizlilik() + "'" +
            ", butunluk='" + getButunluk() + "'" +
            ", erisebilirlik='" + getErisebilirlik() + "'" +
            ", siniflandirma='" + getSiniflandirma() + "'" +
            ", onayDurumu='" + getOnayDurumu() + "'" +
            ", durumu='" + getDurumu() + "'" +
            ", kategoriRiskleri='" + getKategoriRiskleri() + "'" +
            "}";
    }
}
