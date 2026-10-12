package com.ficklebytes.security;

import com.ficklebytes.entity.AllowedOrigin;
import com.ficklebytes.repository.AllowedOriginRepository;
import com.ficklebytes.service.UserDetailsServiceImpl;
import com.ficklebytes.utils.JWTUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {

  private final UserDetailsServiceImpl userDetailsService;

  private final CustomPasswordEncoder customPasswordEncoder;

  private final AllowedOriginRepository originRepository;


  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(customPasswordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    System.out.println("Configuring WebSecurity...");
    http.cors().and().csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // allow preflight
            .anyRequest().permitAll()
            .and()
        .addFilterBefore(new JWTAuthorizationFilter(authenticationManager(), userDetailsService), 
                        org.springframework.security.web.authentication.www.BasicAuthenticationFilter.class)
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    System.out.println("WebSecurity configuration completed.");
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    List<AllowedOrigin> originsFromDb = originRepository.findAll();
    List<String> allowedOrigins = new ArrayList<>();

    for (AllowedOrigin originEntity : originsFromDb) {
      allowedOrigins.add(originEntity.getOrigin());
    }

    configuration.setAllowedOrigins(allowedOrigins);
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
