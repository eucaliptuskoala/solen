package org.solen.controller.mappers;

import lombok.AllArgsConstructor;
import org.solen.controller.dto.checkin.CheckInDto;
import org.solen.domain.checkin.CheckIn;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CheckInMapper {

    private PracticeMapper practiceMapper;

    public CheckInDto convertToDto(CheckIn checkIn) {
        if (checkIn == null) { return null; }
        return CheckInDto.builder()
                .id(checkIn.getId())
                .practice(practiceMapper.convertToDto(checkIn.getPractice()))
                .date(checkIn.getDate())
                .streakValue(checkIn.getStreakValue())
                .content(checkIn.getContent())
                .isPublic(checkIn.isPublic())
                .mood(checkIn.getMood())
                .createdAt(checkIn.getCreatedAt())
                .build();
    }
}
