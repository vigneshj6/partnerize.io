package io.partnerize.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.partnerize.domain.enumeration.PaymentStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentEvent.
 */
@Entity
@Table(name = "payment_event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

    @Column(name = "reason")
    private String reason;

    @Column(name = "jhi_on")
    private Instant on;

    @Column(name = "total_amount", precision = 21, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "invoice")
    private String invoice;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contract", "partner", "company", "paymentEvents" }, allowSetters = true)
    private PartnerPayment partnerPayment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentEvent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentStatus getStatus() {
        return this.status;
    }

    public PaymentEvent status(PaymentStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getReason() {
        return this.reason;
    }

    public PaymentEvent reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getOn() {
        return this.on;
    }

    public PaymentEvent on(Instant on) {
        this.setOn(on);
        return this;
    }

    public void setOn(Instant on) {
        this.on = on;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public PaymentEvent totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getInvoice() {
        return this.invoice;
    }

    public PaymentEvent invoice(String invoice) {
        this.setInvoice(invoice);
        return this;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public PartnerPayment getPartnerPayment() {
        return this.partnerPayment;
    }

    public void setPartnerPayment(PartnerPayment partnerPayment) {
        this.partnerPayment = partnerPayment;
    }

    public PaymentEvent partnerPayment(PartnerPayment partnerPayment) {
        this.setPartnerPayment(partnerPayment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentEvent)) {
            return false;
        }
        return id != null && id.equals(((PaymentEvent) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentEvent{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", reason='" + getReason() + "'" +
            ", on='" + getOn() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", invoice='" + getInvoice() + "'" +
            "}";
    }
}
