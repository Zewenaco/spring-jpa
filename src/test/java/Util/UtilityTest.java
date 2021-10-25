package Util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.io.ClassPathResource;

@UtilityClass
public class UtilityTest {

  static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final DateFormat DATE_FORMAT = new StdDateFormat().withColonInTimeZone(true);

  static {
    OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    OBJECT_MAPPER.setDateFormat(DATE_FORMAT);
    OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  public static void assertAttrOfObject(Object object, List<String> methodNamesToAvoid) {
    Assertions.assertNotNull(
        object, String.format("Object %s shouldn't be null", object.getClass().getSimpleName()));
    Method[] methods = object.getClass().getDeclaredMethods();
    Arrays.stream(methods)
        .filter(method -> method.getName().contains("get"))
        .filter(
            method ->
                methodNamesToAvoid.stream().noneMatch(name -> method.getName().contains(name)))
        .forEach(
            method -> {
              try {
                Assertions.assertNotNull(
                    method.invoke(object, null),
                    String.format(
                        "Object %s %s shouldn't be null",
                        object.getClass().getSimpleName(), method.getName()));
              } catch (IllegalAccessException | InvocationTargetException e) {
                Assertions.fail(e.getMessage());
              }
            });
  }

  public static <T> T getObjectFromFile(String path, Class<T> clazz) throws IOException {
    return OBJECT_MAPPER.readValue(new ClassPathResource(path).getFile(), clazz);
  }

  public static String asPrettyStr(Object obj) throws JsonProcessingException {
    return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
  }

  public static <T> T getObjectFromText(String text, Class<T> clazz) throws IOException {
    return OBJECT_MAPPER.readValue(text, clazz);
  }
}
