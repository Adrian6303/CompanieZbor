package com.example.companiezbor.rest;

import com.example.companiezbor.model.Flight;
import com.example.companiezbor.repository.FlightRepository;
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

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
class FlightControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Flight testFlight1;
    private Flight testFlight2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        flightRepository.deleteAll();

        testFlight1 = new Flight();
        testFlight1.setDeparture("Bucharest");
        testFlight1.setArrival("Paris");
        testFlight1.setDeparture_time(LocalDateTime.of(2025, 12, 20, 10, 30));
        testFlight1.setDuration(180);
        testFlight1.setNr_seats(150);
        testFlight1.setPlane_name("Boeing 737");
        testFlight1.setPrice(299.99);
        testFlight1 = flightRepository.save(testFlight1);

        testFlight2 = new Flight();
        testFlight2.setDeparture("London");
        testFlight2.setArrival("New York");
        testFlight2.setDeparture_time(LocalDateTime.of(2025, 12, 25, 14, 0));
        testFlight2.setDuration(480);
        testFlight2.setNr_seats(200);
        testFlight2.setPlane_name("Airbus A380");
        testFlight2.setPrice(599.99);
        testFlight2 = flightRepository.save(testFlight2);
    }

    @Test
    void testGetAllFlights_ReturnsListOfFlights() throws Exception {
        mockMvc.perform(get("/api/flights"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].departure", is("Bucharest")))
                .andExpect(jsonPath("$[0].arrival", is("Paris")))
                .andExpect(jsonPath("$[0].duration", is(180)))
                .andExpect(jsonPath("$[0].nr_seats", is(150)))
                .andExpect(jsonPath("$[0].plane_name", is("Boeing 737")))
                .andExpect(jsonPath("$[0].price", is(299.99)))
                .andExpect(jsonPath("$[1].departure", is("London")))
                .andExpect(jsonPath("$[1].arrival", is("New York")));
    }

    @Test
    void testGetFlightById_FlightExists_ReturnsFlight() throws Exception {
        mockMvc.perform(get("/api/flights/{id}", testFlight1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testFlight1.getId())))
                .andExpect(jsonPath("$.departure", is("Bucharest")))
                .andExpect(jsonPath("$.arrival", is("Paris")))
                .andExpect(jsonPath("$.duration", is(180)))
                .andExpect(jsonPath("$.nr_seats", is(150)))
                .andExpect(jsonPath("$.plane_name", is("Boeing 737")))
                .andExpect(jsonPath("$.price", is(299.99)));
    }

    @Test
    void testGetFlightById_FlightDoesNotExist_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/flights/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateFlight_ValidFlight_ReturnsCreated() throws Exception {
        Flight newFlight = new Flight();
        newFlight.setDeparture("Rome");
        newFlight.setArrival("Berlin");
        newFlight.setDeparture_time(LocalDateTime.of(2025, 12, 30, 9, 0));
        newFlight.setDuration(120);
        newFlight.setNr_seats(100);
        newFlight.setPlane_name("Boeing 777");
        newFlight.setPrice(199.99);

        mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFlight)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.departure", is("Rome")))
                .andExpect(jsonPath("$.arrival", is("Berlin")))
                .andExpect(jsonPath("$.duration", is(120)))
                .andExpect(jsonPath("$.nr_seats", is(100)))
                .andExpect(jsonPath("$.plane_name", is("Boeing 777")))
                .andExpect(jsonPath("$.price", is(199.99)));
    }

    @Test
    void testCreateFlight_WithoutPlaneName_ReturnsCreated() throws Exception {
        Flight newFlight = new Flight();
        newFlight.setDeparture("Madrid");
        newFlight.setArrival("Lisbon");
        newFlight.setDeparture_time(LocalDateTime.of(2026, 1, 5, 11, 0));
        newFlight.setDuration(90);
        newFlight.setNr_seats(80);
        newFlight.setPlane_name(null);
        newFlight.setPrice(89.99);

        mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFlight)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.departure", is("Madrid")))
                .andExpect(jsonPath("$.arrival", is("Lisbon")))
                .andExpect(jsonPath("$.plane_name").doesNotExist());
    }

    @Test
    void testUpdateFlight_FlightExists_ReturnsUpdatedFlight() throws Exception {
        Flight updatedFlight = new Flight();
        updatedFlight.setDeparture("Bucharest");
        updatedFlight.setArrival("London");
        updatedFlight.setDeparture_time(LocalDateTime.of(2025, 12, 22, 15, 45));
        updatedFlight.setDuration(200);
        updatedFlight.setNr_seats(160);
        updatedFlight.setPlane_name("Boeing 787");
        updatedFlight.setPrice(349.99);

        mockMvc.perform(put("/api/flights/{id}", testFlight1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFlight)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testFlight1.getId())))
                .andExpect(jsonPath("$.departure", is("Bucharest")))
                .andExpect(jsonPath("$.arrival", is("London")))
                .andExpect(jsonPath("$.duration", is(200)))
                .andExpect(jsonPath("$.nr_seats", is(160)))
                .andExpect(jsonPath("$.plane_name", is("Boeing 787")))
                .andExpect(jsonPath("$.price", is(349.99)));
    }

    @Test
    void testUpdateFlight_FlightDoesNotExist_ReturnsNotFound() throws Exception {
        Flight updatedFlight = new Flight();
        updatedFlight.setDeparture("Rome");
        updatedFlight.setArrival("Berlin");
        updatedFlight.setDeparture_time(LocalDateTime.of(2025, 12, 30, 9, 0));
        updatedFlight.setDuration(120);
        updatedFlight.setNr_seats(100);
        updatedFlight.setPlane_name("Boeing 777");
        updatedFlight.setPrice(199.99);

        mockMvc.perform(put("/api/flights/{id}", 9999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFlight)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateFlight_PartialUpdate_ReturnsUpdatedFlight() throws Exception {
        Flight updatedFlight = new Flight();
        updatedFlight.setDeparture(testFlight1.getDeparture());
        updatedFlight.setArrival(testFlight1.getArrival());
        updatedFlight.setDeparture_time(testFlight1.getDeparture_time());
        updatedFlight.setDuration(testFlight1.getDuration());
        updatedFlight.setNr_seats(testFlight1.getNr_seats());
        updatedFlight.setPlane_name("Updated Plane");
        updatedFlight.setPrice(399.99);

        mockMvc.perform(put("/api/flights/{id}", testFlight1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFlight)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plane_name", is("Updated Plane")))
                .andExpect(jsonPath("$.price", is(399.99)));

        // Verify in database
        Flight updatedInDb = flightRepository.findById(testFlight1.getId()).orElseThrow();
        assertEquals("Updated Plane", updatedInDb.getPlane_name());
        assertEquals(399.99, updatedInDb.getPrice());
    }

    @Test
    void testDeleteFlight_FlightExists_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/flights/{id}", testFlight1.getId()))
                .andExpect(status().isNoContent());

        // Verify flight was deleted
        assertTrue(flightRepository.findById(testFlight1.getId()).isEmpty());
    }

    @Test
    void testDeleteFlight_FlightDoesNotExist_ReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/flights/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindByDepartureAndArrival_FlightExists_ReturnsFlight() throws Exception {
        mockMvc.perform(get("/api/flights/search")
                        .param("departure", "Bucharest")
                        .param("arrival", "Paris"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testFlight1.getId())))
                .andExpect(jsonPath("$.departure", is("Bucharest")))
                .andExpect(jsonPath("$.arrival", is("Paris")))
                .andExpect(jsonPath("$.price", is(299.99)));
    }

    @Test
    void testFindByDepartureAndArrival_FlightDoesNotExist_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/flights/search")
                        .param("departure", "Tokyo")
                        .param("arrival", "Sydney"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllFlights_EmptyDatabase_ReturnsEmptyList() throws Exception {
        flightRepository.deleteAll();

        mockMvc.perform(get("/api/flights"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testCreateAndDeleteFlightFlow() throws Exception {
        // Create a new flight
        Flight newFlight = new Flight();
        newFlight.setDeparture("Vienna");
        newFlight.setArrival("Prague");
        newFlight.setDeparture_time(LocalDateTime.of(2026, 1, 10, 8, 30));
        newFlight.setDuration(60);
        newFlight.setNr_seats(50);
        newFlight.setPlane_name("Airbus A320");
        newFlight.setPrice(79.99);

        String response = mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFlight)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Flight createdFlight = objectMapper.readValue(response, Flight.class);

        // Delete the flight
        mockMvc.perform(delete("/api/flights/{id}", createdFlight.getId()))
                .andExpect(status().isNoContent());

        // Verify flight is deleted
        mockMvc.perform(get("/api/flights/{id}", createdFlight.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateFlightAllFields() throws Exception {
        String updateJson = "{\"departure\":\"Tokyo\",\"arrival\":\"Seoul\"," +
                "\"departure_time\":\"2026-02-15T18:00:00\"," +
                "\"duration\":150,\"nr_seats\":180," +
                "\"plane_name\":\"Boeing 747\",\"price\":450.00}";

        mockMvc.perform(put("/api/flights/{id}", testFlight1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departure", is("Tokyo")))
                .andExpect(jsonPath("$.arrival", is("Seoul")))
                .andExpect(jsonPath("$.duration", is(150)))
                .andExpect(jsonPath("$.nr_seats", is(180)))
                .andExpect(jsonPath("$.plane_name", is("Boeing 747")))
                .andExpect(jsonPath("$.price", is(450.0)));

        // Verify in database
        Flight updatedInDb = flightRepository.findById(testFlight1.getId()).orElseThrow();
        assertEquals("Tokyo", updatedInDb.getDeparture());
        assertEquals("Seoul", updatedInDb.getArrival());
        assertEquals(150, updatedInDb.getDuration());
        assertEquals(180, updatedInDb.getNr_seats());
        assertEquals("Boeing 747", updatedInDb.getPlane_name());
        assertEquals(450.0, updatedInDb.getPrice());
    }

    @Test
    void testCreateFlight_LongHaulFlight() throws Exception {
        Flight longHaulFlight = new Flight();
        longHaulFlight.setDeparture("Los Angeles");
        longHaulFlight.setArrival("Singapore");
        longHaulFlight.setDeparture_time(LocalDateTime.of(2026, 3, 1, 22, 0));
        longHaulFlight.setDuration(960); // 16 hours
        longHaulFlight.setNr_seats(350);
        longHaulFlight.setPlane_name("Airbus A350");
        longHaulFlight.setPrice(1299.99);

        mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(longHaulFlight)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.departure", is("Los Angeles")))
                .andExpect(jsonPath("$.arrival", is("Singapore")))
                .andExpect(jsonPath("$.duration", is(960)))
                .andExpect(jsonPath("$.nr_seats", is(350)))
                .andExpect(jsonPath("$.price", is(1299.99)));
    }

    @Test
    void testUpdateFlight_OnlyPrice() throws Exception {
        Flight updatedFlight = new Flight();
        updatedFlight.setDeparture(testFlight2.getDeparture());
        updatedFlight.setArrival(testFlight2.getArrival());
        updatedFlight.setDeparture_time(testFlight2.getDeparture_time());
        updatedFlight.setDuration(testFlight2.getDuration());
        updatedFlight.setNr_seats(testFlight2.getNr_seats());
        updatedFlight.setPlane_name(testFlight2.getPlane_name());
        updatedFlight.setPrice(699.99); // Only changing price

        mockMvc.perform(put("/api/flights/{id}", testFlight2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFlight)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(699.99)))
                .andExpect(jsonPath("$.departure", is("London")))
                .andExpect(jsonPath("$.arrival", is("New York")));
    }

    @Test
    void testSearchFlight_CaseSensitive() throws Exception {
        // Assuming the search is case-sensitive based on the repository method
        mockMvc.perform(get("/api/flights/search")
                        .param("departure", "bucharest") // lowercase
                        .param("arrival", "Paris"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateMultipleFlights() throws Exception {
        Flight flight1 = new Flight();
        flight1.setDeparture("Dublin");
        flight1.setArrival("Amsterdam");
        flight1.setDeparture_time(LocalDateTime.of(2026, 1, 15, 7, 0));
        flight1.setDuration(100);
        flight1.setNr_seats(120);
        flight1.setPlane_name("Boeing 737");
        flight1.setPrice(149.99);

        Flight flight2 = new Flight();
        flight2.setDeparture("Brussels");
        flight2.setArrival("Copenhagen");
        flight2.setDeparture_time(LocalDateTime.of(2026, 1, 16, 12, 30));
        flight2.setDuration(110);
        flight2.setNr_seats(130);
        flight2.setPlane_name("Airbus A319");
        flight2.setPrice(159.99);

        mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flight1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flight2)))
                .andExpect(status().isCreated());

        // Verify we now have 4 flights (2 from setup + 2 new)
        mockMvc.perform(get("/api/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }
}

