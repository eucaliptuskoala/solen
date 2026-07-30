package org.solen.business.checkin;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.CheckInNotFoundException;
import org.solen.business.repos.ICheckInRepository;
import org.solen.domain.checkin.CheckIn;
import org.solen.domain.checkin.Mood;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UpdateCheckInUseCaseImpl implements IUpdateCheckInUseCase {

    private ICheckInRepository checkInRepository;

    @Override
    public CheckIn update(Long id, String content, boolean isPublic, Mood mood) {
        CheckIn existing = checkInRepository.findById(id);
        if (existing == null) {
            throw new CheckInNotFoundException(id);
        }

        existing.setContent(content);
        existing.setPublic(isPublic);
        existing.setMood(mood);

        return checkInRepository.save(existing);
    }
}
