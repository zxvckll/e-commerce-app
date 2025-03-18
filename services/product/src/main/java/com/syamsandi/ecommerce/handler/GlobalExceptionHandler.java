package com.syamsandi.ecommerce.handler;

import com.syamsandi.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductPurchaseException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(ProductPurchaseException ex) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ex.getMessage());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(MethodArgumentNotValidException ex) {

        Map<String,String> errors= new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String field = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(field, errorMessage);
        });

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorResponse(errors));
    }


}
