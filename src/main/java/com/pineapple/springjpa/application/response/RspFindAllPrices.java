package com.pineapple.springjpa.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pineapple.springjpa.application.request.PriceDto;
import java.time.LocalDateTime;
import java.util.Collection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RspFindAllPrices extends GenericResponse {

  @JsonProperty("data")
  protected Collection<PriceDto> priceDtoCollection;

  public RspFindAllPrices(Collection<PriceDto> priceDtoCollection) {
    super(StatusEnum.SUCCESS.toString(), null, null, null, LocalDateTime.now());
    this.priceDtoCollection = priceDtoCollection;
  }
}
