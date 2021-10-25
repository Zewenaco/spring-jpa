package integrationTest;

import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import Util.UtilityTest;
import com.pineapple.springjpa.SpringJpaApplication;
import com.pineapple.springjpa.application.request.PriceDto;
import com.pineapple.springjpa.application.request.RqAddPrice;
import com.pineapple.springjpa.application.request.RqModifyPrice;
import com.pineapple.springjpa.application.response.RspFindAllPrices;
import com.pineapple.springjpa.application.response.RspFindAllPricesByBrandAndFilters;
import com.pineapple.springjpa.application.response.RspFindPriceById;
import java.io.IOException;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = SpringJpaApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(print = MockMvcPrint.LOG_DEBUG, printOnlyOnFailure = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PriceControllerIntegrationTest {

  private static final String RSP_FIND_ALL_PRICES_URI = "responses/findAllPrices.json";
  private static final String REQ_ADD_NEW_PRICE_URI = "requests/addNewPrice.json";
  private static final String REQ_MODIFY_PRICE_BY_ID_URI = "requests/modifyPriceById.json";
  private static final String RSP_FIND_PRICE_BY_ID_URI = "responses/findById.json";
  private static final String RSP_FIND_ALL_PRICES_BY_BRAND_ID_PRODUCT_ID_DATE_URI =
      "responses/findAllPricesByBrandIdAndFiltersProductIdAndDate.json";
  private static final String RSP_FIND_ALL_PRICES_BY_BRAND_ID_PRODUCT_ID_URI =
      "responses/findAllPricesByBrandIdAndFiltersProductId.json";
  private static final String RSP_FIND_ALL_PRICES_BY_BRAND_ID_URI =
      "responses/findAllPricesByBrandIdAndFilters.json";
  private static RspFindAllPrices RSP_FIND_ALL_PRICES;
  private static RqAddPrice RQ_ADD_PRICE;
  private static RqModifyPrice RQ_MODIFY_PRICE;
  private static RspFindPriceById RSP_FIND_PRICE_BY_ID;
  private static RspFindAllPricesByBrandAndFilters RSP_FIND_ALL_PRICES_BY_BRAND_AND_FILTERS;
  private static final String SERVICE_BASE_PATH = "/prices";

  @Autowired private MockMvc mockMvc;

  @BeforeAll
  static void setup() throws IOException {
    RSP_FIND_ALL_PRICES =
        UtilityTest.getObjectFromFile(RSP_FIND_ALL_PRICES_URI, RspFindAllPrices.class);
    RQ_ADD_PRICE = UtilityTest.getObjectFromFile(REQ_ADD_NEW_PRICE_URI, RqAddPrice.class);
    RQ_MODIFY_PRICE =
        UtilityTest.getObjectFromFile(REQ_MODIFY_PRICE_BY_ID_URI, RqModifyPrice.class);
    RSP_FIND_PRICE_BY_ID =
        UtilityTest.getObjectFromFile(RSP_FIND_PRICE_BY_ID_URI, RspFindPriceById.class);
  }

  @Test
  @Order(1)
  void testShouldFindAllPricesSuccessfully() throws Exception {
    MvcResult mvcResult =
        mockMvc
            .perform(get(SERVICE_BASE_PATH).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    RspFindAllPrices rspFindAllPricesFromTest =
        UtilityTest.getObjectFromText(
            mvcResult.getResponse().getContentAsString(), RspFindAllPrices.class);
    rspFindAllPricesFromTest.setTimeStamp(RSP_FIND_ALL_PRICES.getTimeStamp());
    Assertions.assertEquals(
        UtilityTest.asPrettyStr(RSP_FIND_ALL_PRICES),
        UtilityTest.asPrettyStr(rspFindAllPricesFromTest));
  }

  @Test
  void testShouldThrowBadRequestWhenAddPriceWithDtoNull() throws Exception {
    mockMvc
        .perform(
            post(SERVICE_BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(UtilityTest.asPrettyStr(RqAddPrice.builder().build())))
        .andExpect(status().isBadRequest())
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be null\\\",\\\"field\\\":\\\"priceDto\\\"}")))
        .andExpect(content().string(containsStringIgnoringCase("\"status\":\"FAIL\"")))
        .andExpect(
            content().string(containsStringIgnoringCase("\"code\":\"ERR.REQUEST.INVALID\"")));
  }

  @Test
  void testShouldThrowBadRequestWhenAddPriceWithDtoEmpty() throws Exception {
    mockMvc
        .perform(
            post(SERVICE_BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    UtilityTest.asPrettyStr(
                        RqAddPrice.builder().priceDto(PriceDto.builder().build()).build())))
        .andExpect(status().isBadRequest())
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be null\\\",\\\"field\\\":\\\"priceDto.productId\\\"}")))
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be null\\\",\\\"field\\\":\\\"priceDto.startDate\\\"}")))
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be null\\\",\\\"field\\\":\\\"priceDto.price\\\"}")))
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be empty\\\",\\\"field\\\":\\\"priceDto.currency\\\"}")))
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be null\\\",\\\"field\\\":\\\"priceDto.brandId\\\"}")))
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be null\\\",\\\"field\\\":\\\"priceDto.priority\\\"}")))
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be null\\\",\\\"field\\\":\\\"priceDto.priceList\\\"}")))
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be null\\\",\\\"field\\\":\\\"priceDto.endDate\\\"}")))
        .andExpect(content().string(containsStringIgnoringCase("\"status\":\"FAIL\"")))
        .andExpect(content().string(containsStringIgnoringCase("\"code\":\"ERR.REQUEST.INVALID\"")))
        .andReturn();
  }

  @Test
  @Order(2)
  void testShouldFindPriceByIdSuccessfully() throws Exception {
    MvcResult mvcResult =
        mockMvc
            .perform(
                get(SERVICE_BASE_PATH + "/" + NumberUtils.LONG_ONE)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    RspFindPriceById rspFindPriceById =
        UtilityTest.getObjectFromText(
            mvcResult.getResponse().getContentAsString(), RspFindPriceById.class);
    rspFindPriceById.setTimeStamp(RSP_FIND_PRICE_BY_ID.getTimeStamp());
    Assertions.assertEquals(
        UtilityTest.asPrettyStr(RSP_FIND_PRICE_BY_ID), UtilityTest.asPrettyStr(rspFindPriceById));
  }

  @Test
  void testShouldThrowBadRequestWhenFindPriceBydIdWithZeroId() throws Exception {

    mockMvc
        .perform(
            delete(SERVICE_BASE_PATH + "/" + NumberUtils.LONG_ZERO)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "\"message\":\"The number should be greater than 0\"")))
        .andExpect(content().string(containsStringIgnoringCase("\"status\":\"FAIL\"")))
        .andExpect(content().string(containsStringIgnoringCase("\"code\":\"ERR.REQUEST.INVALID\"")))
        .andReturn();
  }

  @Test
  void testShouldThrowBadRequestWhenDeleteBydIdWithZeroId() throws Exception {
    mockMvc
        .perform(
            delete(SERVICE_BASE_PATH + "/" + NumberUtils.LONG_ZERO)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "\"message\":\"The number should be greater than 0\"")))
        .andExpect(content().string(containsStringIgnoringCase("\"status\":\"FAIL\"")))
        .andExpect(content().string(containsStringIgnoringCase("\"code\":\"ERR.REQUEST.INVALID\"")))
        .andReturn();
  }

  @Test
  void testShouldThrowBadRequestWhenModifyBydIdWithZeroId() throws Exception {
    mockMvc
        .perform(
            put(SERVICE_BASE_PATH + "/" + NumberUtils.LONG_ZERO)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(UtilityTest.asPrettyStr(RQ_MODIFY_PRICE)))
        .andExpect(status().isBadRequest())
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "\"message\":\"The number should be greater than 0\"")))
        .andExpect(content().string(containsStringIgnoringCase("\"status\":\"FAIL\"")))
        .andExpect(content().string(containsStringIgnoringCase("\"code\":\"ERR.REQUEST.INVALID\"")))
        .andReturn();
  }

  @Test
  void testShouldThrowBadRequestWhenModifyPriceByIdWithDtoNull() throws Exception {
    mockMvc
        .perform(
            put(SERVICE_BASE_PATH + "/" + NumberUtils.LONG_ONE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(UtilityTest.asPrettyStr(RqModifyPrice.builder().build())))
        .andExpect(status().isBadRequest())
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be null\\\",\\\"field\\\":\\\"priceDto\\\"}")))
        .andExpect(content().string(containsStringIgnoringCase("\"status\":\"FAIL\"")))
        .andExpect(
            content().string(containsStringIgnoringCase("\"code\":\"ERR.REQUEST.INVALID\"")));
  }

  @Test
  void testShouldThrowBadRequestWhenModifyPriceWithDtoEmpty() throws Exception {
    mockMvc
        .perform(
            put(SERVICE_BASE_PATH + "/" + NumberUtils.LONG_ONE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    UtilityTest.asPrettyStr(
                        RqModifyPrice.builder().priceDto(PriceDto.builder().build()).build())))
        .andExpect(status().isBadRequest())
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be null\\\",\\\"field\\\":\\\"priceDto.productId\\\"}")))
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be null\\\",\\\"field\\\":\\\"priceDto.startDate\\\"}")))
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be null\\\",\\\"field\\\":\\\"priceDto.price\\\"}")))
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be empty\\\",\\\"field\\\":\\\"priceDto.currency\\\"}")))
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be null\\\",\\\"field\\\":\\\"priceDto.brandId\\\"}")))
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be null\\\",\\\"field\\\":\\\"priceDto.priority\\\"}")))
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be null\\\",\\\"field\\\":\\\"priceDto.priceList\\\"}")))
        .andExpect(
            content()
                .string(
                    containsStringIgnoringCase(
                        "{\\\"message\\\":\\\"Can't be null\\\",\\\"field\\\":\\\"priceDto.endDate\\\"}")))
        .andExpect(content().string(containsStringIgnoringCase("\"status\":\"FAIL\"")))
        .andExpect(content().string(containsStringIgnoringCase("\"code\":\"ERR.REQUEST.INVALID\"")))
        .andReturn();
  }

  @Test
  @Order(3)
  void testShouldFindAllPricesByBrandAndFiltersProductIdAndDateSuccessfully() throws Exception {
    RSP_FIND_ALL_PRICES_BY_BRAND_AND_FILTERS =
        UtilityTest.getObjectFromFile(
            RSP_FIND_ALL_PRICES_BY_BRAND_ID_PRODUCT_ID_DATE_URI,
            RspFindAllPricesByBrandAndFilters.class);
    MvcResult mvcResult =
        mockMvc
            .perform(
                get(String.format(
                        "%s%s/%d?productId=%d&date=%s",
                        SERVICE_BASE_PATH,
                        "/brand",
                        NumberUtils.LONG_ONE,
                        35455L,
                        "2020-06-15T16:00:00"))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    RspFindAllPricesByBrandAndFilters rspFindAllPricesByBrandAndFilters =
        UtilityTest.getObjectFromText(
            mvcResult.getResponse().getContentAsString(), RspFindAllPricesByBrandAndFilters.class);
    rspFindAllPricesByBrandAndFilters.setTimeStamp(
        RSP_FIND_ALL_PRICES_BY_BRAND_AND_FILTERS.getTimeStamp());
    Assertions.assertEquals(
        UtilityTest.asPrettyStr(RSP_FIND_ALL_PRICES_BY_BRAND_AND_FILTERS),
        UtilityTest.asPrettyStr(rspFindAllPricesByBrandAndFilters));
  }

  @Test
  @Order(4)
  void testShouldFindAllPricesByBrandAndFiltersDateSuccessfully() throws Exception {
    RSP_FIND_ALL_PRICES_BY_BRAND_AND_FILTERS =
        UtilityTest.getObjectFromFile(
            RSP_FIND_ALL_PRICES_BY_BRAND_ID_PRODUCT_ID_DATE_URI,
            RspFindAllPricesByBrandAndFilters.class);
    MvcResult mvcResult =
        mockMvc
            .perform(
                get(String.format(
                        "%s%s/%d?date=%s",
                        SERVICE_BASE_PATH, "/brand", NumberUtils.LONG_ONE, "2020-06-15T16:00:00"))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    RspFindAllPricesByBrandAndFilters rspFindAllPricesByBrandAndFilters =
        UtilityTest.getObjectFromText(
            mvcResult.getResponse().getContentAsString(), RspFindAllPricesByBrandAndFilters.class);
    rspFindAllPricesByBrandAndFilters.setTimeStamp(
        RSP_FIND_ALL_PRICES_BY_BRAND_AND_FILTERS.getTimeStamp());
    Assertions.assertEquals(
        UtilityTest.asPrettyStr(RSP_FIND_ALL_PRICES_BY_BRAND_AND_FILTERS),
        UtilityTest.asPrettyStr(rspFindAllPricesByBrandAndFilters));
  }

  @Test
  @Order(5)
  void testShouldFindAllPricesByBrandAndFiltersProductIdSuccessfully() throws Exception {
    RSP_FIND_ALL_PRICES_BY_BRAND_AND_FILTERS =
        UtilityTest.getObjectFromFile(
            RSP_FIND_ALL_PRICES_BY_BRAND_ID_PRODUCT_ID_URI,
            RspFindAllPricesByBrandAndFilters.class);
    MvcResult mvcResult =
        mockMvc
            .perform(
                get(String.format(
                        "%s%s/%d?productId=%d",
                        SERVICE_BASE_PATH, "/brand", NumberUtils.LONG_ONE, 35455L))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    RspFindAllPricesByBrandAndFilters rspFindAllPricesByBrandAndFilters =
        UtilityTest.getObjectFromText(
            mvcResult.getResponse().getContentAsString(), RspFindAllPricesByBrandAndFilters.class);
    rspFindAllPricesByBrandAndFilters.setTimeStamp(
        RSP_FIND_ALL_PRICES_BY_BRAND_AND_FILTERS.getTimeStamp());
    Assertions.assertEquals(
        UtilityTest.asPrettyStr(RSP_FIND_ALL_PRICES_BY_BRAND_AND_FILTERS),
        UtilityTest.asPrettyStr(rspFindAllPricesByBrandAndFilters));
  }

  @Test
  @Order(6)
  void testShouldFindAllPricesByBrandSuccessfully() throws Exception {
    RSP_FIND_ALL_PRICES_BY_BRAND_AND_FILTERS =
        UtilityTest.getObjectFromFile(
            RSP_FIND_ALL_PRICES_BY_BRAND_ID_URI, RspFindAllPricesByBrandAndFilters.class);
    MvcResult mvcResult =
        mockMvc
            .perform(
                get(String.format("%s%s/%d", SERVICE_BASE_PATH, "/brand", NumberUtils.LONG_ONE))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    RspFindAllPricesByBrandAndFilters rspFindAllPricesByBrandAndFilters =
        UtilityTest.getObjectFromText(
            mvcResult.getResponse().getContentAsString(), RspFindAllPricesByBrandAndFilters.class);
    rspFindAllPricesByBrandAndFilters.setTimeStamp(
        RSP_FIND_ALL_PRICES_BY_BRAND_AND_FILTERS.getTimeStamp());
    Assertions.assertEquals(
        UtilityTest.asPrettyStr(RSP_FIND_ALL_PRICES_BY_BRAND_AND_FILTERS),
        UtilityTest.asPrettyStr(rspFindAllPricesByBrandAndFilters));
  }

  @Test
  @Order(7)
  void testShouldAddNewPriceSuccessfully() throws Exception {
    mockMvc
        .perform(
            post(SERVICE_BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(UtilityTest.asPrettyStr(RQ_ADD_PRICE)))
        .andExpect(status().isCreated());
  }

  @Test
  @Order(8)
  void testShouldModifyPriceByIdSuccessfully() throws Exception {
    mockMvc
        .perform(
            put(SERVICE_BASE_PATH + "/" + NumberUtils.LONG_ONE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(UtilityTest.asPrettyStr(RQ_MODIFY_PRICE)))
        .andExpect(status().isOk());
  }

  @Test
  @Order(9)
  void testShouldDeletePriceById() throws Exception {
    mockMvc
        .perform(
            get(SERVICE_BASE_PATH + "/" + NumberUtils.LONG_ONE).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
}
