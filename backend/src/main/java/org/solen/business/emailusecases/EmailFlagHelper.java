package org.solen.business.emailusecases;

import org.solen.domain.email.EmailType;
import org.springframework.stereotype.Component;

@Component
public class EmailFlagHelper {
  public String getFlag(EmailType type){  
    return switch (type) {
      case EMAIL_VERIFICATION -> "verify=";
      case PASSWORD_RESET -> "password=";
    };   
  }
}