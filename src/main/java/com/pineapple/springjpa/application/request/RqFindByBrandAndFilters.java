package com.pineapple.springjpa.application.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RqFindByBrandAndFilters {

  private LocalDateTime date;
  private Long productId;
  private Long brandId;
}
