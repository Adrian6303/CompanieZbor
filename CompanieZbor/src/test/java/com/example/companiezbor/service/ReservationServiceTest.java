package com.example.companiezbor.service;

import com.example.companiezbor.model.Reservation;
import com.example.companiezbor.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation testReservation1;
    private Reservation testReservation2;
    private Reservation testReservation3;

    @BeforeEach
    void setUp() {
        testReservation1 = new Reservation();
        testReservation1.setId(1);
        testReservation1.setUserId(100);
        testReservation1.setFlightId(200);

        testReservation2 = new Reservation();
        testReservation2.setId(2);
        testReservation2.setUserId(101);
        testReservation2.setFlightId(201);

        testReservation3 = new Reservation();
        testReservation3.setId(3);
        testReservation3.setUserId(100);
        testReservation3.setFlightId(202);
    }

    @Test
    void testFindAll_ReturnsAllReservations() {
        // Arrange
        List<Reservation> reservations = Arrays.asList(testReservation1, testReservation2, testReservation3);
        when(reservationRepository.findAll()).thenReturn(reservations);

        // Act
        List<Reservation> result = reservationService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(100, result.get(0).getUserId());
        assertEquals(200, result.get(0).getFlightId());
        assertEquals(101, result.get(1).getUserId());
        assertEquals(201, result.get(1).getFlightId());
        assertEquals(100, result.get(2).getUserId());
        assertEquals(202, result.get(2).getFlightId());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsEmptyList() {
        // Arrange
        when(reservationRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Reservation> result = reservationService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testFindById_ReservationExists() {
        // Arrange
        when(reservationRepository.findById(1)).thenReturn(Optional.of(testReservation1));

        // Act
        Optional<Reservation> result = reservationService.findById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals(100, result.get().getUserId());
        assertEquals(200, result.get().getFlightId());
        verify(reservationRepository, times(1)).findById(1);
    }

    @Test
    void testFindById_ReservationDoesNotExist() {
        // Arrange
        when(reservationRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<Reservation> result = reservationService.findById(999);

        // Assert
        assertFalse(result.isPresent());
        verify(reservationRepository, times(1)).findById(999);
    }

    @Test
    void testSave_NewReservation() {
        // Arrange
        Reservation newReservation = new Reservation();
        newReservation.setUserId(102);
        newReservation.setFlightId(203);

        Reservation savedReservation = new Reservation();
        savedReservation.setId(4);
        savedReservation.setUserId(102);
        savedReservation.setFlightId(203);

        when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);

        // Act
        Reservation result = reservationService.save(newReservation);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.getId());
        assertEquals(102, result.getUserId());
        assertEquals(203, result.getFlightId());
        verify(reservationRepository, times(1)).save(newReservation);
    }

    @Test
    void testSave_UpdateExistingReservation() {
        // Arrange
        testReservation1.setFlightId(999);
        when(reservationRepository.save(testReservation1)).thenReturn(testReservation1);

        // Act
        Reservation result = reservationService.save(testReservation1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(100, result.getUserId());
        assertEquals(999, result.getFlightId());
        verify(reservationRepository, times(1)).save(testReservation1);
    }

    @Test
    void testDeleteById_Success() {
        // Arrange
        doNothing().when(reservationRepository).deleteById(1);

        // Act
        reservationService.deleteById(1);

        // Assert
        verify(reservationRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteById_NonExistentReservation() {
        // Arrange
        doNothing().when(reservationRepository).deleteById(999);

        // Act
        reservationService.deleteById(999);

        // Assert
        verify(reservationRepository, times(1)).deleteById(999);
    }

    @Test
    void testFindByUserId_ReservationExists() {
        // Arrange
        when(reservationRepository.findByUserId(100)).thenReturn(Arrays.asList(testReservation1, testReservation3));

        // Act
        List<Reservation> result = reservationService.findByUserId(100);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(100, result.get(0).getUserId());
        assertEquals(200, result.get(0).getFlightId());
        assertEquals(100, result.get(1).getUserId());
        assertEquals(202, result.get(1).getFlightId());
        verify(reservationRepository, times(1)).findByUserId(100);
    }

    @Test
    void testFindByUserId_ReservationDoesNotExist() {
        // Arrange
        when(reservationRepository.findByUserId(999)).thenReturn(Arrays.asList());

        // Act
        List<Reservation> result = reservationService.findByUserId(999);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(reservationRepository, times(1)).findByUserId(999);
    }

    @Test
    void testFindByFlightId_ReservationExists() {
        // Arrange
        when(reservationRepository.findByFlightId(200)).thenReturn(Arrays.asList(testReservation1));

        // Act
        List<Reservation> result = reservationService.findByFlightId(200);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getUserId());
        assertEquals(200, result.get(0).getFlightId());
        verify(reservationRepository, times(1)).findByFlightId(200);
    }

    @Test
    void testFindByFlightId_ReservationDoesNotExist() {
        // Arrange
        when(reservationRepository.findByFlightId(999)).thenReturn(Arrays.asList());

        // Act
        List<Reservation> result = reservationService.findByFlightId(999);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(reservationRepository, times(1)).findByFlightId(999);
    }

    @Test
    void testFindByUserIdAndFlightId_ReservationExists() {
        // Arrange
        when(reservationRepository.findByUserIdAndFlightId(100, 200))
                .thenReturn(Optional.of(testReservation1));

        // Act
        Optional<Reservation> result = reservationService.findByUserIdAndFlightId(100, 200);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(100, result.get().getUserId());
        assertEquals(200, result.get().getFlightId());
        assertEquals(1, result.get().getId());
        verify(reservationRepository, times(1)).findByUserIdAndFlightId(100, 200);
    }

    @Test
    void testFindByUserIdAndFlightId_ReservationDoesNotExist() {
        // Arrange
        when(reservationRepository.findByUserIdAndFlightId(999, 888))
                .thenReturn(Optional.empty());

        // Act
        Optional<Reservation> result = reservationService.findByUserIdAndFlightId(999, 888);

        // Assert
        assertFalse(result.isPresent());
        verify(reservationRepository, times(1)).findByUserIdAndFlightId(999, 888);
    }

    @Test
    void testFindByUserIdAndFlightId_ValidUserInvalidFlight() {
        // Arrange
        when(reservationRepository.findByUserIdAndFlightId(100, 999))
                .thenReturn(Optional.empty());

        // Act
        Optional<Reservation> result = reservationService.findByUserIdAndFlightId(100, 999);

        // Assert
        assertFalse(result.isPresent());
        verify(reservationRepository, times(1)).findByUserIdAndFlightId(100, 999);
    }

    @Test
    void testMultipleSaveOperations() {
        // Arrange
        when(reservationRepository.save(testReservation1)).thenReturn(testReservation1);
        when(reservationRepository.save(testReservation2)).thenReturn(testReservation2);

        // Act
        Reservation result1 = reservationService.save(testReservation1);
        Reservation result2 = reservationService.save(testReservation2);

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(1, result1.getId());
        assertEquals(2, result2.getId());
        verify(reservationRepository, times(2)).save(any(Reservation.class));
    }

    @Test
    void testSave_ReservationWithSameUserDifferentFlights() {
        // Arrange
        when(reservationRepository.save(testReservation1)).thenReturn(testReservation1);
        when(reservationRepository.save(testReservation3)).thenReturn(testReservation3);

        // Act
        Reservation result1 = reservationService.save(testReservation1);
        Reservation result3 = reservationService.save(testReservation3);

        // Assert
        assertNotNull(result1);
        assertNotNull(result3);
        assertEquals(100, result1.getUserId());
        assertEquals(100, result3.getUserId());
        assertEquals(200, result1.getFlightId());
        assertEquals(202, result3.getFlightId());
        verify(reservationRepository, times(2)).save(any(Reservation.class));
    }

    @Test
    void testFindByUserId_MultipleReservationsForSameUser() {
        // Arrange
        // User 100 has multiple reservations (testReservation1 and testReservation3)
        when(reservationRepository.findByUserId(100)).thenReturn(Arrays.asList(testReservation1, testReservation3));

        // Act
        List<Reservation> result = reservationService.findByUserId(100);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(100, result.get(0).getUserId());
        assertEquals(100, result.get(1).getUserId());
        verify(reservationRepository, times(1)).findByUserId(100);
    }

    @Test
    void testSave_UpdateUserIdAndFlightId() {
        // Arrange
        testReservation1.setUserId(999);
        testReservation1.setFlightId(888);
        when(reservationRepository.save(testReservation1)).thenReturn(testReservation1);

        // Act
        Reservation result = reservationService.save(testReservation1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(999, result.getUserId());
        assertEquals(888, result.getFlightId());
        verify(reservationRepository, times(1)).save(testReservation1);
    }
}

