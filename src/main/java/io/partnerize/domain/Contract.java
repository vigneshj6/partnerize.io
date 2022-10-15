package io.partnerize.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.partnerize.domain.enumeration.BillingCycle;
import io.partnerize.domain.enumeration.ContractType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ContractType type;

    @Column(name = "start_at")
    private Instant startAt;

    @Column(name = "end_at")
    private Instant endAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle")
    private BillingCycle billingCycle;

    @Column(name = "fixed_rate", precision = 21, scale = 2)
    private BigDecimal fixedRate;

    @Column(name = "commision_percent", precision = 21, scale = 2)
    private BigDecimal commisionPercent;

    @Column(name = "commision_rate", precision = 21, scale = 2)
    private BigDecimal commisionRate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contracts", "sales" }, allowSetters = true)
    private Currency currency;

    @ManyToOne
    @JsonIgnoreProperties(value = { "card", "company", "sales", "contracts", "customers", "partnerPayments" }, allowSetters = true)
    private Partner partner;

    @ManyToOne
    @JsonIgnoreProperties(value = { "card", "partnerPayments", "sales", "contracts", "customers", "partners" }, allowSetters = true)
    private Company company;

    @OneToMany(mappedBy = "contract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contract", "partner", "company", "paymentEvents" }, allowSetters = true)
    private Set<PartnerPayment> partnerPayments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contract id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Contract name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContractType getType() {
        return this.type;
    }

    public Contract type(ContractType type) {
        this.setType(type);
        return this;
    }

    public void setType(ContractType type) {
        this.type = type;
    }

    public Instant getStartAt() {
        return this.startAt;
    }

    public Contract startAt(Instant startAt) {
        this.setStartAt(startAt);
        return this;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getEndAt() {
        return this.endAt;
    }

    public Contract endAt(Instant endAt) {
        this.setEndAt(endAt);
        return this;
    }

    public void setEndAt(Instant endAt) {
        this.endAt = endAt;
    }

    public BillingCycle getBillingCycle() {
        return this.billingCycle;
    }

    public Contract billingCycle(BillingCycle billingCycle) {
        this.setBillingCycle(billingCycle);
        return this;
    }

    public void setBillingCycle(BillingCycle billingCycle) {
        this.billingCycle = billingCycle;
    }

    public BigDecimal getFixedRate() {
        return this.fixedRate;
    }

    public Contract fixedRate(BigDecimal fixedRate) {
        this.setFixedRate(fixedRate);
        return this;
    }

    public void setFixedRate(BigDecimal fixedRate) {
        this.fixedRate = fixedRate;
    }

    public BigDecimal getCommisionPercent() {
        return this.commisionPercent;
    }

    public Contract commisionPercent(BigDecimal commisionPercent) {
        this.setCommisionPercent(commisionPercent);
        return this;
    }

    public void setCommisionPercent(BigDecimal commisionPercent) {
        this.commisionPercent = commisionPercent;
    }

    public BigDecimal getCommisionRate() {
        return this.commisionRate;
    }

    public Contract commisionRate(BigDecimal commisionRate) {
        this.setCommisionRate(commisionRate);
        return this;
    }

    public void setCommisionRate(BigDecimal commisionRate) {
        this.commisionRate = commisionRate;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Contract currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    public Partner getPartner() {
        return this.partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Contract partner(Partner partner) {
        this.setPartner(partner);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Contract company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Set<PartnerPayment> getPartnerPayments() {
        return this.partnerPayments;
    }

    public void setPartnerPayments(Set<PartnerPayment> partnerPayments) {
        if (this.partnerPayments != null) {
            this.partnerPayments.forEach(i -> i.setContract(null));
        }
        if (partnerPayments != null) {
            partnerPayments.forEach(i -> i.setContract(this));
        }
        this.partnerPayments = partnerPayments;
    }

    public Contract partnerPayments(Set<PartnerPayment> partnerPayments) {
        this.setPartnerPayments(partnerPayments);
        return this;
    }

    public Contract addPartnerPayment(PartnerPayment partnerPayment) {
        this.partnerPayments.add(partnerPayment);
        partnerPayment.setContract(this);
        return this;
    }

    public Contract removePartnerPayment(PartnerPayment partnerPayment) {
        this.partnerPayments.remove(partnerPayment);
        partnerPayment.setContract(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contract)) {
            return false;
        }
        return id != null && id.equals(((Contract) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contract{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", startAt='" + getStartAt() + "'" +
            ", endAt='" + getEndAt() + "'" +
            ", billingCycle='" + getBillingCycle() + "'" +
            ", fixedRate=" + getFixedRate() +
            ", commisionPercent=" + getCommisionPercent() +
            ", commisionRate=" + getCommisionRate() +
            "}";
    }
}
