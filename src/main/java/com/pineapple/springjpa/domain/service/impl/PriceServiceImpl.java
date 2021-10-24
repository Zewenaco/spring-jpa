package com.pineapple.springjpa.domain.service.impl;

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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PriceServiceImpl implements PriceService {

  private final PriceRepository priceRepository;
  private final PriceTranslator priceTranslator;

  public PriceServiceImpl(
      final PriceRepository priceRepository, final PriceTranslator priceTranslator) {
    this.priceRepository = priceRepository;
    this.priceTranslator = priceTranslator;
  }

  @Override
  public List<PriceDto> findAllPrices() {
    List<PriceDto> priceDtoList =
        this.priceRepository.findAll().stream()
            .map(this.priceTranslator::buildPriceDto)
            .collect(Collectors.toList());
    if (priceDtoList.isEmpty()) {
      throw new NotFoundException("There is no records of price");
    }
    return priceDtoList;
  }

  @Override
  public TreeSet<String> findAllPricesByBrandAndFilters(
      RqFindByBrandAndFilters rqFindByBrandAndFilters) {

    List<DescriptionByDateProductIdAndBrandName> descriptionByDateProductIdAndBrandNames =
        Optional.of(rqFindByBrandAndFilters)
            .filter(request -> Objects.nonNull(request.getDate()))
            .filter(request -> Objects.nonNull(request.getProductId()))
            .map(
                request ->
                    this.priceRepository.findByDateInRangeAndBrandIdAndProductIdAndMaxPriority(
                        this.priceTranslator.convertToDateFromLocalDateTime(request.getDate()),
                        request.getBrandId(),
                        request.getProductId(),
                        DescriptionByDateProductIdAndBrandName.class))
            .orElse(null);

    descriptionByDateProductIdAndBrandNames =
        Optional.ofNullable(descriptionByDateProductIdAndBrandNames)
            .orElseGet(
                () ->
                    Optional.of(rqFindByBrandAndFilters)
                        .filter(request -> Objects.nonNull(request.getDate()))
                        .map(
                            request ->
                                this.priceRepository.findByDateInRangeAndBrandIdAndMaxPriority(
                                    this.priceTranslator.convertToDateFromLocalDateTime(
                                        request.getDate()),
                                    request.getBrandId(),
                                    DescriptionByDateProductIdAndBrandName.class))
                        .orElse(null));
    descriptionByDateProductIdAndBrandNames =
        Optional.ofNullable(descriptionByDateProductIdAndBrandNames)
            .orElseGet(
                () ->
                    Optional.of(rqFindByBrandAndFilters)
                        .filter(request -> Objects.nonNull(request.getProductId()))
                        .map(
                            request ->
                                this.priceRepository.findByBrand_IdAndProductId(
                                    request.getBrandId(),
                                    request.getProductId(),
                                    DescriptionByDateProductIdAndBrandName.class))
                        .orElse(null));

    descriptionByDateProductIdAndBrandNames =
        Optional.ofNullable(descriptionByDateProductIdAndBrandNames)
            .orElseGet(
                () ->
                    Optional.of(rqFindByBrandAndFilters)
                        .map(
                            request ->
                                this.priceRepository.findByBrand_Id(
                                    request.getBrandId(),
                                    DescriptionByDateProductIdAndBrandName.class))
                        .orElse(null));
    if (descriptionByDateProductIdAndBrandNames.isEmpty()) {
      throw new NotFoundException("There is no record with that description");
    }
    return descriptionByDateProductIdAndBrandNames.stream()
        .map(DescriptionByDateProductIdAndBrandName::getComplexMessage)
        .collect(Collectors.toCollection(TreeSet::new));
  }

  @Override
  public PriceDto findPriceById(long id) {
    return this.priceRepository
        .findById(id)
        .map(this.priceTranslator::buildPriceDto)
        .orElseThrow(() -> new NotFoundException("There is no price with that id"));
  }

  @Override
  public void modifyPriceById(long id, RqModifyPrice rqModifyPrice) {
    this.priceRepository.save(
        this.priceRepository
            .findById(id)
            .map(
                priceFound ->
                    this.priceTranslator.modifyPriceEntity(priceFound, rqModifyPrice.getPriceDto()))
            .orElseThrow(() -> new NotFoundException("There is no price with that id")));
  }

  @Override
  public void deletePriceById(long id) {
    this.priceRepository.deleteById(id);
  }

  @Override
  public void addNewPrice(RqAddPrice rqAddPrice) {
    this.priceRepository.save(
        this.priceTranslator.modifyPriceEntity(
            Price.builder().brand(Brand.builder().build()).build(), rqAddPrice.getPriceDto()));
  }
}
