package com.pineapple.springjpa.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pineapple.springjpa.application.request.PriceDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RspFindAllPricesByBrandAndFilters extends GenericResponse {

  @JsonProperty("data")
  protected List<PriceDto> priceDtoList;

  public RspFindAllPricesByBrandAndFilters(List<PriceDto> priceDtoList) {
    super(StatusEnum.SUCCESS.toString(), null, LocalDateTime.now());
    this.priceDtoList = priceDtoList;
  }
}
