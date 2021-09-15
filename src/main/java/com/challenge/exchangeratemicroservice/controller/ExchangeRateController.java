package com.challenge.exchangeratemicroservice.controller;

import com.challenge.exchangeratemicroservice.*;
import com.challenge.exchangeratemicroservice.entity.ExchangeRate;
import com.challenge.exchangeratemicroservice.exception.DateFormatException;
import com.challenge.exchangeratemicroservice.exception.ExchangeRateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ExchangeRateController {

    Logger logger = LoggerFactory.getLogger(ExchangeRateController.class);

    @Value("${access_key:a9d31edf4d2ba6d6652af500635f6638}")
    private String accessKey;
    @Value("${symbols:GBP,HKD,USD}")
    private String symbols;
    @Value("${ext_service_url:http://api.exchangeratesapi.io/v1/}")
_    private String externalServiceUrl;

    @Autowired
    private ExchangeRateRepository repository;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/load")
    public void loadFromExternalService() {
        LocalDate firstOfCurrentMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = firstOfCurrentMonth.minusYears(1);

        repository.deleteAll();

        RestTemplate restTemplate = new RestTemplate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (LocalDate date = firstOfCurrentMonth; date.isAfter(endDate); date = date.minusMonths(1)) {

            String dateStr = date.format(formatter);

            String url = externalServiceUrl + "{date}?access_key={access_key}&symbols={symbols}";

            logger.info(url);

            Map<String, String> vars = new HashMap<>();
            vars.put("date", dateStr);
            vars.put("access_key", accessKey);
            vars.put("symbols", symbols);

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);


            ExtServiceResponse response = restTemplate.getForObject(url,
                    ExtServiceResponse.class,
                    vars);


            logger.info("response = " + response);

            for (String currency : response.getRates().keySet()) {

                LocalDate exchangeDate = LocalDate.parse(response.getDate(), formatter);
                ExchangeRate exchangeRate = new ExchangeRate(exchangeDate, currency, response.getRates().get(currency));

                logger.info("exchangeRate = " + exchangeRate);
                repository.save(exchangeRate);
            }


        }

        List<ExchangeRate> list = repository.findAll();
    }


    @GetMapping("/range/{from}/{to}")
    public List<ExchangeRateOutput> getForRange(@PathVariable String from, @PathVariable String to)
            throws ExchangeRateNotFoundException, DateFormatException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");


        LocalDate dateFrom;
        LocalDate dateTo;

        try {
            dateFrom = LocalDate.parse(from, formatter);
            dateTo = LocalDate.parse(to, formatter);
        } catch (DateTimeParseException e) {
            throw new DateFormatException(from, to);
        }

        logger.info("dateFrom = " + dateFrom);
        logger.info("dateTo = " + dateTo);

        List<ExchangeRate> list = repository.findByDateBetween(dateFrom, dateTo);
        if (list.isEmpty())
            throw new ExchangeRateNotFoundException(from, to);
        return list.stream()
                .map(e -> new ExchangeRateOutput(e.getCurrency(), e.getDate(), e.getRate()))
                .collect(Collectors.toList());


    }

}
