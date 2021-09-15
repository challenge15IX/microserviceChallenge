package com.challenge.exchangeratemicroservice;

import java.util.List;
import java.util.Map;

public class ExtServiceResponse {
    private boolean success;
    private long timestamp;
    private boolean historical;
    private String base;
    private String date;
    private Map<String,Double> rates;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isHistorical() {
        return historical;
    }

    public void setHistorical(boolean historical) {
        this.historical = historical;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String,Double> getRates() {
        return rates;
    }

    public void setRates(Map<String,Double> rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "ExtServiceResponse{" +
                "success=" + success +
                ", timestamp=" + timestamp +
                ", historical=" + historical +
                ", base='" + base + '\'' +
                ", date='" + date + '\'' +
                ", rates=" + rates +
                '}';
    }
}
