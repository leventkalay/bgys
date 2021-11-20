package com.yesevi.bgysapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Personel.
 */
@Entity
@Table(name = "personel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Personel implements Serializable {

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

    @OneToOne
    @JoinColumn(unique = true)
    private User internalUser;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ustBirim" }, allowSetters = true)
    private Birim birim;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Personel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdi() {
        return this.adi;
    }

    public Personel adi(String adi) {
        this.setAdi(adi);
        return this;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getSoyadi() {
        return this.soyadi;
    }

    public Personel soyadi(String soyadi) {
        this.setSoyadi(soyadi);
        return this;
    }

    public void setSoyadi(String soyadi) {
        this.soyadi = soyadi;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public Personel internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public Birim getBirim() {
        return this.birim;
    }

    public void setBirim(Birim birim) {
        this.birim = birim;
    }

    public Personel birim(Birim birim) {
        this.setBirim(birim);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Personel)) {
            return false;
        }
        return id != null && id.equals(((Personel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Personel{" +
            "id=" + getId() +
            ", adi='" + getAdi() + "'" +
            ", soyadi='" + getSoyadi() + "'" +
            "}";
    }
}
