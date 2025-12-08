package com.example.companiezbor.rest;


import com.example.companiezbor.model.Flight;
import com.example.companiezbor.service.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService service;

    public FlightController(FlightService service) { this.service = service; }

    @GetMapping
    public List<Flight> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> get(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Flight> create(@RequestBody Flight flight) {
        Flight saved = service.save(flight);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return service.findById(id).map(f -> {
            service.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Flight> findByDepartureAndArrival(@RequestParam String departure,
                                                            @RequestParam String arrival) {
        return service.findByDepartureAndArrival(departure, arrival)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
