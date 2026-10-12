package com.ficklebytes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

public class Status<T> {

  public static final String OK = "OK";

  private HttpStatus status;
  private int errorCode;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime timestamp;
  private String message;
  private String debugMessage;
  private T value;

  public Status() {

  }

  public Status(T value) {
    this.status = HttpStatus.OK;
    this.errorCode = status.value();
    this.timestamp = LocalDateTime.now();
    this.message = "OK";
    this.debugMessage = "OK";
    this.value = value;
  }


  public Status(HttpStatus status, String message, T value) {
    this.status = status;
    this.errorCode = status.value();
    this.timestamp = LocalDateTime.now();
    this.message = message;
    this.debugMessage = message;
    this.value = value;
  }

  public Status(HttpStatus status, String message, String debugMessage,
      T value) {
    this.status = status;
    this.errorCode = status.value();
    this.timestamp = LocalDateTime.now();
    this.message = message;
    this.debugMessage = debugMessage;
    this.value = value;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public void setStatus(HttpStatus status) {
    this.status = status;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public String getDebugMessage() {
    return debugMessage;
  }

  public void setDebugMessage(String debugMessage) {
    this.debugMessage = debugMessage;
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @JsonIgnore
  public boolean hasError() {
    return !isOk();
  }

  @JsonIgnore
  public boolean isOk() {
    return status == HttpStatus.OK;
  }

}
