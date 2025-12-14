package com.example.companiezbor.service;

import com.example.companiezbor.model.Flight;
import com.example.companiezbor.repository.FlightRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

        private final FlightRepository repo;

    public FlightService(FlightRepository repo) {
        this.repo = repo;
    }


    public List<Flight> findAll() { return repo.findAll(); }

    public Optional<Flight> findById(Integer id) { return repo.findById(id); }

    public Flight save(Flight flight) { return repo.save(flight); }

    public void deleteById(Integer id) { repo.deleteById(id); }

    public Optional<Flight> findByDepartureAndArrival(String departure, String arrival) {
        return repo.findByDepartureAndArrival(departure, arrival);
    }

}
