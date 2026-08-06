package org.solen.business.emailusecases.emailstrategy;

import org.solen.domain.email.EmailType; 

public interface IEmailStrategy {
    void send(String to, String url);
    EmailType getType();
}
