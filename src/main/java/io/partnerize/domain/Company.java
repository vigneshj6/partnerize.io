package io.partnerize.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "region")
    private String region;

    @Column(name = "country")
    private String country;

    @Column(name = "status")
    private String status;

    @JsonIgnoreProperties(value = { "partner", "company" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Card card;

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contract", "partner", "company", "paymentEvents" }, allowSetters = true)
    private Set<PartnerPayment> partnerPayments = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "currency", "customer", "partner", "company" }, allowSetters = true)
    private Set<Sale> sales = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "currency", "partner", "company", "partnerPayments" }, allowSetters = true)
    private Set<Contract> contracts = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "partner", "company", "sales" }, allowSetters = true)
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "card", "company", "sales", "contracts", "customers", "partnerPayments" }, allowSetters = true)
    private Set<Partner> partners = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Company id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Company name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return this.region;
    }

    public Company region(String region) {
        this.setRegion(region);
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return this.country;
    }

    public Company country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return this.status;
    }

    public Company status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Card getCard() {
        return this.card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Company card(Card card) {
        this.setCard(card);
        return this;
    }

    public Set<PartnerPayment> getPartnerPayments() {
        return this.partnerPayments;
    }

    public void setPartnerPayments(Set<PartnerPayment> partnerPayments) {
        if (this.partnerPayments != null) {
            this.partnerPayments.forEach(i -> i.setCompany(null));
        }
        if (partnerPayments != null) {
            partnerPayments.forEach(i -> i.setCompany(this));
        }
        this.partnerPayments = partnerPayments;
    }

    public Company partnerPayments(Set<PartnerPayment> partnerPayments) {
        this.setPartnerPayments(partnerPayments);
        return this;
    }

    public Company addPartnerPayment(PartnerPayment partnerPayment) {
        this.partnerPayments.add(partnerPayment);
        partnerPayment.setCompany(this);
        return this;
    }

    public Company removePartnerPayment(PartnerPayment partnerPayment) {
        this.partnerPayments.remove(partnerPayment);
        partnerPayment.setCompany(null);
        return this;
    }

    public Set<Sale> getSales() {
        return this.sales;
    }

    public void setSales(Set<Sale> sales) {
        if (this.sales != null) {
            this.sales.forEach(i -> i.setCompany(null));
        }
        if (sales != null) {
            sales.forEach(i -> i.setCompany(this));
        }
        this.sales = sales;
    }

    public Company sales(Set<Sale> sales) {
        this.setSales(sales);
        return this;
    }

    public Company addSale(Sale sale) {
        this.sales.add(sale);
        sale.setCompany(this);
        return this;
    }

    public Company removeSale(Sale sale) {
        this.sales.remove(sale);
        sale.setCompany(null);
        return this;
    }

    public Set<Contract> getContracts() {
        return this.contracts;
    }

    public void setContracts(Set<Contract> contracts) {
        if (this.contracts != null) {
            this.contracts.forEach(i -> i.setCompany(null));
        }
        if (contracts != null) {
            contracts.forEach(i -> i.setCompany(this));
        }
        this.contracts = contracts;
    }

    public Company contracts(Set<Contract> contracts) {
        this.setContracts(contracts);
        return this;
    }

    public Company addContract(Contract contract) {
        this.contracts.add(contract);
        contract.setCompany(this);
        return this;
    }

    public Company removeContract(Contract contract) {
        this.contracts.remove(contract);
        contract.setCompany(null);
        return this;
    }

    public Set<Customer> getCustomers() {
        return this.customers;
    }

    public void setCustomers(Set<Customer> customers) {
        if (this.customers != null) {
            this.customers.forEach(i -> i.setCompany(null));
        }
        if (customers != null) {
            customers.forEach(i -> i.setCompany(this));
        }
        this.customers = customers;
    }

    public Company customers(Set<Customer> customers) {
        this.setCustomers(customers);
        return this;
    }

    public Company addCustomer(Customer customer) {
        this.customers.add(customer);
        customer.setCompany(this);
        return this;
    }

    public Company removeCustomer(Customer customer) {
        this.customers.remove(customer);
        customer.setCompany(null);
        return this;
    }

    public Set<Partner> getPartners() {
        return this.partners;
    }

    public void setPartners(Set<Partner> partners) {
        if (this.partners != null) {
            this.partners.forEach(i -> i.setCompany(null));
        }
        if (partners != null) {
            partners.forEach(i -> i.setCompany(this));
        }
        this.partners = partners;
    }

    public Company partners(Set<Partner> partners) {
        this.setPartners(partners);
        return this;
    }

    public Company addPartner(Partner partner) {
        this.partners.add(partner);
        partner.setCompany(this);
        return this;
    }

    public Company removePartner(Partner partner) {
        this.partners.remove(partner);
        partner.setCompany(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", region='" + getRegion() + "'" +
            ", country='" + getCountry() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
