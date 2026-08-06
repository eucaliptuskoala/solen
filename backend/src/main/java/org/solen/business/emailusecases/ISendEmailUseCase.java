package org.solen.business.emailusecases;

import org.solen.domain.email.EmailType;

public interface ISendEmailUseCaseImpl {
    public void execute(EmailType type, String email);
}
