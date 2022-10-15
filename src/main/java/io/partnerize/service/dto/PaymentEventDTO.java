package io.partnerize.service.dto;

import io.partnerize.domain.enumeration.PaymentStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link io.partnerize.domain.PaymentEvent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentEventDTO implements Serializable {

    private Long id;

    private PaymentStatus status;

    private String reason;

    private Instant on;

    private BigDecimal totalAmount;

    private String invoice;

    private PartnerPaymentDTO partnerPayment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getOn() {
        return on;
    }

    public void setOn(Instant on) {
        this.on = on;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public PartnerPaymentDTO getPartnerPayment() {
        return partnerPayment;
    }

    public void setPartnerPayment(PartnerPaymentDTO partnerPayment) {
        this.partnerPayment = partnerPayment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentEventDTO)) {
            return false;
        }

        PaymentEventDTO paymentEventDTO = (PaymentEventDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentEventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentEventDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", reason='" + getReason() + "'" +
            ", on='" + getOn() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", invoice='" + getInvoice() + "'" +
            ", partnerPayment=" + getPartnerPayment() +
            "}";
    }
}
