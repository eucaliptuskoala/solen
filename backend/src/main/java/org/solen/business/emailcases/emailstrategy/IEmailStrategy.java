package org.solen.business.emailcases.emailstrategy;

import org.solen.domain.email.EmailType; 

public interface IEmailStrategy {
    void send(String to, String url);
    EmailType getType();
}
