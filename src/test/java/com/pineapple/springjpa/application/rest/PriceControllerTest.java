package com.pineapple.springjpa.application.rest;

import Util.UtilityTest;
import com.pineapple.springjpa.application.request.PriceDto;
import com.pineapple.springjpa.application.request.RqAddPrice;
import com.pineapple.springjpa.application.request.RqFindByBrandAndFilters;
import com.pineapple.springjpa.application.request.RqModifyPrice;
import com.pineapple.springjpa.application.response.RspFindAllPrices;
import com.pineapple.springjpa.application.response.RspFindAllPricesByBrandAndFilters;
import com.pineapple.springjpa.application.response.RspFindPriceById;
import com.pineapple.springjpa.domain.service.PriceService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PriceControllerTest {

  @InjectMocks PriceController priceController;
  @Mock PriceService priceService;
  private PriceDto priceDto;

  @BeforeEach
  void setup() {
    priceDto =
        PriceDto.builder()
            .price(new BigDecimal("35.50"))
            .currency("EUR")
            .endDate(LocalDateTime.now().plusDays(1))
            .startDate(LocalDateTime.now())
            .brandId(1L)
            .brandName("Zara")
            .productId(35455L)
            .id(1L)
            .priority(1)
            .priceList(1)
            .build();
    priceController = new PriceController(priceService);
  }

  @Test
  void testShouldFindAllPricesSuccessfully() {
    Mockito.when(priceService.findAllPrices()).thenReturn(List.of(priceDto));
    RspFindAllPrices rspFindAllPrices = this.priceController.findAllPrices();
    Mockito.verify(this.priceService, Mockito.atLeastOnce()).findAllPrices();
    UtilityTest.assertAttrOfObject(rspFindAllPrices, Lists.emptyList());
    Assertions.assertFalse(rspFindAllPrices.getPriceDtoCollection().isEmpty());
    rspFindAllPrices
        .getPriceDtoCollection()
        .forEach(
            priceDtoInCollection ->
                UtilityTest.assertAttrOfObject(priceDtoInCollection, Lists.emptyList()));
  }

  @Test
  void testShouldAddNewPriceSuccessfully() {
    try {
      this.priceController.addNewPrice(RqAddPrice.builder().priceDto(priceDto).build());
    } catch (Throwable throwable) {
      Assertions.fail(throwable);
    }
    Mockito.verify(this.priceService, Mockito.atLeastOnce())
        .addNewPrice(ArgumentMatchers.any(RqAddPrice.class));
  }

  @Test
  void testShouldFindPriceByIdSuccessfully() {
    Mockito.when(this.priceService.findPriceById(ArgumentMatchers.anyLong())).thenReturn(priceDto);
    RspFindPriceById rspFindPriceById = this.priceController.findPriceById(1L);
    Mockito.verify(this.priceService, Mockito.atLeastOnce())
        .findPriceById(ArgumentMatchers.anyLong());
    UtilityTest.assertAttrOfObject(rspFindPriceById, List.of("error", "code", "message"));
    UtilityTest.assertAttrOfObject(rspFindPriceById.getPriceDto(), Lists.emptyList());
  }

  @Test
  void testShouldModifyPriceByIdSuccessfully() {
    try {
      this.priceController.modifyPriceById(1L, RqModifyPrice.builder().priceDto(priceDto).build());
    } catch (Throwable throwable) {
      Assertions.fail(throwable);
    }
    Mockito.verify(this.priceService, Mockito.atLeastOnce())
        .modifyPriceById(ArgumentMatchers.anyLong(), ArgumentMatchers.any(RqModifyPrice.class));
  }

  @Test
  void testShouldDeleteByIdSuccessfully() {
    try {
      this.priceController.deletePriceById(1L);
    } catch (Throwable throwable) {
      Assertions.fail(throwable);
    }
    Mockito.verify(this.priceService, Mockito.atLeastOnce())
        .deletePriceById(ArgumentMatchers.anyLong());
  }

  @Test
  void testShouldFindAllPricesByBrandAndFiltersWithAllArgsSuccessfully() {
    Mockito.when(
            this.priceService.findAllPricesByBrandAndFilters(
                ArgumentMatchers.any(RqFindByBrandAndFilters.class)))
        .thenReturn(List.of(priceDto));
    RspFindAllPricesByBrandAndFilters rspFindAllPricesByBrandAndFilters =
        this.priceController.findAllPricesByBrandAndFilters(LocalDateTime.now(), 35455L, 1L);
    Mockito.verify(this.priceService, Mockito.atLeastOnce())
        .findAllPricesByBrandAndFilters(ArgumentMatchers.any(RqFindByBrandAndFilters.class));
    UtilityTest.assertAttrOfObject(rspFindAllPricesByBrandAndFilters, Lists.emptyList());
    rspFindAllPricesByBrandAndFilters
        .getPriceDtoList()
        .forEach(priceDtoRsp -> UtilityTest.assertAttrOfObject(priceDtoRsp, Lists.emptyList()));
  }
}
