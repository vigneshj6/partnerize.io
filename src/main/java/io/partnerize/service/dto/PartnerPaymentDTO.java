package io.partnerize.service.dto;

import io.partnerize.domain.enumeration.PaymentStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link io.partnerize.domain.PartnerPayment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartnerPaymentDTO implements Serializable {

    private Long id;

    private String invoice;

    private BigDecimal totalAmount;

    private BigDecimal tax;

    private Instant on;

    private PaymentStatus status;

    private String reason;

    private ContractDTO contract;

    private PartnerDTO partner;

    private CompanyDTO company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public Instant getOn() {
        return on;
    }

    public void setOn(Instant on) {
        this.on = on;
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

    public ContractDTO getContract() {
        return contract;
    }

    public void setContract(ContractDTO contract) {
        this.contract = contract;
    }

    public PartnerDTO getPartner() {
        return partner;
    }

    public void setPartner(PartnerDTO partner) {
        this.partner = partner;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartnerPaymentDTO)) {
            return false;
        }

        PartnerPaymentDTO partnerPaymentDTO = (PartnerPaymentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, partnerPaymentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartnerPaymentDTO{" +
            "id=" + getId() +
            ", invoice='" + getInvoice() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", tax=" + getTax() +
            ", on='" + getOn() + "'" +
            ", status='" + getStatus() + "'" +
            ", reason='" + getReason() + "'" +
            ", contract=" + getContract() +
            ", partner=" + getPartner() +
            ", company=" + getCompany() +
            "}";
    }
}
