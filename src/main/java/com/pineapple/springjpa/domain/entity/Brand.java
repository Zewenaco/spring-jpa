package com.pineapple.springjpa.domain.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BRAND")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Brand {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "NAME", length = 255, nullable = false)
  private String name;

  @OneToMany(mappedBy = "brand")
  private List<Price> priceList;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Price> getPriceList() {
    return priceList;
  }

  public void setPriceList(List<Price> priceList) {
    this.priceList = priceList;
  }
}
