package com.pineapple.springjpa.domain.service.impl;

import Util.UtilityTest;
import com.pineapple.springjpa.application.exceptions.NotFoundException;
import com.pineapple.springjpa.application.request.PriceDto;
import com.pineapple.springjpa.application.request.RqAddPrice;
import com.pineapple.springjpa.application.request.RqFindByBrandAndFilters;
import com.pineapple.springjpa.application.request.RqModifyPrice;
import com.pineapple.springjpa.domain.entity.Brand;
import com.pineapple.springjpa.domain.entity.Price;
import com.pineapple.springjpa.domain.entity.view.DescriptionByDateProductIdAndBrandName;
import com.pineapple.springjpa.domain.service.PriceService;
import com.pineapple.springjpa.domain.translator.PriceTranslator;
import com.pineapple.springjpa.infrastructure.repository.PriceRepository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

@ExtendWith(MockitoExtension.class)
class PriceServiceImplTest {

  @Mock private PriceTranslator priceTranslator;

  @Mock private PriceRepository priceRepository;

  private PriceService priceService;
  private Price price;
  private PriceDto priceDto;
  private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

  @BeforeEach
  void setup() {
    price =
        Price.builder()
            .id(1L)
            .brand(Brand.builder().id(1L).name("Zara").build())
            .price(new BigDecimal("35.45"))
            .currency("EUR")
            .priceList(1)
            .priority(1)
            .productId(3545L)
            .startDate(Timestamp.from(Instant.now()))
            .endDate(Timestamp.from(Instant.now().plus(Period.ofDays(1))))
            .build();
    priceDto =
        PriceDto.builder()
            .id(1L)
            .brandId(1L)
            .brandName("Zara")
            .price(new BigDecimal("35.45"))
            .currency("EUR")
            .priceList(1)
            .priority(1)
            .productId(3545L)
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(1))
            .build();
    this.priceService = new PriceServiceImpl(priceRepository, priceTranslator);
  }

  @Test
  void testShouldFindAllPricesSuccessfully() {
    Mockito.when(priceRepository.findAll()).thenReturn(List.of(price));
    Mockito.when(this.priceTranslator.buildPriceDto(ArgumentMatchers.any(Price.class)))
        .thenCallRealMethod();
    Mockito.when(
            this.priceTranslator.convertToLocalDateTimeFromDate(
                ArgumentMatchers.any(Timestamp.class)))
        .thenCallRealMethod();
    Collection<PriceDto> priceDtoList = this.priceService.findAllPrices();
    Mockito.verify(this.priceRepository, Mockito.atLeastOnce()).findAll();
    Mockito.verify(this.priceTranslator, Mockito.atLeastOnce())
        .buildPriceDto(ArgumentMatchers.any(Price.class));
    Mockito.verify(this.priceTranslator, Mockito.atLeastOnce())
        .convertToLocalDateTimeFromDate(ArgumentMatchers.any(Timestamp.class));
    Assertions.assertNotNull(priceDtoList);
    priceDtoList.forEach(
        priceDto -> UtilityTest.assertAttrOfObject(priceDto, List.of("getBrandName")));
  }

  @Test
  void testShouldThrowNotFoundExceptionWhenNonePriceEntityFounded() {
    Mockito.when(priceRepository.findAll()).thenReturn(Lists.emptyList());
    Assertions.assertThrows(
        NotFoundException.class,
        () -> this.priceService.findAllPrices(),
        "There is no records of price");
    Mockito.verify(this.priceRepository, Mockito.atLeastOnce()).findAll();
    Mockito.verify(this.priceTranslator, Mockito.never())
        .buildPriceDto(ArgumentMatchers.any(Price.class));
    Mockito.verify(this.priceTranslator, Mockito.never())
        .convertToLocalDateTimeFromDate(ArgumentMatchers.any(Timestamp.class));
  }

  @Test
  void testShouldFindPriceByIdSuccessfully() {
    Mockito.when(this.priceTranslator.buildPriceDto(ArgumentMatchers.any(Price.class)))
        .thenCallRealMethod();
    Mockito.when(
            this.priceTranslator.convertToLocalDateTimeFromDate(
                ArgumentMatchers.any(Timestamp.class)))
        .thenCallRealMethod();
    Mockito.when(priceRepository.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(price));
    PriceDto priceDto = this.priceService.findPriceById(1L);
    Mockito.verify(this.priceRepository, Mockito.atLeastOnce())
        .findById(ArgumentMatchers.anyLong());
    Mockito.verify(this.priceTranslator, Mockito.atLeastOnce())
        .buildPriceDto(ArgumentMatchers.any(Price.class));
    Mockito.verify(this.priceTranslator, Mockito.atLeastOnce())
        .convertToLocalDateTimeFromDate(ArgumentMatchers.any(Timestamp.class));
    UtilityTest.assertAttrOfObject(priceDto, List.of("getBrandName"));
  }

  @Test
  void testShouldThrowNotFoundExceptionWhenNonePriceEntityLookedByIdFounded() {
    Mockito.when(priceRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
    Assertions.assertThrows(
        NotFoundException.class,
        () -> this.priceService.findPriceById(1L),
        "There is no price with that id");
    Mockito.verify(this.priceRepository, Mockito.atLeastOnce())
        .findById(ArgumentMatchers.anyLong());
    Mockito.verify(this.priceTranslator, Mockito.never())
        .buildPriceDto(ArgumentMatchers.any(Price.class));
    Mockito.verify(this.priceTranslator, Mockito.never())
        .convertToLocalDateTimeFromDate(ArgumentMatchers.any(Timestamp.class));
  }

  @Test
  void testShouldModifyPriceByIdSuccessfully() {
    Mockito.when(
            this.priceTranslator.modifyPriceEntity(
                ArgumentMatchers.any(Price.class), ArgumentMatchers.any(PriceDto.class)))
        .thenCallRealMethod();
    Mockito.when(
            this.priceTranslator.convertToDateFromLocalDateTime(
                ArgumentMatchers.any(LocalDateTime.class)))
        .thenCallRealMethod();
    Mockito.when(priceRepository.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(price));
    Mockito.when(priceRepository.save(ArgumentMatchers.any(Price.class))).thenReturn(price);
    this.priceService.modifyPriceById(1L, RqModifyPrice.builder().priceDto(priceDto).build());
    Mockito.verify(this.priceRepository, Mockito.atLeastOnce())
        .findById(ArgumentMatchers.anyLong());
    Mockito.verify(this.priceRepository, Mockito.atLeastOnce())
        .save(ArgumentMatchers.any(Price.class));
    Mockito.verify(this.priceTranslator, Mockito.atLeastOnce())
        .modifyPriceEntity(ArgumentMatchers.any(Price.class), ArgumentMatchers.any(PriceDto.class));
    Mockito.verify(this.priceTranslator, Mockito.atLeastOnce())
        .convertToDateFromLocalDateTime(ArgumentMatchers.any(LocalDateTime.class));
  }

  @Test
  void testShouldThrowNotFoundExceptionWhenNonePriceEntityLookedByIdFoundedToBeModified() {
    Mockito.when(priceRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
    RqModifyPrice rqModifyPrice = RqModifyPrice.builder().priceDto(priceDto).build();
    Assertions.assertThrows(
        NotFoundException.class,
        () -> this.priceService.modifyPriceById(1L, rqModifyPrice),
        "There is no price with that id");
    Mockito.verify(this.priceRepository, Mockito.atLeastOnce())
        .findById(ArgumentMatchers.anyLong());
    Mockito.verify(this.priceRepository, Mockito.never()).save(ArgumentMatchers.any(Price.class));
    Mockito.verify(this.priceTranslator, Mockito.never())
        .modifyPriceEntity(ArgumentMatchers.any(Price.class), ArgumentMatchers.any(PriceDto.class));
    Mockito.verify(this.priceTranslator, Mockito.never())
        .convertToDateFromLocalDateTime(ArgumentMatchers.any(LocalDateTime.class));
  }

  @Test
  void testShouldDeletePriceByIdSuccessfully() {
    Mockito.doNothing().when(this.priceRepository).deleteById(ArgumentMatchers.anyLong());
    try {
      this.priceService.deletePriceById(1L);
    } catch (Throwable throwable) {
      Assertions.fail(throwable);
    }
  }

  @Test
  void testShouldThrowEmptyResultDataAccessExceptionWhenNoneEntityFoundedToBeDeleted() {
    Mockito.doThrow(EmptyResultDataAccessException.class)
        .when(this.priceRepository)
        .deleteById(ArgumentMatchers.anyLong());
    Assertions.assertThrows(
        EmptyResultDataAccessException.class, () -> this.priceService.deletePriceById(1L));
  }

  @Test
  void testShouldAddNewPriceSuccessfully() {
    Mockito.when(this.priceRepository.save(ArgumentMatchers.any(Price.class))).thenReturn(price);
    Mockito.when(
            this.priceTranslator.modifyPriceEntity(
                ArgumentMatchers.any(Price.class), ArgumentMatchers.any(PriceDto.class)))
        .thenCallRealMethod();
    Mockito.when(
            this.priceTranslator.convertToDateFromLocalDateTime(
                ArgumentMatchers.any(LocalDateTime.class)))
        .thenCallRealMethod();
    this.priceService.addNewPrice(RqAddPrice.builder().priceDto(priceDto).build());
    Mockito.verify(this.priceRepository, Mockito.atLeastOnce())
        .save(ArgumentMatchers.any(Price.class));
    Mockito.verify(this.priceTranslator, Mockito.atLeastOnce())
        .modifyPriceEntity(ArgumentMatchers.any(Price.class), ArgumentMatchers.any(PriceDto.class));
    Mockito.verify(this.priceTranslator, Mockito.atLeastOnce())
        .convertToDateFromLocalDateTime(ArgumentMatchers.any(LocalDateTime.class));
  }

  @Test
  void testShouldFindAllPricesByBrandAndFiltersWithAllArgs() {
    Mockito.when(
            this.priceTranslator.buildPriceDto(
                ArgumentMatchers.any(DescriptionByDateProductIdAndBrandName.class)))
        .thenCallRealMethod();
    Mockito.when(
            this.priceTranslator.convertToDateFromLocalDateTime(
                ArgumentMatchers.any(LocalDateTime.class)))
        .thenCallRealMethod();
    Mockito.when(
            this.priceTranslator.convertToLocalDateTimeFromDate(
                ArgumentMatchers.any(Timestamp.class)))
        .thenCallRealMethod();
    Mockito.when(
            priceRepository.findByDateInRangeAndBrandIdAndProductIdAndMaxPriority(
                ArgumentMatchers.any(Timestamp.class),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(Class.class)))
        .thenReturn(List.of(this.buildDescriptionByDateProductIdAndBrandName()));

    List<PriceDto> priceDtoList =
        this.priceService.findAllPricesByBrandAndFilters(
            RqFindByBrandAndFilters.builder()
                .productId(1L)
                .brandId(1L)
                .date(LocalDateTime.now())
                .build());
    this.validatePriceDtoList(priceDtoList, List.of("getPriority", "getId", "getCurrency"));
  }

  @Test
  void testShouldFindAllPricesByBrandAndFiltersWithDateAndBrandAsParameter() {
    Mockito.when(
            this.priceTranslator.buildPriceDto(
                ArgumentMatchers.any(DescriptionByDateProductIdAndBrandName.class)))
        .thenCallRealMethod();
    Mockito.when(
            this.priceTranslator.convertToDateFromLocalDateTime(
                ArgumentMatchers.any(LocalDateTime.class)))
        .thenCallRealMethod();
    Mockito.when(
            this.priceTranslator.convertToLocalDateTimeFromDate(
                ArgumentMatchers.any(Timestamp.class)))
        .thenCallRealMethod();
    Mockito.when(
            priceRepository.findByDateInRangeAndBrandIdAndMaxPriority(
                ArgumentMatchers.any(Timestamp.class),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(Class.class)))
        .thenReturn(List.of(this.buildDescriptionByDateProductIdAndBrandName()));

    List<PriceDto> priceDtoList =
        this.priceService.findAllPricesByBrandAndFilters(
            RqFindByBrandAndFilters.builder().brandId(1L).date(LocalDateTime.now()).build());
    this.validatePriceDtoList(priceDtoList, List.of("getPriority", "getId", "getCurrency"));
  }

  @Test
  void testShouldFindAllPricesByBrandAndFiltersWithProductIdAndBrandAsParameter() {
    Mockito.when(
            this.priceTranslator.buildPriceDto(
                ArgumentMatchers.any(DescriptionByDateProductIdAndBrandName.class)))
        .thenCallRealMethod();
    Mockito.when(
            this.priceTranslator.convertToLocalDateTimeFromDate(
                ArgumentMatchers.any(Timestamp.class)))
        .thenCallRealMethod();
    Mockito.when(
            priceRepository.findByBrand_IdAndProductId(
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(Class.class)))
        .thenReturn(List.of(this.buildDescriptionByDateProductIdAndBrandName()));

    List<PriceDto> priceDtoList =
        this.priceService.findAllPricesByBrandAndFilters(
            RqFindByBrandAndFilters.builder().brandId(1L).productId(3545L).build());
    this.validatePriceDtoList(priceDtoList, List.of("getPriority", "getId", "getCurrency"));
  }

  @Test
  void testShouldFindAllPricesByBrand() {
    Mockito.when(
            this.priceTranslator.buildPriceDto(
                ArgumentMatchers.any(DescriptionByDateProductIdAndBrandName.class)))
        .thenCallRealMethod();
    Mockito.when(
            this.priceTranslator.convertToLocalDateTimeFromDate(
                ArgumentMatchers.any(Timestamp.class)))
        .thenCallRealMethod();
    Mockito.when(
            priceRepository.findByBrand_Id(
                ArgumentMatchers.anyLong(), ArgumentMatchers.any(Class.class)))
        .thenReturn(List.of(this.buildDescriptionByDateProductIdAndBrandName()));

    List<PriceDto> priceDtoList =
        this.priceService.findAllPricesByBrandAndFilters(
            RqFindByBrandAndFilters.builder().brandId(1L).build());
    this.validatePriceDtoList(priceDtoList, List.of("getPriority", "getId", "getCurrency"));
  }

  @Test
  void testShouldThrowNotFoundExceptionWhenNonePriceEntityFoundedLookedByBrandId() {
    Mockito.when(
            priceRepository.findByBrand_Id(
                ArgumentMatchers.anyLong(), ArgumentMatchers.any(Class.class)))
        .thenReturn(Lists.emptyList());
    RqFindByBrandAndFilters rqFindByBrandAndFilters =
        RqFindByBrandAndFilters.builder().brandId(1L).build();
    Assertions.assertThrows(
        NotFoundException.class,
        () -> this.priceService.findAllPricesByBrandAndFilters(rqFindByBrandAndFilters),
        "There is no record with that description");
  }

  protected void validatePriceDtoList(
      List<PriceDto> priceDtoList, List<String> methodNamesToAvoid) {
    Assertions.assertNotNull(priceDtoList);
    Assertions.assertFalse(priceDtoList.isEmpty());
    priceDtoList.forEach(
        priceDtoRsp -> UtilityTest.assertAttrOfObject(priceDtoRsp, methodNamesToAvoid));
  }

  protected DescriptionByDateProductIdAndBrandName buildDescriptionByDateProductIdAndBrandName() {
    Timestamp timestamp = Timestamp.from(Instant.now());
    var descriptionByDateProductIdAndBrandName =
        factory.createProjection(DescriptionByDateProductIdAndBrandName.class);
    descriptionByDateProductIdAndBrandName.setBrandId(1L);
    descriptionByDateProductIdAndBrandName.setProductId(3545L);
    descriptionByDateProductIdAndBrandName.setBrandName("Zara");
    descriptionByDateProductIdAndBrandName.setStartDate(timestamp);
    descriptionByDateProductIdAndBrandName.setEndDate(timestamp);
    descriptionByDateProductIdAndBrandName.setPriceList(5L);
    descriptionByDateProductIdAndBrandName.setPrice(new BigDecimal("35.45"));
    return descriptionByDateProductIdAndBrandName;
  }
}
