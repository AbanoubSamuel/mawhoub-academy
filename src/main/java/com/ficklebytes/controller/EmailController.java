package com.ficklebytes.controller;

import com.ficklebytes.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api/email")
public class EmailController {

  @Autowired
  private MailService mailService;

  @PostMapping("/send")
  public ResponseEntity sendEmail(@RequestParam String email) throws IOException {
    mailService.sendTextEmail(email);
    return ResponseEntity.ok().build();
  }


}