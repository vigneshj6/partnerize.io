package io.partnerize.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sale.
 */
@Entity
@Table(name = "sale")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sale_id")
    private String saleId;

    @Column(name = "invoice")
    private String invoice;

    @Column(name = "total_amount", precision = 21, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "tax", precision = 21, scale = 2)
    private BigDecimal tax;

    @Column(name = "coupon_code")
    private String couponCode;

    @Column(name = "coupon_amount", precision = 21, scale = 2)
    private BigDecimal couponAmount;

    @Column(name = "jhi_on")
    private Instant on;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contracts", "sales" }, allowSetters = true)
    private Currency currency;

    @ManyToOne
    @JsonIgnoreProperties(value = { "partner", "company", "sales" }, allowSetters = true)
    private Customer customer;

    @ManyToOne
    @JsonIgnoreProperties(value = { "card", "company", "sales", "contracts", "customers", "partnerPayments" }, allowSetters = true)
    private Partner partner;

    @ManyToOne
    @JsonIgnoreProperties(value = { "card", "partnerPayments", "sales", "contracts", "customers", "partners" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sale id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSaleId() {
        return this.saleId;
    }

    public Sale saleId(String saleId) {
        this.setSaleId(saleId);
        return this;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getInvoice() {
        return this.invoice;
    }

    public Sale invoice(String invoice) {
        this.setInvoice(invoice);
        return this;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public Sale totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTax() {
        return this.tax;
    }

    public Sale tax(BigDecimal tax) {
        this.setTax(tax);
        return this;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public String getCouponCode() {
        return this.couponCode;
    }

    public Sale couponCode(String couponCode) {
        this.setCouponCode(couponCode);
        return this;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public BigDecimal getCouponAmount() {
        return this.couponAmount;
    }

    public Sale couponAmount(BigDecimal couponAmount) {
        this.setCouponAmount(couponAmount);
        return this;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public Instant getOn() {
        return this.on;
    }

    public Sale on(Instant on) {
        this.setOn(on);
        return this;
    }

    public void setOn(Instant on) {
        this.on = on;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Sale currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Sale customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Partner getPartner() {
        return this.partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Sale partner(Partner partner) {
        this.setPartner(partner);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Sale company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sale)) {
            return false;
        }
        return id != null && id.equals(((Sale) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sale{" +
            "id=" + getId() +
            ", saleId='" + getSaleId() + "'" +
            ", invoice='" + getInvoice() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", tax=" + getTax() +
            ", couponCode='" + getCouponCode() + "'" +
            ", couponAmount=" + getCouponAmount() +
            ", on='" + getOn() + "'" +
            "}";
    }
}
