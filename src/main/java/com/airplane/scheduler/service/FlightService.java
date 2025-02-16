package com.airplane.scheduler.service;

import com.airplane.scheduler.model.Flight;
import com.airplane.scheduler.repository.FlightRepository;
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

    @Autowired
    private FlightRepository flightRepository;

    public List<Flight> getFlights(String origin, String destination, String date) {
        try {
            String accessToken = getAccessToken();

            String url = "https://test.api.amadeus.com/v2/shopping/flight-offers" +
                         "?originLocationCode=" + origin +
                         "&destinationLocationCode=" + destination +
                         "&departureDate=" + date +
                         "&adults=1";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Flight[]> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Flight[].class);

            if (response.getBody() != null) {
                List<Flight> flights = Arrays.asList(response.getBody());
                
                // Save to database
                flightRepository.saveAll(flights);

                return flights;
            } else {
                throw new RuntimeException("No flight data found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching flights: " + e.getMessage(), e);
        }
    }

    private String getAccessToken() {
        try {
            String tokenUrl = "https://test.api.amadeus.com/v1/security/oauth2/token";
            String requestBody = "grant_type=client_credentials&client_id=" + apiKey + "&client_secret=" + apiSecret;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    tokenUrl, request, Map.class);

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
