package com.example.User_Managment.exceptions_handler;

import com.example.User_Managment.exceptions_handler.customs_exception.AccountExpiredException;
import com.example.User_Managment.exceptions_handler.customs_exception.IncorrectLoginException;
import com.example.User_Managment.exceptions_handler.customs_exception.UserExistedException;
import com.example.User_Managment.response_handler.ErrorRespone;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(ExceptionsHandler.class);

    //Rest Exceptions
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getMessage());
        Throwable throwable = ex.getCause();
        if (throwable instanceof InvalidFormatException) {
            logger.error(throwable.getMessage());
            return buildResponseEntity(new ErrorRespone("Invalid format", HttpStatus.BAD_REQUEST.value()));
        }
        return buildResponseEntity(new ErrorRespone("Missing Request Body", status.value()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> listErrors = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            String errorMessage = fieldError.getField() + " " + fieldError.getDefaultMessage();
            listErrors.add(errorMessage);
        }
        logger.warn(listErrors.toString());
        return buildResponseEntity(new ErrorRespone("There is something wrong with input values", status.value()));
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getMessage());
        return buildResponseEntity(new ErrorRespone("Missing header", status.value()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        logger.warn(ex.getMessage());
        return buildResponseEntity(new ErrorRespone("Can't find user", HttpStatus.NO_CONTENT.value()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        logger.error(ex.getMessage());
        return buildResponseEntity(new ErrorRespone(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.error(ex.getMessage());
        return buildResponseEntity(new ErrorRespone("", HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(UserExistedException.class)
    protected ResponseEntity<Object> handleUserExistedException(UserExistedException ex, WebRequest request) {
        logger.error("Add error: Username already existed");
        return buildResponseEntity(new ErrorRespone("Username already existed", HttpStatus.CONFLICT.value()));
    }

    //Login exceptions

    @ExceptionHandler(IncorrectLoginException.class)
    protected ResponseEntity<Object> handleIncorrectLoginException(IncorrectLoginException ex, WebRequest request) {
        logger.error("Username or password is incorrect");
        return buildResponseEntity(new ErrorRespone("Username or password is incorrect", HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(AccountExpiredException.class)
    protected ResponseEntity<Object> handleAccountExpiredException(AccountExpiredException ex, WebRequest request) {
        logger.error("Expired Account");
        return buildResponseEntity(new ErrorRespone("Your account has expired please contact your system administrator", HttpStatus.CONFLICT.value()));
    }


    //JWT exceptions
    @ExceptionHandler(MalformedJwtException.class)
    protected ResponseEntity<Object> handleMalformedJwtException(MalformedJwtException ex) {
        logger.error(ex.getMessage());
        return buildResponseEntity(new ErrorRespone("Access denied!", HttpStatus.UNAUTHORIZED.value()));
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    protected ResponseEntity<Object> handleUnsupportedJwtException(UnsupportedJwtException ex) {
        logger.error(ex.getMessage());
        return buildResponseEntity(new ErrorRespone("Access denied!", HttpStatus.UNAUTHORIZED.value()));
    }

    @ExceptionHandler(SignatureException.class)
    protected ResponseEntity<Object> handleSignatureException(SignatureException ex) {
        logger.error(ex.getMessage());
        return buildResponseEntity(new ErrorRespone("Access denied!", HttpStatus.UNAUTHORIZED.value()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) {
        logger.error("JWT expired at " + ex.getClaims().getExpiration());
        return buildResponseEntity(new ErrorRespone("Unauthorized Access!", HttpStatus.FORBIDDEN.value()));
    }


    private ResponseEntity<Object> buildResponseEntity(ErrorRespone errorRespone) {
        return new ResponseEntity<>(errorRespone, HttpStatus.valueOf(errorRespone.getStatusCode()));
    }

}
