package io.partnerize.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.partnerize.domain.enumeration.PaymentStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PartnerPayment.
 */
@Entity
@Table(name = "partner_payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartnerPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "invoice")
    private String invoice;

    @Column(name = "total_amount", precision = 21, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "tax", precision = 21, scale = 2)
    private BigDecimal tax;

    @Column(name = "jhi_on")
    private Instant on;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

    @Column(name = "reason")
    private String reason;

    @ManyToOne
    @JsonIgnoreProperties(value = { "currency", "partner", "company", "partnerPayments" }, allowSetters = true)
    private Contract contract;

    @ManyToOne
    @JsonIgnoreProperties(value = { "card", "company", "sales", "contracts", "customers", "partnerPayments" }, allowSetters = true)
    private Partner partner;

    @ManyToOne
    @JsonIgnoreProperties(value = { "card", "partnerPayments", "sales", "contracts", "customers", "partners" }, allowSetters = true)
    private Company company;

    @OneToMany(mappedBy = "partnerPayment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "partnerPayment" }, allowSetters = true)
    private Set<PaymentEvent> paymentEvents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PartnerPayment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoice() {
        return this.invoice;
    }

    public PartnerPayment invoice(String invoice) {
        this.setInvoice(invoice);
        return this;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public PartnerPayment totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTax() {
        return this.tax;
    }

    public PartnerPayment tax(BigDecimal tax) {
        this.setTax(tax);
        return this;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public Instant getOn() {
        return this.on;
    }

    public PartnerPayment on(Instant on) {
        this.setOn(on);
        return this;
    }

    public void setOn(Instant on) {
        this.on = on;
    }

    public PaymentStatus getStatus() {
        return this.status;
    }

    public PartnerPayment status(PaymentStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getReason() {
        return this.reason;
    }

    public PartnerPayment reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public PartnerPayment contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    public Partner getPartner() {
        return this.partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public PartnerPayment partner(Partner partner) {
        this.setPartner(partner);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public PartnerPayment company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Set<PaymentEvent> getPaymentEvents() {
        return this.paymentEvents;
    }

    public void setPaymentEvents(Set<PaymentEvent> paymentEvents) {
        if (this.paymentEvents != null) {
            this.paymentEvents.forEach(i -> i.setPartnerPayment(null));
        }
        if (paymentEvents != null) {
            paymentEvents.forEach(i -> i.setPartnerPayment(this));
        }
        this.paymentEvents = paymentEvents;
    }

    public PartnerPayment paymentEvents(Set<PaymentEvent> paymentEvents) {
        this.setPaymentEvents(paymentEvents);
        return this;
    }

    public PartnerPayment addPaymentEvent(PaymentEvent paymentEvent) {
        this.paymentEvents.add(paymentEvent);
        paymentEvent.setPartnerPayment(this);
        return this;
    }

    public PartnerPayment removePaymentEvent(PaymentEvent paymentEvent) {
        this.paymentEvents.remove(paymentEvent);
        paymentEvent.setPartnerPayment(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartnerPayment)) {
            return false;
        }
        return id != null && id.equals(((PartnerPayment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartnerPayment{" +
            "id=" + getId() +
            ", invoice='" + getInvoice() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", tax=" + getTax() +
            ", on='" + getOn() + "'" +
            ", status='" + getStatus() + "'" +
            ", reason='" + getReason() + "'" +
            "}";
    }
}
