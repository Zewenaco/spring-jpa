package com.pineapple.springjpa.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pineapple.springjpa.application.request.PriceDto;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RspFindPriceById extends GenericResponse {

  @JsonProperty("data")
  protected PriceDto priceDto;

  public RspFindPriceById(PriceDto priceDto) {
    super(StatusEnum.SUCCESS.toString(), null, LocalDateTime.now());
    this.priceDto = priceDto;
  }
}
