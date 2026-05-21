package org.solen.configuration;

import org.solen.business.exceptions.CategoryNotFoundByIdException;
import org.solen.business.exceptions.CheckInNotFoundException;
import org.solen.business.exceptions.EmailAlreadyExistsException;
import org.solen.business.exceptions.PracticeAlreadyExistsException;
import org.solen.business.exceptions.PracticeNotFoundByIdException;
import org.solen.business.exceptions.StreakAlreadyUpdatedException;
import org.solen.business.exceptions.UserNotFoundByEmailException;
import org.solen.business.exceptions.UserNotFoundByIdException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundByIdException.class)
    public ResponseEntity<String> handleUserNotByIdFound(UserNotFoundByIdException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundByEmailException.class)
    public ResponseEntity<String> handleUserNotByEmailFound(UserNotFoundByEmailException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailExists(EmailAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(StreakAlreadyUpdatedException.class)
    public ResponseEntity<String> handleStreakUpdated(StreakAlreadyUpdatedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PracticeAlreadyExistsException.class)
    public ResponseEntity<String> handlePracticeAlreadyExists(PracticeAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CheckInNotFoundException.class)
    public ResponseEntity<String> handleCheckInNotFound(CheckInNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundByIdException.class)
    public ResponseEntity<String> handleCategoryNotFoundById(CategoryNotFoundByIdException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PracticeNotFoundByIdException.class)
    public ResponseEntity<String> handlePracticeNotFoundById(PracticeNotFoundByIdException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return new ResponseEntity<>("Operation violates a database constraint.", HttpStatus.CONFLICT);
    }
}