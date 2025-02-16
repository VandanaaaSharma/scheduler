package com.airplane.scheduler.service;

import com.airplane.scheduler.model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class FlightService {

    @Value("${dlryr4XizJ9a6Iryq8G6VEYrMays8jhA}") // Fetch API key from application.properties
    private String apiKey;

    @Value("${yfsAgk5pFdizszGA}") // Fetch API secret from application.properties
    private String apiSecret;

    @Autowired
    private RestTemplate restTemplate;

    public List<Flight> getFlights(String origin, String destination, String date) {
        try {
            // Step 1: Get Access Token
            String accessToken = getAccessToken();

            // Step 2: Call Amadeus API with the access token
            String url = "https://test.api.amadeus.com/v2/shopping/flight-offers" +
                         "?originLocationCode=" + origin +
                         "&destinationLocationCode=" + destination +
                         "&departureDate=" + date +
                         "&adults=1";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken); // Add access token to headers

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Flight[]> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Flight[].class);

            if (response.getBody() != null) {
                return Arrays.asList(response.getBody());
            } else {
                throw new RuntimeException("No flight data found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching flights: " + e.getMessage(), e);
        }
    }

    private String getAccessToken() {
        try {
            // Step 1: Prepare the token request URL and body
            String tokenUrl = "https://test.api.amadeus.com/v1/security/oauth2/token";
            String requestBody = "grant_type=client_credentials&client_id=" + apiKey + "&client_secret=" + apiSecret;

            // Step 2: Set headers for the token request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Step 3: Send the token request
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    tokenUrl, request, Map.class);

            // Step 4: Extract and return the access token
            if (response.getBody() != null && response.getBody().containsKey("access_token")) {
                return response.getBody().get("access_token").toString();
            } else {
                throw new RuntimeException("Failed to retrieve access token from Amadeus API");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching access token: " + e.getMessage(), e);
        }
    }
}