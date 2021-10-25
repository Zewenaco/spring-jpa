package integrationTest;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pineapple.springjpa.SpringJpaApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = SpringJpaApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(print = MockMvcPrint.LOG_DEBUG, printOnlyOnFailure = false)
class BasicIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  public void testShouldBeSuccessfullyConsumingSwaggerEndpoint() throws Exception {
    mockMvc
        .perform(get("/swagger-ui/index.html").accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(containsStringIgnoringCase("Swagger")));
  }

  @Test
  public void testApiDocsWithoutHeadersShouldBeOk() throws Exception {
    ResultActions resultActions =
        mockMvc
            .perform(get("/api-docs").accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.swagger").exists())
            .andExpect(jsonPath("$.swagger").value("2.0"))
            .andExpect(jsonPath("$.info").exists())
            .andExpect(jsonPath("$.info").isMap())
            .andExpect(jsonPath("$.info").isNotEmpty())
            .andExpect(jsonPath("$.info.description").exists())
            .andExpect(jsonPath("$.info.description").isString())
            .andExpect(jsonPath("$.info.description").isNotEmpty())
            .andExpect(jsonPath("$.info.title").exists())
            .andExpect(jsonPath("$.info.title").isString())
            .andExpect(jsonPath("$.info.title").isNotEmpty())
            .andExpect(jsonPath("$.info.contact").exists())
            .andExpect(jsonPath("$.info.contact").isMap())
            .andExpect(jsonPath("$.info.contact").isNotEmpty())
            .andExpect(jsonPath("$.info.contact.name").exists())
            .andExpect(jsonPath("$.info.contact.name").isString())
            .andExpect(jsonPath("$.info.contact.name").isNotEmpty())
            .andExpect(jsonPath("$.info.contact.url").exists())
            .andExpect(jsonPath("$.info.contact.url").isString())
            .andExpect(jsonPath("$.info.contact.url").isNotEmpty())
            .andExpect(jsonPath("$.info.contact.email").exists())
            .andExpect(jsonPath("$.info.contact.email").isString())
            .andExpect(jsonPath("$.info.contact.email").isNotEmpty())
            .andExpect(jsonPath("$.info.contact.email").exists())
            .andExpect(jsonPath("$.info.contact.email").isString())
            .andExpect(jsonPath("$.info.contact.email").isNotEmpty())
            .andExpect(jsonPath("$.paths").exists())
            .andExpect(jsonPath("$.paths").isMap())
            .andExpect(jsonPath("$.paths").isNotEmpty());
  }

  @Test
  public void testSwaggerShouldBeOkAndWithoutModelErrors() throws Exception {
    mockMvc
        .perform(get("/api-docs").accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(not(containsStringIgnoringCase("Error-ModelName"))));
  }

  @Test
  public void testHealthWithoutHeadersShouldBeOk() throws Exception {
    mockMvc
        .perform(get("/private/health").accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("status").value("UP"));
  }

  @Test
  public void testInfoWithoutHeadersShouldBeOk() throws Exception {
    mockMvc.perform(get("/private/info").accept(APPLICATION_JSON)).andExpect(status().isOk());
  }
}
