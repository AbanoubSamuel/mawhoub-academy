package com.ficklebytes.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

  public static final String dateTimeFormat = "yyyy-MM-dd'T'hh:mm:ss'Z'";
  public static final String timeZone = "UTC";

  @Bean
  @Primary
  public ObjectMapper serializingObjectMapper() {
    DateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
    ObjectMapper Obj = new ObjectMapper();
    Obj.findAndRegisterModules();
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    Obj.setDateFormat(dateFormat);
    Obj.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    Obj.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    Obj.enable(SerializationFeature.INDENT_OUTPUT);
    return Obj;
  }
}
