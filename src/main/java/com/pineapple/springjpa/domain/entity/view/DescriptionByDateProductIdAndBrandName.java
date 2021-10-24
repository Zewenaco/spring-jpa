package com.pineapple.springjpa.domain.entity.view;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface DescriptionByDateProductIdAndBrandName {

  long getProductId();

  long getBrandId();

  String getBrandName();

  Timestamp getStartDate();

  default String getComplexMessage() {
    LocalDateTime localDateTime = getStartDate().toLocalDateTime();
    return String.format(
        "Petición a las %s:%s del día %s del producto %d para la brand %d (%s)",
        DateTimeFormatter.ofPattern("HH").format(localDateTime),
        DateTimeFormatter.ofPattern("mm").format(localDateTime),
        DateTimeFormatter.ofPattern("dd").format(localDateTime),
        getProductId(),
        getBrandId(),
        getBrandName());
  }
}
