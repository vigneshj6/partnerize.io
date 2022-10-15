package io.partnerize.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Card.
 */
@Entity
@Table(name = "card")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "auth")
    private String auth;

    @Column(name = "jhi_key")
    private String key;

    @Column(name = "account")
    private String account;

    @JsonIgnoreProperties(value = { "card", "company", "sales", "contracts", "customers", "partnerPayments" }, allowSetters = true)
    @OneToOne(mappedBy = "card")
    private Partner partner;

    @JsonIgnoreProperties(value = { "card", "partnerPayments", "sales", "contracts", "customers", "partners" }, allowSetters = true)
    @OneToOne(mappedBy = "card")
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Card id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return this.token;
    }

    public Card token(String token) {
        this.setToken(token);
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAuth() {
        return this.auth;
    }

    public Card auth(String auth) {
        this.setAuth(auth);
        return this;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getKey() {
        return this.key;
    }

    public Card key(String key) {
        this.setKey(key);
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAccount() {
        return this.account;
    }

    public Card account(String account) {
        this.setAccount(account);
        return this;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Partner getPartner() {
        return this.partner;
    }

    public void setPartner(Partner partner) {
        if (this.partner != null) {
            this.partner.setCard(null);
        }
        if (partner != null) {
            partner.setCard(this);
        }
        this.partner = partner;
    }

    public Card partner(Partner partner) {
        this.setPartner(partner);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        if (this.company != null) {
            this.company.setCard(null);
        }
        if (company != null) {
            company.setCard(this);
        }
        this.company = company;
    }

    public Card company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Card)) {
            return false;
        }
        return id != null && id.equals(((Card) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Card{" +
            "id=" + getId() +
            ", token='" + getToken() + "'" +
            ", auth='" + getAuth() + "'" +
            ", key='" + getKey() + "'" +
            ", account='" + getAccount() + "'" +
            "}";
    }
}
