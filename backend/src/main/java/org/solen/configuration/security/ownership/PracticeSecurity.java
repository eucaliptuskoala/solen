package org.solen.configuration.security.ownership;

import lombok.AllArgsConstructor;
import org.solen.business.repos.IPracticeRepository;
import org.springframework.stereotype.Component;

@Component("practiceSecurity")
@AllArgsConstructor
public class PracticeSecurity {

    private IPracticeRepository practiceRepository;

    public boolean isOwnerByEmail(Long practiceId, String email) {
        return practiceRepository.existsByPracticeIdAndCreatorEmail(practiceId, email);
    }
}
