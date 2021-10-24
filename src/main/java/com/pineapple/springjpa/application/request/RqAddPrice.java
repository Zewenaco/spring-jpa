package com.pineapple.springjpa.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pineapple.springjpa.application.constant.ValidationMessage;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RqAddPrice {

  @NotNull(message = ValidationMessage.NOT_NULL)
  @JsonProperty("price")
  private @Valid PriceDto priceDto;
}
