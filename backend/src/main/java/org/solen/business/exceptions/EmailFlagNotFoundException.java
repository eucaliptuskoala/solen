package org.solen.business.exceptions;

public class EmailFlagNotFoundException extends RuntimeException{
    public EmailFlagNotFoundException(String message){
        super("Email Flag not found");
    }
    
}