package org.solen.configuration;

import org.solen.business.exceptions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Test
    void handleUserNotFoundById_returns404() {
        ResponseEntity<String> response = handler.handleUserNotByIdFound(new UserNotFoundByIdException(1L));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User with id 1 does not exist", response.getBody());
    }

    @Test
    void handleUserNotFoundByEmail_returns404() {
        ResponseEntity<String> response = handler.handleUserNotByEmailFound(new UserNotFoundByEmailException("a@b.com"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User with email a@b.com not found", response.getBody());
    }

    @Test
    void handleEmailAlreadyExists_returns409() {
        ResponseEntity<String> response = handler.handleEmailExists(new EmailAlreadyExistsException("a@b.com"));
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email a@b.com already exists", response.getBody());
    }

    @Test
    void handleStreakAlreadyUpdated_returns409() {
        ResponseEntity<String> response = handler.handleStreakUpdated(new StreakAlreadyUpdatedException());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Streak already updated!", response.getBody());
    }

    @Test
    void handleHabitAlreadyExists_returns409() {
        ResponseEntity<String> response = handler.handleHabitAlreadyExists(new HabitAlreadyExistsException());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Habit Already Exists!", response.getBody());
    }

    @Test
    void handleIllegalArgument_returns400() {
        ResponseEntity<String> response = handler.handleBadRequest(new IllegalArgumentException("invalid"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("invalid", response.getBody());
    }

    @Test
    void handleCheckInNotFound_returns404() {
        ResponseEntity<String> response = handler.handleCheckInNotFound(new CheckInNotFoundException(5L));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Check-in with id 5 not found", response.getBody());
    }

    @Test
    void handleCategoryNotFound_returns404() {
        ResponseEntity<String> response = handler.handleCategoryNotFoundById(new CategoryNotFoundByIdException(3L));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category with id 3 does not exist", response.getBody());
    }

    @Test
    void handleHabitNotFound_returns404() {
        ResponseEntity<String> response = handler.handleHabitNotFoundById(new HabitNotFoundByIdException(7L));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Habit with id 7 does not exist", response.getBody());
    }

    @Test
    void handleDataIntegrityViolation_returns409() {
        ResponseEntity<String> response = handler.handleDataIntegrityViolation(new DataIntegrityViolationException("violation"));
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Operation violates a database constraint.", response.getBody());
    }
}
