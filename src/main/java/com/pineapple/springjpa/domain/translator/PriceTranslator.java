package com.pineapple.springjpa.domain.translator;

import com.pineapple.springjpa.application.request.PriceDto;
import com.pineapple.springjpa.domain.entity.Price;
import com.pineapple.springjpa.domain.entity.view.DescriptionByDateProductIdAndBrandName;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class PriceTranslator {

  public Price modifyPriceEntity(Price priceFound, PriceDto priceDto) {
    Optional.ofNullable(priceFound.getBrand())
        .ifPresent(brand -> brand.setId(priceDto.getBrandId()));
    priceFound.setStartDate(this.convertToDateFromLocalDateTime(priceDto.getStartDate()));
    priceFound.setEndDate(this.convertToDateFromLocalDateTime(priceDto.getEndDate()));
    priceFound.setPriceList(priceDto.getPriceList());
    priceFound.setProductId(priceDto.getProductId());
    priceFound.setPriority(priceDto.getPriority());
    priceFound.setPrice(priceDto.getPrice());
    priceFound.setCurrency(priceDto.getCurrency());
    return priceFound;
  }

  public PriceDto buildPriceDto(Price price) {
    return PriceDto.builder()
        .id(price.getId())
        .brandId(price.getBrand().getId())
        .price(price.getPrice())
        .currency(price.getCurrency())
        .priceList(price.getPriceList())
        .endDate(this.convertToLocalDateTimeFromDate(price.getEndDate()))
        .priority(price.getPriority())
        .productId(price.getProductId())
        .startDate(this.convertToLocalDateTimeFromDate(price.getStartDate()))
        .build();
  }

  public PriceDto buildPriceDto(
      DescriptionByDateProductIdAndBrandName descriptionByDateProductIdAndBrandName) {
    return PriceDto.builder()
        .brandId(descriptionByDateProductIdAndBrandName.getBrandId())
        .brandName(descriptionByDateProductIdAndBrandName.getBrandName())
        .price(descriptionByDateProductIdAndBrandName.getPrice())
        .priceList(descriptionByDateProductIdAndBrandName.getPriceList())
        .endDate(
            this.convertToLocalDateTimeFromDate(
                descriptionByDateProductIdAndBrandName.getEndDate()))
        .productId(descriptionByDateProductIdAndBrandName.getProductId())
        .startDate(
            this.convertToLocalDateTimeFromDate(
                descriptionByDateProductIdAndBrandName.getStartDate()))
        .build();
  }

  public Timestamp convertToDateFromLocalDateTime(LocalDateTime localDateTime) {
    return Timestamp.valueOf(localDateTime);
  }

  public LocalDateTime convertToLocalDateTimeFromDate(Timestamp timestamp) {
    return timestamp.toLocalDateTime();
  }
}
