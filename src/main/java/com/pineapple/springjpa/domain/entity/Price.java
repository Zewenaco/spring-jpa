package com.pineapple.springjpa.domain.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRICE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Price {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRICE_GEN")
  @SequenceGenerator(name = "PRICE_GEN", initialValue = 5)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "BRAND_ID")
  private Brand brand;

  @Column(name = "START_DATE", nullable = false)
  private Timestamp startDate;

  @Column(name = "END_DATE", nullable = false)
  private Timestamp endDate;

  @Column(name = "PRICE_LIST", nullable = false)
  private int priceList;

  @Column(name = "PRODUCT_ID", nullable = false)
  private long productId;

  @Column(name = "PRIORITY", nullable = false)
  private int priority;

  @Column(name = "PRICE", nullable = false)
  private BigDecimal price;

  @Column(name = "CURR", length = 3, nullable = false)
  private String currency;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Brand getBrand() {
    return brand;
  }

  public void setBrand(Brand brand) {
    this.brand = brand;
  }

  public Timestamp getStartDate() {
    return startDate;
  }

  public void setStartDate(Timestamp startDate) {
    this.startDate = startDate;
  }

  public Timestamp getEndDate() {
    return endDate;
  }

  public void setEndDate(Timestamp endDate) {
    this.endDate = endDate;
  }

  public int getPriceList() {
    return priceList;
  }

  public void setPriceList(int priceList) {
    this.priceList = priceList;
  }

  public long getProductId() {
    return productId;
  }

  public void setProductId(long productId) {
    this.productId = productId;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }
}
