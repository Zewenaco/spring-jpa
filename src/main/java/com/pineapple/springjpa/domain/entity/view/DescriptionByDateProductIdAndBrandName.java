package com.pineapple.springjpa.domain.entity.view;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface DescriptionByDateProductIdAndBrandName {

  long getProductId();

  void setProductId(long productId);

  long getBrandId();

  void setBrandId(long brandId);

  String getBrandName();

  void setBrandName(String brandName);

  Timestamp getStartDate();

  void setStartDate(Timestamp startDate);

  Timestamp getEndDate();

  void setEndDate(Timestamp endDate);

  int getPriceList();

  void setPriceList(long priceList);

  BigDecimal getPrice();

  void setPrice(BigDecimal price);
}
