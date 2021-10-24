package com.pineapple.springjpa.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.TreeSet;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RspFindAllPricesByBrandAndFilters extends GenericResponse {

  @JsonProperty("data")
  protected TreeSet<String> stringTreeSet;

  public RspFindAllPricesByBrandAndFilters(TreeSet<String> stringTreeSet) {
    super(StatusEnum.SUCCESS.toString(), null, null, null, LocalDateTime.now());
    this.stringTreeSet = stringTreeSet;
  }
}
