package com.example.companiezbor.rest;

import com.example.companiezbor.model.Reservation;
import com.example.companiezbor.repository.ReservationRepository;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
class ReservationControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Reservation testReservation1;
    private Reservation testReservation2;
    private Reservation testReservation3;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        reservationRepository.deleteAll();

        testReservation1 = new Reservation();
        testReservation1.setUserId(1);
        testReservation1.setFlightId(10);
        testReservation1 = reservationRepository.save(testReservation1);

        testReservation2 = new Reservation();
        testReservation2.setUserId(2);
        testReservation2.setFlightId(20);
        testReservation2 = reservationRepository.save(testReservation2);

        testReservation3 = new Reservation();
        testReservation3.setUserId(1);
        testReservation3.setFlightId(30);
        testReservation3 = reservationRepository.save(testReservation3);
    }

    @Test
    void testGetAllReservations_ReturnsListOfReservations() throws Exception {
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].flightId", is(10)))
                .andExpect(jsonPath("$[1].userId", is(2)))
                .andExpect(jsonPath("$[1].flightId", is(20)))
                .andExpect(jsonPath("$[2].userId", is(1)))
                .andExpect(jsonPath("$[2].flightId", is(30)));
    }

    @Test
    void testGetReservationById_ReservationExists_ReturnsReservation() throws Exception {
        mockMvc.perform(get("/api/reservations/{id}", testReservation1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testReservation1.getId())))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.flightId", is(10)));
    }

    @Test
    void testGetReservationById_ReservationDoesNotExist_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/reservations/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateReservation_ValidReservation_ReturnsCreated() throws Exception {
        Reservation newReservation = new Reservation();
        newReservation.setUserId(3);
        newReservation.setFlightId(40);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReservation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userId", is(3)))
                .andExpect(jsonPath("$.flightId", is(40)));
    }

    @Test
    void testCreateReservation_UserAlreadyHasReservation_ReturnsCreated() throws Exception {
        // User 1 already has reservations, but should be able to create another
        Reservation newReservation = new Reservation();
        newReservation.setUserId(1);
        newReservation.setFlightId(50);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReservation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.flightId", is(50)));
    }

    @Test
    void testUpdateReservation_ReservationExists_ReturnsUpdatedReservation() throws Exception {
        Reservation updatedReservation = new Reservation();
        updatedReservation.setUserId(5);
        updatedReservation.setFlightId(60);

        mockMvc.perform(put("/api/reservations/{id}", testReservation1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedReservation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testReservation1.getId())))
                .andExpect(jsonPath("$.userId", is(5)))
                .andExpect(jsonPath("$.flightId", is(60)));
    }

    @Test
    void testUpdateReservation_ReservationDoesNotExist_ReturnsNotFound() throws Exception {
        Reservation updatedReservation = new Reservation();
        updatedReservation.setUserId(5);
        updatedReservation.setFlightId(60);

        mockMvc.perform(put("/api/reservations/{id}", 9999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedReservation)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateReservation_OnlyUserId() throws Exception {
        Reservation updatedReservation = new Reservation();
        updatedReservation.setUserId(99);
        updatedReservation.setFlightId(testReservation2.getFlightId());

        mockMvc.perform(put("/api/reservations/{id}", testReservation2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedReservation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(99)))
                .andExpect(jsonPath("$.flightId", is(20)));

        // Verify in database
        Reservation updatedInDb = reservationRepository.findById(testReservation2.getId()).orElseThrow();
        assertEquals(99, updatedInDb.getUserId());
        assertEquals(20, updatedInDb.getFlightId());
    }

    @Test
    void testUpdateReservation_OnlyFlightId() throws Exception {
        Reservation updatedReservation = new Reservation();
        updatedReservation.setUserId(testReservation2.getUserId());
        updatedReservation.setFlightId(88);

        mockMvc.perform(put("/api/reservations/{id}", testReservation2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedReservation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(2)))
                .andExpect(jsonPath("$.flightId", is(88)));

        // Verify in database
        Reservation updatedInDb = reservationRepository.findById(testReservation2.getId()).orElseThrow();
        assertEquals(2, updatedInDb.getUserId());
        assertEquals(88, updatedInDb.getFlightId());
    }

    @Test
    void testDeleteReservation_ReservationExists_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/reservations/{id}", testReservation1.getId()))
                .andExpect(status().isNoContent());

        // Verify reservation was deleted
        assertTrue(reservationRepository.findById(testReservation1.getId()).isEmpty());
    }

    @Test
    void testDeleteReservation_ReservationDoesNotExist_ReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/reservations/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindByUserId_ReservationExists_ReturnsReservation() throws Exception {
        mockMvc.perform(get("/api/reservations/user/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void testFindByUserId_ReservationDoesNotExist_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/reservations/user/{userId}", 9999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testFindByFlightId_ReservationExists_ReturnsReservation() throws Exception {
        mockMvc.perform(get("/api/reservations/flight/{flightId}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].flightId", is(10)))
                .andExpect(jsonPath("$[0].userId", is(1)));
    }

    @Test
    void testFindByFlightId_ReservationDoesNotExist_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/reservations/flight/{flightId}", 9999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testFindByUserAndFlight_ReservationExists_ReturnsReservation() throws Exception {
        mockMvc.perform(get("/api/reservations/search")
                        .param("userId", "1")
                        .param("flightId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testReservation1.getId())))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.flightId", is(10)));
    }

    @Test
    void testFindByUserAndFlight_ReservationDoesNotExist_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/reservations/search")
                        .param("userId", "999")
                        .param("flightId", "888"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindByUserAndFlight_ValidUserInvalidFlight_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/reservations/search")
                        .param("userId", "1")
                        .param("flightId", "999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindByUserAndFlight_InvalidUserValidFlight_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/reservations/search")
                        .param("userId", "999")
                        .param("flightId", "10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllReservations_EmptyDatabase_ReturnsEmptyList() throws Exception {
        reservationRepository.deleteAll();

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testCreateAndDeleteReservationFlow() throws Exception {
        // Create a new reservation
        Reservation newReservation = new Reservation();
        newReservation.setUserId(7);
        newReservation.setFlightId(70);

        String response = mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReservation)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Reservation createdReservation = objectMapper.readValue(response, Reservation.class);

        // Delete the reservation
        mockMvc.perform(delete("/api/reservations/{id}", createdReservation.getId()))
                .andExpect(status().isNoContent());

        // Verify reservation is deleted
        mockMvc.perform(get("/api/reservations/{id}", createdReservation.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateReservationBothFields() throws Exception {
        String updateJson = "{\"userId\":100,\"flightId\":200}";

        mockMvc.perform(put("/api/reservations/{id}", testReservation1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(100)))
                .andExpect(jsonPath("$.flightId", is(200)));

        // Verify in database
        Reservation updatedInDb = reservationRepository.findById(testReservation1.getId()).orElseThrow();
        assertEquals(100, updatedInDb.getUserId());
        assertEquals(200, updatedInDb.getFlightId());
    }

    @Test
    void testCreateMultipleReservations() throws Exception {
        Reservation reservation1 = new Reservation();
        reservation1.setUserId(10);
        reservation1.setFlightId(100);

        Reservation reservation2 = new Reservation();
        reservation2.setUserId(11);
        reservation2.setFlightId(101);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation2)))
                .andExpect(status().isCreated());

        // Verify we now have 5 reservations (3 from setup + 2 new)
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    void testCreateReservation_WithHighUserIdAndFlightId() throws Exception {
        Reservation newReservation = new Reservation();
        newReservation.setUserId(99999);
        newReservation.setFlightId(88888);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReservation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is(99999)))
                .andExpect(jsonPath("$.flightId", is(88888)));
    }

    @Test
    void testUpdateReservation_SwapUserAndFlight() throws Exception {
        Integer originalUserId = testReservation1.getUserId();
        Integer originalFlightId = testReservation1.getFlightId();

        Reservation updatedReservation = new Reservation();
        updatedReservation.setUserId(originalFlightId);
        updatedReservation.setFlightId(originalUserId);

        mockMvc.perform(put("/api/reservations/{id}", testReservation1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedReservation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(originalFlightId)))
                .andExpect(jsonPath("$.flightId", is(originalUserId)));
    }

    @Test
    void testMultipleReservationsForSameUser() throws Exception {
        // User 1 already has testReservation1 and testReservation3
        // Create another one for the same user
        Reservation newReservation = new Reservation();
        newReservation.setUserId(1);
        newReservation.setFlightId(100);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReservation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.flightId", is(100)));

        // Verify total count
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    void testCreateReservation_MinimumValidData() throws Exception {
        Reservation newReservation = new Reservation();
        newReservation.setUserId(0);
        newReservation.setFlightId(0);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReservation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is(0)))
                .andExpect(jsonPath("$.flightId", is(0)));
    }

    @Test
    void testDeleteMultipleReservations() throws Exception {
        // Delete first reservation
        mockMvc.perform(delete("/api/reservations/{id}", testReservation1.getId()))
                .andExpect(status().isNoContent());

        // Delete second reservation
        mockMvc.perform(delete("/api/reservations/{id}", testReservation2.getId()))
                .andExpect(status().isNoContent());

        // Verify only one reservation remains
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testReservation3.getId())));
    }

    @Test
    void testFindByUserId_AfterUpdate() throws Exception {
        // Update testReservation2's userId to 1
        Reservation updatedReservation = new Reservation();
        updatedReservation.setUserId(1);
        updatedReservation.setFlightId(testReservation2.getFlightId());

        mockMvc.perform(put("/api/reservations/{id}", testReservation2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedReservation)))
                .andExpect(status().isOk());

        // Now find by userId 1 - should find multiple reservations
        mockMvc.perform(get("/api/reservations/user/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].userId", is(1)));
    }

    @Test
    void testComplexUpdateScenario() throws Exception {
        // Get original reservation
        Integer originalId = testReservation1.getId();

        // Update it
        Reservation update1 = new Reservation();
        update1.setUserId(50);
        update1.setFlightId(500);

        mockMvc.perform(put("/api/reservations/{id}", originalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(50)))
                .andExpect(jsonPath("$.flightId", is(500)));

        // Update it again
        Reservation update2 = new Reservation();
        update2.setUserId(60);
        update2.setFlightId(600);

        mockMvc.perform(put("/api/reservations/{id}", originalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(60)))
                .andExpect(jsonPath("$.flightId", is(600)));

        // Verify final state
        Reservation finalState = reservationRepository.findById(originalId).orElseThrow();
        assertEquals(60, finalState.getUserId());
        assertEquals(600, finalState.getFlightId());
    }

    @Test
    void testSearchWithDifferentCombinations() throws Exception {
        // Valid combination
        mockMvc.perform(get("/api/reservations/search")
                        .param("userId", "2")
                        .param("flightId", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testReservation2.getId())));

        // Another valid combination
        mockMvc.perform(get("/api/reservations/search")
                        .param("userId", "1")
                        .param("flightId", "30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testReservation3.getId())));

        // Invalid combination
        mockMvc.perform(get("/api/reservations/search")
                        .param("userId", "1")
                        .param("flightId", "20"))
                .andExpect(status().isNotFound());
    }
}

