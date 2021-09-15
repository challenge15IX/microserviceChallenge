package com.challenge.exchangeratemicroservice;

import java.time.LocalDate;

public class ExchangeRateOutput {
    private LocalDate date;
    private String currency;
    private double rate;

    public ExchangeRateOutput() {
    }

    public ExchangeRateOutput(String currency, LocalDate date, double rate) {
        this.date = date;
        this.currency = currency;
        this.rate = rate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
