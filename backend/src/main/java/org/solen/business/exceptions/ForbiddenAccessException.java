package org.solen.business.exceptions;

public class ForbiddenAccessException extends RuntimeException {
    public ForbiddenAccessException() {
        super("Access to this resource is forbidden");
    }
}
