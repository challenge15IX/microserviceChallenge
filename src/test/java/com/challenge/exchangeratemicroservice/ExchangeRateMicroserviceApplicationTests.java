package com.challenge.exchangeratemicroservice;

import com.challenge.exchangeratemicroservice.entity.ExchangeRate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ExchangeRateMicroserviceApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ExchangeRateRepository repository;

    private MvcResult loadExchangeRates() throws Exception {
        String uri = "/load";
        return mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
    }

    @Test
    void whenDataIsLoadedOnlyTwelveMonthsForGivenCurrenciesAreReturned() throws Exception {
        MvcResult mvcResult = loadExchangeRates();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.NO_CONTENT.value(), status);

        long numOfMonths = repository.findAll().stream()
                .map(exchangeRate -> exchangeRate.getDate().withDayOfMonth(1))
                .distinct()
                .count();
        assertEquals(12, numOfMonths);

        List<String> currenciesList = repository.findAll().stream()
                .map(exchangeRate -> exchangeRate.getCurrency())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        assertEquals(List.of("GBP", "HKD", "USD"), currenciesList);
    }

    @Test
    void GivenSpecificDateAndCurrencyWhenRateForGivenDateIsCalledThenRateToEuroIsReturned() throws Exception {
        String from = "2021-09-01";
        String to = from;
        String uri = "/range/" + from + "/" + to;


        loadExchangeRates();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();



        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        String result = mvcResult.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ExchangeRateOutput[] exchangeRateOutputs = mapper.readValue(result, ExchangeRateOutput[].class);

        Optional<ExchangeRateOutput> optional =
                Arrays.stream(exchangeRateOutputs)
                        .filter(exchangeRateOutput -> exchangeRateOutput.getCurrency().equals("GBP"))
                        .findFirst();

        assertTrue(optional.isPresent());

        assertEquals(0.859735, optional.get().getRate(), 0.001);


    }

}
