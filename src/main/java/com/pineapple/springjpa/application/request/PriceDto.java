package com.pineapple.springjpa.application.request;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.pineapple.springjpa.application.constant.ValidationMessage;
import com.pineapple.springjpa.application.view.PriceView;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceDto {

  @JsonView({PriceView.Find.Response.class})
  private Long id;

  @NotNull(message = ValidationMessage.NOT_NULL)
  @Min(value = 1, message = ValidationMessage.NUMERIC_GREATER_THAN_ZERO)
  @ApiModelProperty(notes = "Primary Key of Brand entity", example = "1", dataType = "Long")
  private Long brandId;

  @JsonView({PriceView.Find.ResponseByBrandIdAndFilters.class})
  private String brandName;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @NotNull(message = ValidationMessage.NOT_NULL)
  @ApiModelProperty(
      notes = "Date on which the price starts to be applied",
      example = "2020-06-14T00:00:00")
  private LocalDateTime startDate;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @NotNull(message = ValidationMessage.NOT_NULL)
  @ApiModelProperty(notes = "Date on which the price end", example = "2020-12-31T23:59:59")
  private LocalDateTime endDate;

  @NotNull(message = ValidationMessage.NOT_NULL)
  @Min(value = 1, message = ValidationMessage.NUMERIC_GREATER_THAN_ZERO)
  @ApiModelProperty(
      notes = "Identifier of tax price to be apply",
      example = "1",
      dataType = "Integer")
  private Integer priceList;

  @NotNull(message = ValidationMessage.NOT_NULL)
  @Min(value = 1, message = ValidationMessage.NUMERIC_GREATER_THAN_ZERO)
  @ApiModelProperty(
      notes = "Identifier related to primary key of product",
      example = "1",
      dataType = "Long")
  private Long productId;

  @NotNull(message = ValidationMessage.NOT_NULL)
  @Min(value = 0, message = ValidationMessage.NUMERIC_GREATER_OR_EQUAL_THAN_ZERO)
  @ApiModelProperty(
      notes = "Number that identified the price to be applied",
      example = "1",
      dataType = "Integer")
  private Integer priority;

  @NotNull(message = ValidationMessage.NOT_NULL)
  @Min(value = 0, message = ValidationMessage.NUMERIC_GREATER_OR_EQUAL_THAN_ZERO)
  @ApiModelProperty(notes = "Price of the product", example = "35.50", dataType = "BigDecimal")
  private BigDecimal price;

  @NotEmpty(message = ValidationMessage.NOT_EMPTY)
  @Length(min = 3, max = 3)
  @Pattern(regexp = "^[A-Z][A-Z]{2}$", message = ValidationMessage.CURRENCY_VALIDATION)
  @ApiModelProperty(notes = "Currency related to the price", example = "EUR")
  private String currency;
}
