package com.airplane.scheduler.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.airplane.scheduler.model.Flight;
import com.airplane.scheduler.service.FlightService;

@CrossOrigin(origins = "http://127.0.0.1:5500") // Allow requests from the frontend
@RestController
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/api/flights")
    public List<Flight> getFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String date) {
        return flightService.getFlights(origin, destination, date);
    }
}