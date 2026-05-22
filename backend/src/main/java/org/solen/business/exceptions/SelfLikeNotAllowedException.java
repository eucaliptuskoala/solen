package org.solen.business.exceptions;

public class SelfLikeNotAllowedException extends RuntimeException {
    public SelfLikeNotAllowedException() {
        super("You cannot like your own check-in");
    }
}
