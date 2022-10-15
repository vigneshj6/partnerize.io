package io.partnerize.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Partner.
 */
@Entity
@Table(name = "partner")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Partner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

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

    @ManyToOne
    @JsonIgnoreProperties(value = { "card", "partnerPayments", "sales", "contracts", "customers", "partners" }, allowSetters = true)
    private Company company;

    @OneToMany(mappedBy = "partner")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "currency", "customer", "partner", "company" }, allowSetters = true)
    private Set<Sale> sales = new HashSet<>();

    @OneToMany(mappedBy = "partner")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "currency", "partner", "company", "partnerPayments" }, allowSetters = true)
    private Set<Contract> contracts = new HashSet<>();

    @OneToMany(mappedBy = "partner")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "partner", "company", "sales" }, allowSetters = true)
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(mappedBy = "partner")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contract", "partner", "company", "paymentEvents" }, allowSetters = true)
    private Set<PartnerPayment> partnerPayments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Partner id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Partner name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public Partner type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegion() {
        return this.region;
    }

    public Partner region(String region) {
        this.setRegion(region);
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return this.country;
    }

    public Partner country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return this.status;
    }

    public Partner status(String status) {
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

    public Partner card(Card card) {
        this.setCard(card);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Partner company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Set<Sale> getSales() {
        return this.sales;
    }

    public void setSales(Set<Sale> sales) {
        if (this.sales != null) {
            this.sales.forEach(i -> i.setPartner(null));
        }
        if (sales != null) {
            sales.forEach(i -> i.setPartner(this));
        }
        this.sales = sales;
    }

    public Partner sales(Set<Sale> sales) {
        this.setSales(sales);
        return this;
    }

    public Partner addSale(Sale sale) {
        this.sales.add(sale);
        sale.setPartner(this);
        return this;
    }

    public Partner removeSale(Sale sale) {
        this.sales.remove(sale);
        sale.setPartner(null);
        return this;
    }

    public Set<Contract> getContracts() {
        return this.contracts;
    }

    public void setContracts(Set<Contract> contracts) {
        if (this.contracts != null) {
            this.contracts.forEach(i -> i.setPartner(null));
        }
        if (contracts != null) {
            contracts.forEach(i -> i.setPartner(this));
        }
        this.contracts = contracts;
    }

    public Partner contracts(Set<Contract> contracts) {
        this.setContracts(contracts);
        return this;
    }

    public Partner addContract(Contract contract) {
        this.contracts.add(contract);
        contract.setPartner(this);
        return this;
    }

    public Partner removeContract(Contract contract) {
        this.contracts.remove(contract);
        contract.setPartner(null);
        return this;
    }

    public Set<Customer> getCustomers() {
        return this.customers;
    }

    public void setCustomers(Set<Customer> customers) {
        if (this.customers != null) {
            this.customers.forEach(i -> i.setPartner(null));
        }
        if (customers != null) {
            customers.forEach(i -> i.setPartner(this));
        }
        this.customers = customers;
    }

    public Partner customers(Set<Customer> customers) {
        this.setCustomers(customers);
        return this;
    }

    public Partner addCustomer(Customer customer) {
        this.customers.add(customer);
        customer.setPartner(this);
        return this;
    }

    public Partner removeCustomer(Customer customer) {
        this.customers.remove(customer);
        customer.setPartner(null);
        return this;
    }

    public Set<PartnerPayment> getPartnerPayments() {
        return this.partnerPayments;
    }

    public void setPartnerPayments(Set<PartnerPayment> partnerPayments) {
        if (this.partnerPayments != null) {
            this.partnerPayments.forEach(i -> i.setPartner(null));
        }
        if (partnerPayments != null) {
            partnerPayments.forEach(i -> i.setPartner(this));
        }
        this.partnerPayments = partnerPayments;
    }

    public Partner partnerPayments(Set<PartnerPayment> partnerPayments) {
        this.setPartnerPayments(partnerPayments);
        return this;
    }

    public Partner addPartnerPayment(PartnerPayment partnerPayment) {
        this.partnerPayments.add(partnerPayment);
        partnerPayment.setPartner(this);
        return this;
    }

    public Partner removePartnerPayment(PartnerPayment partnerPayment) {
        this.partnerPayments.remove(partnerPayment);
        partnerPayment.setPartner(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Partner)) {
            return false;
        }
        return id != null && id.equals(((Partner) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Partner{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", region='" + getRegion() + "'" +
            ", country='" + getCountry() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
