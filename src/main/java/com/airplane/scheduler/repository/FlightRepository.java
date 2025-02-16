package com.airplane.scheduler.repository;

import com.airplane.scheduler.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    // Custom queries (if needed)
}
