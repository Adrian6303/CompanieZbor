package com.example.companiezbor.repository;

import com.example.companiezbor.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    Optional<Flight> findByDepartureAndArrival(String departure, String arrival);


}
