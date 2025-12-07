package com.example.companiezbor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "flight")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String departure;

    @Column(nullable = false)
    private String arrival;

    @Column(nullable = false)
    private LocalDateTime departure_time;

    @Column(nullable = false)
    private int duration;


    @Column(nullable = false)
    private Integer nr_seats;

    @Column
    private String plane_name;

    @Column(nullable = false)
    private Double price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String destination) {
        this.arrival = destination;
    }

    public LocalDateTime getDeparture_time() {
        return departure_time;
    }
    public void setDeparture_time(LocalDateTime departure_time) {
        this.departure_time = departure_time;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public String getPlane_name() {
        return plane_name;
    }
    public void setPlane_name(String plane_name) {
        this.plane_name = plane_name;
    }

    public Integer getNr_seats() {
        return nr_seats;
    }

    public void setNr_seats(Integer capacity) {
        this.nr_seats = capacity;
    }
}
