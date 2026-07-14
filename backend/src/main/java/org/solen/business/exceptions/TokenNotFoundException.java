package org.solen.business.exceptions;

public class TokenNotFoundException extends RuntimeException{
    public TokenNotFoundException(String token){
        super("Token not found");
    }
}