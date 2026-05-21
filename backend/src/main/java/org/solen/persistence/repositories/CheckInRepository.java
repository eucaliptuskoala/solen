package org.solen.persistence.repositories;

import lombok.AllArgsConstructor;
import org.solen.business.repos.ICheckInRepository;
import org.solen.domain.checkin.CheckIn;
import org.solen.persistence.converters.CheckInConverter;
import org.solen.persistence.entities.CheckInEntity;
import org.solen.persistence.jparepos.CheckInJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class CheckInRepository implements ICheckInRepository {

    private CheckInConverter converter;
    private CheckInJpaRepository jpaRepository;

    @Override
    public CheckIn findById(Long id) {
        CheckInEntity entity = jpaRepository.findByIdWithPracticeAndCreator(id);
        return entity != null ? converter.convertToDomain(entity) : null;
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean findByCheckInIdAndEmail(Long checkInId, String email) {
        return jpaRepository.findByCheckInIdAndEmail(checkInId, email);
    }

    @Override
    public CheckIn save(CheckIn checkIn) {
        CheckInEntity entity = jpaRepository.save(converter.convertToEntity(checkIn));
        return converter.convertToDomain(entity);
    }

    @Override
    public List<CheckIn> findByPracticeCreatorId(Long userId){
        return jpaRepository.findByPracticeCreatorId(userId).stream().map(converter::convertToDomain).toList();
    }

    @Override
    public List<CheckIn> findCheckInsForUser(Long userId, LocalDate from, LocalDate to) {
        return jpaRepository.findCheckInsForUser(userId, from, to).stream().map(converter::convertToDomain).toList();
    }

    @Override
    public List<CheckIn> findPublicCheckIns() {
        return jpaRepository.findPublicCheckIns().stream().map(converter::convertToDomain).toList();
    }

    @Override
    public Set<Long> findPracticeIdsCheckedInTodayByUserId(Long userId) {
        return jpaRepository.findPracticeIdsCheckedInOnDate(userId, LocalDate.now()).stream().collect(Collectors.toSet());
    }
}
