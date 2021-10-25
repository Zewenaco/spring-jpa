package com.pineapple.springjpa.domain.service;

import com.pineapple.springjpa.application.request.PriceDto;
import com.pineapple.springjpa.application.request.RqAddPrice;
import com.pineapple.springjpa.application.request.RqFindByBrandAndFilters;
import com.pineapple.springjpa.application.request.RqModifyPrice;
import java.util.Collection;
import java.util.List;

public interface PriceService {

  Collection<PriceDto> findAllPrices();

  List<PriceDto> findAllPricesByBrandAndFilters(RqFindByBrandAndFilters rqFindByBrandAndFilters);

  PriceDto findPriceById(long id);

  void modifyPriceById(long id, RqModifyPrice rqModifyPrice);

  void deletePriceById(long id);

  void addNewPrice(RqAddPrice rqAddPrice);
}
