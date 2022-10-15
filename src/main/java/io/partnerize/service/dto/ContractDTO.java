package io.partnerize.service.dto;

import io.partnerize.domain.enumeration.BillingCycle;
import io.partnerize.domain.enumeration.ContractType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link io.partnerize.domain.Contract} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractDTO implements Serializable {

    private Long id;

    private String name;

    private ContractType type;

    private Instant startAt;

    private Instant endAt;

    private BillingCycle billingCycle;

    private BigDecimal fixedRate;

    private BigDecimal commisionPercent;

    private BigDecimal commisionRate;

    private CurrencyDTO currency;

    private PartnerDTO partner;

    private CompanyDTO company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContractType getType() {
        return type;
    }

    public void setType(ContractType type) {
        this.type = type;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getEndAt() {
        return endAt;
    }

    public void setEndAt(Instant endAt) {
        this.endAt = endAt;
    }

    public BillingCycle getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(BillingCycle billingCycle) {
        this.billingCycle = billingCycle;
    }

    public BigDecimal getFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(BigDecimal fixedRate) {
        this.fixedRate = fixedRate;
    }

    public BigDecimal getCommisionPercent() {
        return commisionPercent;
    }

    public void setCommisionPercent(BigDecimal commisionPercent) {
        this.commisionPercent = commisionPercent;
    }

    public BigDecimal getCommisionRate() {
        return commisionRate;
    }

    public void setCommisionRate(BigDecimal commisionRate) {
        this.commisionRate = commisionRate;
    }

    public CurrencyDTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyDTO currency) {
        this.currency = currency;
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
        if (!(o instanceof ContractDTO)) {
            return false;
        }

        ContractDTO contractDTO = (ContractDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contractDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", startAt='" + getStartAt() + "'" +
            ", endAt='" + getEndAt() + "'" +
            ", billingCycle='" + getBillingCycle() + "'" +
            ", fixedRate=" + getFixedRate() +
            ", commisionPercent=" + getCommisionPercent() +
            ", commisionRate=" + getCommisionRate() +
            ", currency=" + getCurrency() +
            ", partner=" + getPartner() +
            ", company=" + getCompany() +
            "}";
    }
}
