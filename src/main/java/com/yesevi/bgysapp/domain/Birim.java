package com.yesevi.bgysapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Birim.
 */
@Entity
@Table(name = "birim")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Birim implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "adi", nullable = false)
    private String adi;

    @NotNull
    @Column(name = "soyadi", nullable = false)
    private String soyadi;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ustBirim" }, allowSetters = true)
    private Birim ustBirim;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Birim id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdi() {
        return this.adi;
    }

    public Birim adi(String adi) {
        this.setAdi(adi);
        return this;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getSoyadi() {
        return this.soyadi;
    }

    public Birim soyadi(String soyadi) {
        this.setSoyadi(soyadi);
        return this;
    }

    public void setSoyadi(String soyadi) {
        this.soyadi = soyadi;
    }

    public Birim getUstBirim() {
        return this.ustBirim;
    }

    public void setUstBirim(Birim birim) {
        this.ustBirim = birim;
    }

    public Birim ustBirim(Birim birim) {
        this.setUstBirim(birim);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Birim)) {
            return false;
        }
        return id != null && id.equals(((Birim) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Birim{" +
            "id=" + getId() +
            ", adi='" + getAdi() + "'" +
            ", soyadi='" + getSoyadi() + "'" +
            "}";
    }
}
