package org.solen.business.checkin;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.CheckInNotFoundException;
import org.solen.business.repos.ICheckInRepository;
import org.solen.domain.checkin.CheckIn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class DeleteCheckInUseCaseImpl implements IDeleteCheckInUseCase {

    private ICheckInRepository checkInRepository;

    @Override
    public void delete(Long id) {
        CheckIn existing = checkInRepository.findById(id);
        if (existing == null) {
            throw new CheckInNotFoundException(id);
        }
        checkInRepository.deleteById(id);
    }
}
