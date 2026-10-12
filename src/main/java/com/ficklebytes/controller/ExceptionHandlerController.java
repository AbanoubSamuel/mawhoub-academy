package com.ficklebytes.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.ficklebytes.exception.CommentNotFoundException;
import com.ficklebytes.exception.ContentNotFoundException;
import com.ficklebytes.exception.CourseNotFoundException;
import com.ficklebytes.exception.DocumentNotFoundException;
import com.ficklebytes.exception.PurchasedCourseAlreadyException;
import com.ficklebytes.exception.UnAuthenticationException;
import com.ficklebytes.exception.UnAuthorizedException;
import com.ficklebytes.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

  @ExceptionHandler(value = JsonParseException.class)
  public ResponseEntity<Object> exception(JsonParseException exception) {
    return new ResponseEntity<>(exception.getMessage(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = ContentNotFoundException.class)
  public ResponseEntity<Object> exception(ContentNotFoundException exception) {
    return new ResponseEntity<>("Content Not Found!",
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = UserNotFoundException.class)
  public ResponseEntity<Object> exception(UserNotFoundException exception) {
    return new ResponseEntity<>("User Not Found!", HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = UnAuthorizedException.class)
  public ResponseEntity<Object> exception(UnAuthorizedException exception) {
    return new ResponseEntity<>(exception.getMessage(),
        HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(value = UnAuthenticationException.class)
  public ResponseEntity<Object> exception(UnAuthenticationException exception) {
    return new ResponseEntity<>(exception.getMessage(),
        HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(value = PurchasedCourseAlreadyException.class)
  public ResponseEntity<Object> exception(PurchasedCourseAlreadyException exception) {
    return new ResponseEntity<>("Course already purchased",
        HttpStatus.CONFLICT);
  }

  @ExceptionHandler(value = CourseNotFoundException.class)
  public ResponseEntity<Object> exception(CourseNotFoundException exception) {
    return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = CommentNotFoundException.class)
  public ResponseEntity<Object> exception(CommentNotFoundException exception) {
    return new ResponseEntity<>("Comment not found",
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = DocumentNotFoundException.class)
  public ResponseEntity<Object> exception(DocumentNotFoundException exception) {
    return new ResponseEntity<>("Document not found",
        HttpStatus.NOT_FOUND);
  }
}
