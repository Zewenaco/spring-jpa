package com.pineapple.springjpa.domain.translator;

import Util.UtilityTest;
import com.pineapple.springjpa.application.request.PriceDto;
import com.pineapple.springjpa.domain.entity.Brand;
import com.pineapple.springjpa.domain.entity.Price;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PriceTranslatorTest {

  @InjectMocks PriceTranslator priceTranslator;
  private Price price;
  private PriceDto priceDto;

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
            .price(new BigDecimal("35.45"))
            .currency("EUR")
            .priceList(1)
            .priority(1)
            .productId(3545L)
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(1))
            .build();
  }

  @Test
  void testShouldModifyEntitySuccessfully() {
    Price priceRsp = this.priceTranslator.modifyPriceEntity(price, priceDto);
    UtilityTest.assertAttrOfObject(priceRsp, Lists.emptyList());
    UtilityTest.assertAttrOfObject(priceRsp.getBrand(), List.of("getPriceList"));
  }

  @Test
  void testShouldModifyEntitySuccessfullyWithPriceFoundEmptyButWithBrand() {
    Price priceFound = Price.builder().brand(Brand.builder().build()).build();
    Price priceRsp = this.priceTranslator.modifyPriceEntity(priceFound, priceDto);
    UtilityTest.assertAttrOfObject(priceRsp, List.of("getId"));
    UtilityTest.assertAttrOfObject(priceRsp.getBrand(), List.of("getName", "getPriceList"));
    Assertions.assertTrue(priceRsp.getProductId() > 0L);
    Assertions.assertTrue(priceRsp.getPriceList() > 0L);
    Assertions.assertTrue(priceRsp.getPriority() > 0L);
  }

  @Test
  void testShouldModifyEntitySuccessfullyWithPriceFoundEmpty() {
    Price priceFound = Price.builder().build();
    Price priceRsp = this.priceTranslator.modifyPriceEntity(priceFound, priceDto);
    UtilityTest.assertAttrOfObject(priceRsp, List.of("getId", "getBrand"));
    Assertions.assertNull(priceRsp.getBrand());
    Assertions.assertTrue(priceRsp.getProductId() > 0L);
    Assertions.assertTrue(priceRsp.getPriceList() > 0L);
    Assertions.assertTrue(priceRsp.getPriority() > 0L);
  }
}
