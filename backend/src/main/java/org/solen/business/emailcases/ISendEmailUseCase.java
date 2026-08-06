package org.solen.business.emailcases;

import org.solen.domain.email.EmailType;

public interface ISendEmailUseCase {
    public void execute(EmailType type, String email);
}
