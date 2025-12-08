package com.example.companiezbor.repository;

import com.example.companiezbor.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Optional<Reservation> findByUserId(Integer id);

    Optional<Reservation> findByFlightId(Integer id);

    Optional<Reservation> findByUserIdAndFlightId(Integer id, Integer flightId);
}