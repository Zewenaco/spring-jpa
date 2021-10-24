package com.pineapple.springjpa.infrastructure.repository;

import com.pineapple.springjpa.domain.entity.Price;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PriceRepository extends JpaRepository<Price, Long> {

  @Query(
      value =
          "SELECT priceA.ID as id, "
              + "priceA.PRODUCT_ID as productId, "
              + "priceA.BRAND_ID as brandId, "
              + "brand.NAME as brandName, "
              + "priceA.START_DATE as startDate, "
              + "priceA.END_DATE as endDate, "
              + "priceA.PRICE_LIST as priceList, "
              + "priceA.PRIORITY as priority, "
              + "priceA.PRICE as price, "
              + "priceA.CURR as currency "
              + "FROM Price priceA "
              + "INNER JOIN Brand brand "
              + "ON brand.ID = ?2 "
              + "WHERE priceA.END_DATE >= ?1  "
              + "AND priceA.START_DATE <= ?1 "
              + "AND priceA.PRODUCT_ID = ?3 "
              + "AND priceA.PRIORITY = (SELECT MAX (priceB.PRIORITY) FROM Price priceB)",
      nativeQuery = true)
  <T> List<T> findByDateInRangeAndBrandIdAndProductIdAndMaxPriority(
      Timestamp startDate, long brandId, long productId, Class<T> type);

  @Query(
      value =
          "SELECT priceA.ID as id, "
              + "priceA.PRODUCT_ID as productId, "
              + "priceA.BRAND_ID as brandId, "
              + "brand.NAME as brandName, "
              + "priceA.START_DATE as startDate, "
              + "priceA.END_DATE as endDate, "
              + "priceA.PRICE_LIST as priceList, "
              + "priceA.PRIORITY as priority, "
              + "priceA.PRICE as price, "
              + "priceA.CURR as currency "
              + "FROM Price priceA "
              + "INNER JOIN Brand brand "
              + "ON brand.ID = ?2 "
              + "WHERE priceA.END_DATE >= ?1 "
              + "AND priceA.START_DATE <= ?1 "
              + "AND priceA.PRIORITY = (SELECT MAX (priceB.PRIORITY) FROM Price priceB)",
      nativeQuery = true)
  <T> List<T> findByDateInRangeAndBrandIdAndMaxPriority(
      Timestamp startDate, long brandId, Class<T> type);

  <T> List<T> findByBrand_IdAndProductId(long brandId, long productId, Class<T> type);

  <T> List<T> findByBrand_Id(long brandId, Class<T> type);
}
