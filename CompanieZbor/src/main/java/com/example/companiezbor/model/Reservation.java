package com.example.companiezbor.model;
import com.example.companiezbor.model.Flight;
import jakarta.persistence.*;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="user_id", nullable = false)
    private Integer userId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "flight_id", nullable = false)
//    private Flight flight;

    @Column(name="flight_id", nullable = false)
    private Integer flightId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFlightId() {
        return flightId;
    }
    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }
}
