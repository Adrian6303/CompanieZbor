package com.example.companiezbor.rest;

import com.example.companiezbor.model.User;
import com.example.companiezbor.repository.UserRepository;
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
class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private User adminUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setAdmin(false);
        testUser = userRepository.save(testUser);

        adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword("admin123");
        adminUser.setAdmin(true);
        adminUser = userRepository.save(adminUser);
    }

    @Test
    void testGetAllUsers_ReturnsListOfUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("testuser")))
                .andExpect(jsonPath("$[0].admin", is(false)))
                .andExpect(jsonPath("$[0].password").doesNotExist()) // Password should not be in response
                .andExpect(jsonPath("$[1].username", is("admin")))
                .andExpect(jsonPath("$[1].admin", is(true)));
    }

    @Test
    void testGetUserById_UserExists_ReturnsUser() throws Exception {
        mockMvc.perform(get("/api/users/{id}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testUser.getId())))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.admin", is(false)))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void testGetUserById_UserDoesNotExist_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser_ValidUser_ReturnsCreated() throws Exception {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("newpass");
        newUser.setAdmin(false);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username", is("newuser")))
                .andExpect(jsonPath("$.admin", is(false)))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void testCreateUser_AdminUser_ReturnsCreated() throws Exception {
        User newAdmin = new User();
        newAdmin.setUsername("newadmin");
        newAdmin.setPassword("adminpass");
        newAdmin.setAdmin(true);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAdmin)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("newadmin")))
                .andExpect(jsonPath("$.admin", is(true)));
    }

    @Test
    void testUpdateUser_UserExists_ReturnsUpdatedUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setUsername("updateduser");
        updatedUser.setPassword("newpassword");
        updatedUser.setAdmin(true);

        mockMvc.perform(put("/api/users/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUser.getId())))
                .andExpect(jsonPath("$.username", is("updateduser")))
                .andExpect(jsonPath("$.admin", is(true)))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void testUpdateUser_UserDoesNotExist_ReturnsNotFound() throws Exception {
        User updatedUser = new User();
        updatedUser.setUsername("updateduser");
        updatedUser.setPassword("newpassword");
        updatedUser.setAdmin(false);

        mockMvc.perform(put("/api/users/{id}", 9999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUser_PasswordIsNull_DoesNotUpdatePassword() throws Exception {
        String originalPassword = testUser.getPassword();

        User updatedUser = new User();
        updatedUser.setUsername("updateduser");
        updatedUser.setPassword(null); // Not updating password
        updatedUser.setAdmin(false);

        mockMvc.perform(put("/api/users/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("updateduser")));

        // Verify password wasn't changed
        User userInDb = userRepository.findById(testUser.getId()).orElseThrow();
        assertEquals(originalPassword, userInDb.getPassword());
    }

    @Test
    void testDeleteUser_UserExists_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", testUser.getId()))
                .andExpect(status().isNoContent());

        // Verify user was deleted
        assertTrue(userRepository.findById(testUser.getId()).isEmpty());
    }

    @Test
    void testDeleteUser_UserDoesNotExist_ReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testLogin_ValidCredentials_ReturnsUser() throws Exception {
        String loginJson = "{\"username\":\"testuser\",\"password\":\"password123\"}";

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUser.getId())))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.admin", is(false)));
    }

    @Test
    void testLogin_InvalidPassword_ReturnsUnauthorized() throws Exception {
        String loginJson = "{\"username\":\"testuser\",\"password\":\"wrongpassword\"}";

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogin_InvalidUsername_ReturnsUnauthorized() throws Exception {
        String loginJson = "{\"username\":\"nonexistent\",\"password\":\"password123\"}";

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogin_AdminUser_ReturnsAdminUser() throws Exception {
        String loginJson = "{\"username\":\"admin\",\"password\":\"admin123\"}";

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("admin")))
                .andExpect(jsonPath("$.admin", is(true)));
    }

    @Test
    void testGetAllUsers_EmptyDatabase_ReturnsEmptyList() throws Exception {
        userRepository.deleteAll();

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testCreateAndDeleteUserFlow() throws Exception {
        // Create a new user
        User newUser = new User();
        newUser.setUsername("tempuser");
        newUser.setPassword("temppass");
        newUser.setAdmin(false);

        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        User createdUser = objectMapper.readValue(response, User.class);

        // Delete the user
        mockMvc.perform(delete("/api/users/{id}", createdUser.getId()))
                .andExpect(status().isNoContent());

        // Verify user is deleted
        mockMvc.perform(get("/api/users/{id}", createdUser.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUserMultipleFields() throws Exception {
        String updateJson = "{\"username\":\"completelynewname\",\"password\":\"completelynewpass\",\"admin\":true}";

        mockMvc.perform(put("/api/users/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("completelynewname")))
                .andExpect(jsonPath("$.admin", is(true)));

        // Verify in database
        User updatedInDb = userRepository.findById(testUser.getId()).orElseThrow();
        assertEquals("completelynewname", updatedInDb.getUsername());
        assertEquals("completelynewpass", updatedInDb.getPassword());
        assertTrue(updatedInDb.isAdmin());
    }
}

