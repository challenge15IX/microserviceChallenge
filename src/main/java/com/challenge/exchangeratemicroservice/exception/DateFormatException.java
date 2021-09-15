package com.challenge.exchangeratemicroservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DateFormatException extends RuntimeException {
    public DateFormatException(String from, String to) {
        super("Zły format jednej z dat " + from + ", " + to);
    }
}
