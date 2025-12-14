package com.example.companiezbor.service;

import com.example.companiezbor.model.User;
import com.example.companiezbor.repository.UserRepository;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private User adminUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setAdmin(false);

        adminUser = new User();
        adminUser.setId(2);
        adminUser.setUsername("admin");
        adminUser.setPassword("admin123");
        adminUser.setAdmin(true);
    }

    @Test
    void testFindAll_ReturnsAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(testUser, adminUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        assertEquals("admin", result.get(1).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsEmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<User> result = userService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindById_UserExists() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.findById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("testuser", result.get().getUsername());
        assertFalse(result.get().isAdmin());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testFindById_UserDoesNotExist() {
        // Arrange
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findById(999);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(999);
    }

    @Test
    void testSave_NewUser() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("newpass");
        newUser.setAdmin(false);

        User savedUser = new User();
        savedUser.setId(3);
        savedUser.setUsername("newuser");
        savedUser.setPassword("newpass");
        savedUser.setAdmin(false);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.save(newUser);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getId());
        assertEquals("newuser", result.getUsername());
        assertEquals("newpass", result.getPassword());
        assertFalse(result.isAdmin());
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void testSave_UpdateExistingUser() {
        // Arrange
        testUser.setUsername("updateduser");
        when(userRepository.save(testUser)).thenReturn(testUser);

        // Act
        User result = userService.save(testUser);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("updateduser", result.getUsername());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testSave_AdminUser() {
        // Arrange
        when(userRepository.save(adminUser)).thenReturn(adminUser);

        // Act
        User result = userService.save(adminUser);

        // Assert
        assertNotNull(result);
        assertTrue(result.isAdmin());
        assertEquals("admin", result.getUsername());
        verify(userRepository, times(1)).save(adminUser);
    }

    @Test
    void testDeleteById_Success() {
        // Arrange
        doNothing().when(userRepository).deleteById(1);

        // Act
        userService.deleteById(1);

        // Assert
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteById_NonExistentUser() {
        // Arrange
        doNothing().when(userRepository).deleteById(999);

        // Act
        userService.deleteById(999);

        // Assert
        verify(userRepository, times(1)).deleteById(999);
    }

    @Test
    void testSave_WithNullPassword() {
        // Arrange
        User userWithNullPassword = new User();
        userWithNullPassword.setId(4);
        userWithNullPassword.setUsername("nopassuser");
        userWithNullPassword.setPassword(null);
        userWithNullPassword.setAdmin(false);

        when(userRepository.save(any(User.class))).thenReturn(userWithNullPassword);

        // Act
        User result = userService.save(userWithNullPassword);

        // Assert
        assertNotNull(result);
        assertNull(result.getPassword());
        assertEquals("nopassuser", result.getUsername());
        verify(userRepository, times(1)).save(userWithNullPassword);
    }

    @Test
    void testMultipleSaveOperations() {
        // Arrange
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userRepository.save(adminUser)).thenReturn(adminUser);

        // Act
        User result1 = userService.save(testUser);
        User result2 = userService.save(adminUser);

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(1, result1.getId());
        assertEquals(2, result2.getId());
        verify(userRepository, times(2)).save(any(User.class));
    }
}

