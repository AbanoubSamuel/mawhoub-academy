package com.ficklebytes.service;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.ficklebytes.entity.AcademyUser;

@Service
public class MailService {

  private static final Logger logger = LoggerFactory.getLogger(MailService.class);

  private final String sendGridApiKey;
  private final String sendGridResetApiKey;

  public MailService(@Value("${app.sendgrid.api-key}") String sendGridApiKey,
                     @Value("${app.sendgrid.reset-api-key}") String sendGridResetApiKey) {
    this.sendGridApiKey = sendGridApiKey;
    this.sendGridResetApiKey = sendGridResetApiKey;
  }

  public String sendTextEmail(String email) throws IOException {
    Email from = new Email("ahmedcampora2719@gmail.com");
    String subject = "The subject";
    Email to = new Email(email);
    Content content = new Content("text/plain", "This is a test email");
    Mail mail = new Mail(from, subject, to, content);

    SendGrid sg = new SendGrid(sendGridApiKey);
    Request request = new Request();
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
      logger.info(response.getBody());
      return response.getBody();
    } catch (IOException ex) {
      throw ex;
    }
  }

  public String sendPasswordReset(AcademyUser user, String token) throws IOException {

//		 	mawhoubacademy@gmail.com
    Email from = new Email("mawhoubacademy@gmail.com");
    String subject = "Reset Your Password";
    Email to = new Email(user.getEmail());
    Content content = new Content("text/plain", "Use next token, To reset your password\n" + token);

    Mail mail = new Mail(from, subject, to, content);

    SendGrid sg = new SendGrid(sendGridResetApiKey);
    Request request = new Request();
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
      logger.info(response.getBody());
      return response.getBody();
    } catch (IOException ex) {
      throw ex;
    }

  }

}
