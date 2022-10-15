package io.partnerize.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link io.partnerize.domain.Sale} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SaleDTO implements Serializable {

    private Long id;

    private String saleId;

    private String invoice;

    private BigDecimal totalAmount;

    private BigDecimal tax;

    private String couponCode;

    private BigDecimal couponAmount;

    private Instant on;

    private CurrencyDTO currency;

    private CustomerDTO customer;

    private PartnerDTO partner;

    private CompanyDTO company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
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

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public Instant getOn() {
        return on;
    }

    public void setOn(Instant on) {
        this.on = on;
    }

    public CurrencyDTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyDTO currency) {
        this.currency = currency;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
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
        if (!(o instanceof SaleDTO)) {
            return false;
        }

        SaleDTO saleDTO = (SaleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, saleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SaleDTO{" +
            "id=" + getId() +
            ", saleId='" + getSaleId() + "'" +
            ", invoice='" + getInvoice() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", tax=" + getTax() +
            ", couponCode='" + getCouponCode() + "'" +
            ", couponAmount=" + getCouponAmount() +
            ", on='" + getOn() + "'" +
            ", currency=" + getCurrency() +
            ", customer=" + getCustomer() +
            ", partner=" + getPartner() +
            ", company=" + getCompany() +
            "}";
    }
}
