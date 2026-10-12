package com.ficklebytes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
@RequestMapping("/api/general")
public class GeneralController {


  @GetMapping("/current-version")
  public ResponseEntity<String> returnVersion() {
    String buildNumber = "1";
    String message = "There is a new version available, Please visit " +
        "store and update to keep all features working";
    return new ResponseEntity<>(buildNumber + "/" + message, HttpStatus.OK);
  }


  @GetMapping("/minimum-version")
  public ResponseEntity<String> returnMinimumVersion() {
    String buildNumber = "1";
    String message = "This version is no longer supported,please update " +
        "the app";
    return new ResponseEntity<>(buildNumber + "/" + message, HttpStatus.OK);
  }


}
