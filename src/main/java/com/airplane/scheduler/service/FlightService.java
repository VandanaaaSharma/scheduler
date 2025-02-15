package com.airplane.scheduler.service;

import com.airplane.scheduler.model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class FlightService {

    @Value("${dlryr4XizJ9a6Iryq8G6VEYrMays8jhA}")
    private String apiKey;

    @Value("${yfsAgk5pFdizszGA}")
    private String apiSecret;

    @Autowired
    private RestTemplate restTemplate;

    public List<Flight> getFlights(String origin, String destination, String date) {
        String url = "https://test.api.amadeus.com/v2/shopping/flight-offers" +
                     "?originLocationCode=" + origin +
                     "&destinationLocationCode=" + destination +
                     "&departureDate=" + date +
                     "&adults=1";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Flight[]> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Flight[].class);

        return Arrays.asList(response.getBody());
    }

    private String getAccessToken() {
        String tokenUrl = "https://test.api.amadeus.com/v1/security/oauth2/token";
        String requestBody = "grant_type=client_credentials&client_id=" + apiKey + "&client_secret=" + apiSecret;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        return response.getBody().get("access_token").toString();
    }
}