package org.solen.business.exceptions;

public class PracticeAlreadyExistsException extends RuntimeException {
    public PracticeAlreadyExistsException() {
        super("Practice Already Exists!");
    }
}
