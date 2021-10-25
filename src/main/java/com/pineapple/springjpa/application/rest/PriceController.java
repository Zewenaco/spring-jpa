package com.pineapple.springjpa.application.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.pineapple.springjpa.application.constant.ValidationMessage;
import com.pineapple.springjpa.application.request.RqAddPrice;
import com.pineapple.springjpa.application.request.RqFindByBrandAndFilters;
import com.pineapple.springjpa.application.request.RqModifyPrice;
import com.pineapple.springjpa.application.response.RspFindAllPrices;
import com.pineapple.springjpa.application.response.RspFindAllPricesByBrandAndFilters;
import com.pineapple.springjpa.application.response.RspFindPriceById;
import com.pineapple.springjpa.application.view.PriceView;
import com.pineapple.springjpa.domain.service.PriceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDateTime;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping("/prices")
@Validated
public class PriceController {
  private final PriceService priceService;

  public PriceController(final PriceService priceService) {
    this.priceService = priceService;
  }

  @ApiOperation(value = "Add new price to database")
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public void addNewPrice(
      @RequestBody @Valid @JsonView(PriceView.Create.Request.class) RqAddPrice rqAddPrice) {
    this.priceService.addNewPrice(rqAddPrice);
  }

  @ApiOperation(value = "Find a single price by primary key")
  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public @JsonView(PriceView.Find.Response.class) RspFindPriceById findPriceById(
      @PathVariable @Min(value = 1, message = ValidationMessage.NUMERIC_GREATER_THAN_ZERO)
          long id) {
    return new RspFindPriceById(this.priceService.findPriceById(id));
  }

  @ApiOperation(value = "Modify a price by primary key")
  @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.OK)
  public void modifyPriceById(
      @PathVariable @Min(value = 1, message = ValidationMessage.NUMERIC_GREATER_THAN_ZERO) long id,
      @RequestBody @Valid @JsonView(PriceView.Modify.Request.class) RqModifyPrice rqModifyPrice) {
    this.priceService.modifyPriceById(id, rqModifyPrice);
  }

  @ApiOperation(value = "Delete price by primary key")
  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.OK)
  public void deletePriceById(
      @PathVariable @Min(value = 1, message = ValidationMessage.NUMERIC_GREATER_THAN_ZERO)
          long id) {
    this.priceService.deletePriceById(id);
  }

  @ApiOperation(value = "Find All Prices")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public @JsonView(PriceView.Find.Response.class) RspFindAllPrices findAllPrices() {
    return new RspFindAllPrices(this.priceService.findAllPrices());
  }

  @ApiOperation(value = "Find Prices by Brand with optional filters like startDate and productId")
  @GetMapping(path = "/brand/{brandId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public RspFindAllPricesByBrandAndFilters findAllPricesByBrandAndFilters(
      @RequestParam(name = "date", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime date,
      @RequestParam(name = "productId", required = false)
          @Min(value = 1, message = ValidationMessage.NUMERIC_GREATER_THAN_ZERO)
          Long productId,
      @PathVariable(name = "brandId")
          @Min(value = 1, message = ValidationMessage.NUMERIC_GREATER_THAN_ZERO)
          Long brandId) {
    return new RspFindAllPricesByBrandAndFilters(
        this.priceService.findAllPricesByBrandAndFilters(
            RqFindByBrandAndFilters.builder()
                .brandId(brandId)
                .date(date)
                .productId(productId)
                .build()));
  }
}
