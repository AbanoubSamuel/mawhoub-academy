package com.ficklebytes;

import com.ficklebytes.security.CustomPasswordEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@ComponentScan(basePackages = {"com.ficklebytes"})
@EntityScan("com.ficklebytes.entity")
@EnableJpaRepositories("com.ficklebytes.repository")
@EnableCaching
public class AcademyApplication {

  public static void main(String[] args) {
    SpringApplication.run(AcademyApplication.class, args);
  }

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
        .setFieldMatchingEnabled(true)
        .setAmbiguityIgnored(true)
        .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
        .setMatchingStrategy(MatchingStrategies.LOOSE);
    return modelMapper;
  }

  @Bean
  public CustomPasswordEncoder passwordEncoder() {
    return new CustomPasswordEncoder();
  }
}
