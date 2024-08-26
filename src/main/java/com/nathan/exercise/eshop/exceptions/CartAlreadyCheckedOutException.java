package com.nathan.exercise.eshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CartAlreadyCheckedOutException extends RuntimeException {
    public CartAlreadyCheckedOutException(String message) {
        super(message);
    }
}
