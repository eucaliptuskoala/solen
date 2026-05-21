package org.solen.business.exceptions;

public class PracticeNotFoundByIdException extends RuntimeException {

    public PracticeNotFoundByIdException(Long id) {
        super("Practice with id " + id + " does not exist");
    }

}