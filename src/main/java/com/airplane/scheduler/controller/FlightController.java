package com.airplane.scheduler.controller;

import com.airplane.scheduler.model.Flight;
import com.airplane.scheduler.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/api/flights")
    public List<Flight> getFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String date) {
        System.out.println("Endpoint hit: /api/flights");
        System.out.println("Parameters: origin=" + origin + ", destination=" + destination + ", date=" + date);
        return flightService.getFlights(origin, destination, date);
    }
}