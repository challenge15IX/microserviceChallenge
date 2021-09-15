package com.challenge.exchangeratemicroservice;

import com.challenge.exchangeratemicroservice.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Integer> {
    public List<ExchangeRate> findByCurrencyAndDate(String currency, LocalDate date);
    public List<ExchangeRate> findByDateBetween(LocalDate from, LocalDate to);
}
