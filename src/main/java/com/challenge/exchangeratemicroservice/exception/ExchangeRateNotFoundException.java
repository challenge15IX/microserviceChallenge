package com.challenge.exchangeratemicroservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExchangeRateNotFoundException extends RuntimeException {
    public ExchangeRateNotFoundException(String from, String to) {
        super("No exchange rates found for range from " + from  + " to " + to);
    }
}
