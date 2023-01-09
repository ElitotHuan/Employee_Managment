package com.example.User_Managment;

import com.example.User_Managment.response_handler.ErrorRespone;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getMessage());
        return buildResponseEntity(new ErrorRespone("Missing Request Body", status.value()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getMessage());
        return buildResponseEntity(new ErrorRespone("Validation failed", status.value()));
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getMessage());
        return buildResponseEntity(new ErrorRespone(ex.getMessage(), status.value()));
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        logger.warn(ex.getMessage());
        return buildResponseEntity(new ErrorRespone("Can't find user", HttpStatus.BAD_REQUEST.value()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        logger.error(ex.getMessage());
        return buildResponseEntity(new ErrorRespone(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.error("JWT String argument cannot be null or empty");
        return buildResponseEntity(new ErrorRespone("Unauthorized Access!", HttpStatus.UNAUTHORIZED.value()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(SignatureException.class)
    protected ResponseEntity<Object> handleSignatureException(SignatureException ex, WebRequest request) {
        logger.error("Signature verification failed");
        return buildResponseEntity(new ErrorRespone("Signature verification failed", HttpStatus.FORBIDDEN.value()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MalformedJwtException.class)
    protected ResponseEntity<Object> handleMalformedJwtException(MalformedJwtException ex, WebRequest request) {
        logger.error("Invalid JWT token");
        return buildResponseEntity(new ErrorRespone("Unauthorized Access! ", HttpStatus.UNAUTHORIZED.value()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({UnsupportedJwtException.class})
    protected ResponseEntity<Object> handleUnsupportedJwtException(UnsupportedJwtException ex, WebRequest request) {
        logger.error("JWT token is unsupported");
        return buildResponseEntity(new ErrorRespone("Unauthorized Access!", HttpStatus.UNAUTHORIZED.value()));
    }


    private ResponseEntity<Object> buildResponseEntity(ErrorRespone errorRespone) {
        return new ResponseEntity<>(errorRespone, HttpStatus.valueOf(errorRespone.getStatus()));
    }

}
