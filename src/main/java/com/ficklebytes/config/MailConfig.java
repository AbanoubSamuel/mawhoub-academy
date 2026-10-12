package com.ficklebytes.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

  @Value("{spring.profiles.active}")
  private String profileActive;

  @Value("${spring.mail.username}")
  private String emailAddress;

  @Value("${spring.mail.password}")
  private String password;

  @Bean
  public JavaMailSender javaMailService() {
    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

    javaMailSender.setHost(profileActive.contains("dz") ? "smtp.sendgrid.net"
        : "smtp.sendgrid.net");
    javaMailSender.setPort(587);
    javaMailSender.setUsername(emailAddress);
    javaMailSender.setPassword(password);
    javaMailSender.setJavaMailProperties(getMailProperties());

    return javaMailSender;
  }

  private Properties getMailProperties() {
    Properties properties = new Properties();
    properties.setProperty("mail.transport.protocol", "smtp");
    properties.setProperty("mail.smtp.auth", "true");
    properties.setProperty("mail.debug", "true");
    properties.setProperty("mail.smtp.connectiontimeout", "5000");
    properties.setProperty("mail.smtp.timeout", "5000");
    properties.setProperty("mail.smtp.writetimeout", "5000");
    properties.setProperty("mail.smtp.starttls.enable", "true");
    return properties;
  }
}
