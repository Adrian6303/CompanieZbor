package com.example.companiezbor.service;

import com.example.companiezbor.model.Flight;
import com.example.companiezbor.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    private Flight testFlight1;
    private Flight testFlight2;

    @BeforeEach
    void setUp() {
        testFlight1 = new Flight();
        testFlight1.setId(1);
        testFlight1.setDeparture("Bucharest");
        testFlight1.setArrival("Paris");
        testFlight1.setDeparture_time(LocalDateTime.of(2025, 12, 20, 10, 30));
        testFlight1.setDuration(180);
        testFlight1.setNr_seats(150);
        testFlight1.setPlane_name("Boeing 737");
        testFlight1.setPrice(299.99);

        testFlight2 = new Flight();
        testFlight2.setId(2);
        testFlight2.setDeparture("London");
        testFlight2.setArrival("New York");
        testFlight2.setDeparture_time(LocalDateTime.of(2025, 12, 25, 14, 0));
        testFlight2.setDuration(480);
        testFlight2.setNr_seats(200);
        testFlight2.setPlane_name("Airbus A380");
        testFlight2.setPrice(599.99);
    }

    @Test
    void testFindAll_ReturnsAllFlights() {
        // Arrange
        List<Flight> flights = Arrays.asList(testFlight1, testFlight2);
        when(flightRepository.findAll()).thenReturn(flights);

        // Act
        List<Flight> result = flightService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Bucharest", result.get(0).getDeparture());
        assertEquals("Paris", result.get(0).getArrival());
        assertEquals("London", result.get(1).getDeparture());
        assertEquals("New York", result.get(1).getArrival());
        verify(flightRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsEmptyList() {
        // Arrange
        when(flightRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Flight> result = flightService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(flightRepository, times(1)).findAll();
    }

    @Test
    void testFindById_FlightExists() {
        // Arrange
        when(flightRepository.findById(1)).thenReturn(Optional.of(testFlight1));

        // Act
        Optional<Flight> result = flightService.findById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("Bucharest", result.get().getDeparture());
        assertEquals("Paris", result.get().getArrival());
        assertEquals(150, result.get().getNr_seats());
        assertEquals(299.99, result.get().getPrice());
        verify(flightRepository, times(1)).findById(1);
    }

    @Test
    void testFindById_FlightDoesNotExist() {
        // Arrange
        when(flightRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<Flight> result = flightService.findById(999);

        // Assert
        assertFalse(result.isPresent());
        verify(flightRepository, times(1)).findById(999);
    }

    @Test
    void testSave_NewFlight() {
        // Arrange
        Flight newFlight = new Flight();
        newFlight.setDeparture("Rome");
        newFlight.setArrival("Berlin");
        newFlight.setDeparture_time(LocalDateTime.of(2025, 12, 30, 9, 0));
        newFlight.setDuration(120);
        newFlight.setNr_seats(100);
        newFlight.setPlane_name("Boeing 777");
        newFlight.setPrice(199.99);

        Flight savedFlight = new Flight();
        savedFlight.setId(3);
        savedFlight.setDeparture("Rome");
        savedFlight.setArrival("Berlin");
        savedFlight.setDeparture_time(LocalDateTime.of(2025, 12, 30, 9, 0));
        savedFlight.setDuration(120);
        savedFlight.setNr_seats(100);
        savedFlight.setPlane_name("Boeing 777");
        savedFlight.setPrice(199.99);

        when(flightRepository.save(any(Flight.class))).thenReturn(savedFlight);

        // Act
        Flight result = flightService.save(newFlight);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getId());
        assertEquals("Rome", result.getDeparture());
        assertEquals("Berlin", result.getArrival());
        assertEquals(120, result.getDuration());
        assertEquals(100, result.getNr_seats());
        assertEquals("Boeing 777", result.getPlane_name());
        assertEquals(199.99, result.getPrice());
        verify(flightRepository, times(1)).save(newFlight);
    }

    @Test
    void testSave_UpdateExistingFlight() {
        // Arrange
        testFlight1.setPrice(349.99);
        testFlight1.setNr_seats(140);
        when(flightRepository.save(testFlight1)).thenReturn(testFlight1);

        // Act
        Flight result = flightService.save(testFlight1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(349.99, result.getPrice());
        assertEquals(140, result.getNr_seats());
        verify(flightRepository, times(1)).save(testFlight1);
    }

    @Test
    void testDeleteById_Success() {
        // Arrange
        doNothing().when(flightRepository).deleteById(1);

        // Act
        flightService.deleteById(1);

        // Assert
        verify(flightRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteById_NonExistentFlight() {
        // Arrange
        doNothing().when(flightRepository).deleteById(999);

        // Act
        flightService.deleteById(999);

        // Assert
        verify(flightRepository, times(1)).deleteById(999);
    }

    @Test
    void testFindByDepartureAndArrival_FlightExists() {
        // Arrange
        when(flightRepository.findByDepartureAndArrival("Bucharest", "Paris"))
                .thenReturn(Optional.of(testFlight1));

        // Act
        Optional<Flight> result = flightService.findByDepartureAndArrival("Bucharest", "Paris");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Bucharest", result.get().getDeparture());
        assertEquals("Paris", result.get().getArrival());
        assertEquals(299.99, result.get().getPrice());
        verify(flightRepository, times(1)).findByDepartureAndArrival("Bucharest", "Paris");
    }

    @Test
    void testFindByDepartureAndArrival_FlightDoesNotExist() {
        // Arrange
        when(flightRepository.findByDepartureAndArrival("Tokyo", "Sydney"))
                .thenReturn(Optional.empty());

        // Act
        Optional<Flight> result = flightService.findByDepartureAndArrival("Tokyo", "Sydney");

        // Assert
        assertFalse(result.isPresent());
        verify(flightRepository, times(1)).findByDepartureAndArrival("Tokyo", "Sydney");
    }

    @Test
    void testSave_FlightWithNullPlaneName() {
        // Arrange
        Flight flightWithoutPlaneName = new Flight();
        flightWithoutPlaneName.setId(4);
        flightWithoutPlaneName.setDeparture("Madrid");
        flightWithoutPlaneName.setArrival("Lisbon");
        flightWithoutPlaneName.setDeparture_time(LocalDateTime.of(2026, 1, 5, 11, 0));
        flightWithoutPlaneName.setDuration(90);
        flightWithoutPlaneName.setNr_seats(80);
        flightWithoutPlaneName.setPlane_name(null);
        flightWithoutPlaneName.setPrice(89.99);

        when(flightRepository.save(any(Flight.class))).thenReturn(flightWithoutPlaneName);

        // Act
        Flight result = flightService.save(flightWithoutPlaneName);

        // Assert
        assertNotNull(result);
        assertNull(result.getPlane_name());
        assertEquals("Madrid", result.getDeparture());
        assertEquals("Lisbon", result.getArrival());
        verify(flightRepository, times(1)).save(flightWithoutPlaneName);
    }

    @Test
    void testMultipleSaveOperations() {
        // Arrange
        when(flightRepository.save(testFlight1)).thenReturn(testFlight1);
        when(flightRepository.save(testFlight2)).thenReturn(testFlight2);

        // Act
        Flight result1 = flightService.save(testFlight1);
        Flight result2 = flightService.save(testFlight2);

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(1, result1.getId());
        assertEquals(2, result2.getId());
        verify(flightRepository, times(2)).save(any(Flight.class));
    }

    @Test
    void testSave_FlightWithAllFields() {
        // Arrange
        when(flightRepository.save(testFlight2)).thenReturn(testFlight2);

        // Act
        Flight result = flightService.save(testFlight2);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getId());
        assertEquals("London", result.getDeparture());
        assertEquals("New York", result.getArrival());
        assertEquals(LocalDateTime.of(2025, 12, 25, 14, 0), result.getDeparture_time());
        assertEquals(480, result.getDuration());
        assertEquals(200, result.getNr_seats());
        assertEquals("Airbus A380", result.getPlane_name());
        assertEquals(599.99, result.getPrice());
        verify(flightRepository, times(1)).save(testFlight2);
    }

    @Test
    void testFindByDepartureAndArrival_WithDifferentCases() {
        // Arrange
        when(flightRepository.findByDepartureAndArrival("BUCHAREST", "PARIS"))
                .thenReturn(Optional.empty());

        // Act
        Optional<Flight> result = flightService.findByDepartureAndArrival("BUCHAREST", "PARIS");

        // Assert
        assertFalse(result.isPresent());
        verify(flightRepository, times(1)).findByDepartureAndArrival("BUCHAREST", "PARIS");
    }
}

