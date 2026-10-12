package com.ficklebytes.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ficklebytes.Status;
import com.ficklebytes.config.JacksonConfig;
import com.ficklebytes.exception.UnAuthorizedException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

public class Utils {


  public static boolean GenerateRandomBoolean() {
    return GenerateRandomNumber(0, 1) == 0;
  }

  public static int GenerateRandomNumber(int l, int r) {
    Random rand = new Random();
    return rand.nextInt(r - l) + l;
  }

  public static String GenerateLowercaseString(int count) {
    StringBuilder builder = new StringBuilder();
    String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyz";
    while (count-- != 0) {
      int character =
          (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
      builder.append(ALPHA_NUMERIC_STRING.charAt(character));
    }
    return builder.toString();
  }

  public static boolean IsValidEmailAddress(String email) {
    try {
      InternetAddress emailAddr = new InternetAddress(email);
      emailAddr.validate();
    } catch (AddressException ex) {
      return false;
    }
    return true;
  }

  public static String getRemoteAddress(HttpServletRequest httpServletRequest){
    String ipAddress = httpServletRequest.getHeader("cf-connecting-ip");
    if(ipAddress != null)
      return ipAddress;
    return httpServletRequest.getRemoteAddr();
  }

  public static String GenerateToken(String username) {
    return Utils.MD5(username + "1337");
  }

  public static String MD5(String password) {
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    md.update(password.getBytes());
    byte[] digest = md.digest();
    return DatatypeConverter.printHexBinary(digest).toLowerCase();
  }


  public static String ClassToString(Object object) {
    DateFormat dateFormat = new SimpleDateFormat(JacksonConfig.dateTimeFormat);

    ObjectMapper Obj = new ObjectMapper();
    Obj.findAndRegisterModules();
    dateFormat.setTimeZone(TimeZone.getTimeZone(JacksonConfig.timeZone));
    Obj.setDateFormat(dateFormat);
    Obj.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    Obj.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    Obj.enable(SerializationFeature.INDENT_OUTPUT);
    return Obj.valueToTree(object).toString();
  }

  /*
     Use this function to read from REST API directly using URL
  */
  public static <T extends Object> Status<T> GetFromAPIURL(String uri,
      TypeReference tr) {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters()
        .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    String result = restTemplate.getForObject(uri, String.class);
    try {
      ObjectMapper om = new ObjectMapper();
      om.findAndRegisterModules();
      om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      om.enable(SerializationFeature.INDENT_OUTPUT);
      T t = (T) om.readValue(result, tr);
      return new Status<>(t);
    } catch (Exception e) {
      return new Status<>(HttpStatus.BAD_REQUEST, "Bad Format missing " +
          "fields", e.getLocalizedMessage(), null);
    }
  }

  public static <T extends Object> Status<T> ParseRequest(String json,
      Class<?> cl) {
    ObjectMapper Obj = new ObjectMapper();
    Obj.findAndRegisterModules();
    Obj.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    Obj.enable(SerializationFeature.INDENT_OUTPUT);
    try {
      return new Status<>(HttpStatus.OK, "OK", (T) Obj.readValue(json, cl));
    } catch (Exception e) {
      return new Status<>(HttpStatus.BAD_REQUEST, "Bad Format missing " +
          "fields", e.getLocalizedMessage(), null);
    }
  }


  public static String toString(Object object) {
    ObjectMapper Obj = new ObjectMapper();
    Obj.findAndRegisterModules();
    Obj.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    Obj.enable(SerializationFeature.INDENT_OUTPUT);

    return Obj.valueToTree(object).toString();
  }

}
