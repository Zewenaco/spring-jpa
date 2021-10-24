package com.pineapple.springjpa.domain.service;

import com.pineapple.springjpa.application.request.PriceDto;
import com.pineapple.springjpa.application.request.RqAddPrice;
import com.pineapple.springjpa.application.request.RqFindByBrandAndFilters;
import com.pineapple.springjpa.application.request.RqModifyPrice;
import java.util.Collection;
import java.util.TreeSet;

public interface PriceService {

  Collection<PriceDto> findAllPrices();

  TreeSet<String> findAllPricesByBrandAndFilters(RqFindByBrandAndFilters rqFindByBrandAndFilters);

  PriceDto findPriceById(long id);

  void modifyPriceById(long id, RqModifyPrice rqModifyPrice);

  void deletePriceById(long id);

  void addNewPrice(RqAddPrice rqAddPrice);
}
