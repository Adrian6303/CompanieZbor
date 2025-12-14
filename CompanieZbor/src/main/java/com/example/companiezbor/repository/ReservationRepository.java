package com.example.companiezbor.repository;

import com.example.companiezbor.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByUserId(Integer id);

    List<Reservation> findByFlightId(Integer id);

    Optional<Reservation> findByUserIdAndFlightId(Integer id, Integer flightId);
}