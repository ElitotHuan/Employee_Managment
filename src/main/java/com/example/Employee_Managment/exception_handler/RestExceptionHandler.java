package com.example.Employee_Managment.exception_handler;

import com.example.Employee_Managment.response_handler.error_response.Error;
import com.example.Employee_Managment.response_handler.error_response.ResponeObject;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("" + ex.getCause());
        return buildResponseEntity(new ResponeObject("Error found", new Error("JSON parse error", ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST.value())));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("" + ex.getLocalizedMessage());
        ResponeObject responeObject = new ResponeObject("Error found", new Error("Validation failed", ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST.value()));
        return buildResponseEntity(responeObject);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.error("" + ex.getLocalizedMessage());
        ResponeObject responeObject = new ResponeObject("Error found", new Error("Can't find employee", ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST.value()));
        return buildResponseEntity(responeObject);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) {
        logger.error("" + ex.getLocalizedMessage());
        ResponeObject responeObject = new ResponeObject("Error found", new Error("Access denied", "Token expired please login again", HttpStatus.CONFLICT.value()));
        return buildResponseEntity(responeObject);
    }


    private ResponseEntity<Object> buildResponseEntity(ResponeObject ResponeObject) {
        return new ResponseEntity<>(ResponeObject, HttpStatus.valueOf(ResponeObject.getError().getStatus()));
    }

}
