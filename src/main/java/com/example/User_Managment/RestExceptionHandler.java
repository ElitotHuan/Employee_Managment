package com.example.User_Managment;

import com.example.User_Managment.response_handler.Error;
import com.example.User_Managment.response_handler.ResponeObject;
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

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("" + ex.getLocalizedMessage());
        return buildResponseEntity(new ResponeObject("Error found", new Error("Missing Request Body", 400)));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("" + ex.getLocalizedMessage());
        ResponeObject responeObject = new ResponeObject("Error found", new Error("Validation failed", 400));
        return buildResponseEntity(responeObject);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.error("" + ex.getLocalizedMessage());
        ResponeObject responeObject = new ResponeObject("Error found", new Error("Can't find employee", 400));
        return buildResponseEntity(responeObject);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("" + ex.getLocalizedMessage());
        ResponeObject responeObject = new ResponeObject("Error found", new Error("Access denied", 401));
        return buildResponseEntity(responeObject);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) {
        logger.error("" + ex.getLocalizedMessage());
        ResponeObject responeObject = new ResponeObject("Error found", new Error("Access denied", 403));
        return buildResponseEntity(responeObject);
    }

    private ResponseEntity<Object> buildResponseEntity(ResponeObject ResponeObject) {
        return new ResponseEntity<>(ResponeObject, HttpStatus.valueOf(ResponeObject.getError().getStatus()));
    }

}
