package com.example.companiezbor.service;

import com.example.companiezbor.model.Reservation;
import com.example.companiezbor.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository repo;

    public ReservationService(ReservationRepository repo) {
        this.repo = repo;
    }


    public List<Reservation> findAll() {
        return repo.findAll();
    }
    public Optional<Reservation> findById(Integer id) {
        return repo.findById(id);
    }
    public Reservation save(Reservation reservation) {
        return repo.save(reservation);
    }
    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

    public Optional<Reservation> findByUserId(Integer id) {
        return repo.findByUserId(id);
    }

    public Optional<Reservation> findByFlightId(Integer id) {
        return repo.findByFlightId(id);
    }

    public Optional<Reservation> findByUserIdAndFlightId(Integer id, Integer flightId) {
        return repo.findByUserIdAndFlightId(id, flightId);
    }

}
