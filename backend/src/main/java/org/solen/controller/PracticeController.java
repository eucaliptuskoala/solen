package org.solen.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.solen.business.checkincases.IGetCheckInsForUserUseCase;
import org.solen.business.practicecases.ICreatePracticeUseCase;
import org.solen.business.practicecases.IDeletePracticeUseCase;
import org.solen.business.practicecases.IGetPracticesByUserUseCase;
import org.solen.business.practicecases.IUpdateStreakUseCase;
import org.solen.configuration.security.UserInfoProvider;
import org.solen.controller.dto.practice.CreatePracticeRequest;
import org.solen.controller.dto.practice.PracticeDto;
import org.solen.controller.mappers.PracticeMapper;
import org.solen.domain.practices.Practice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/practices")
@AllArgsConstructor
public class PracticeController {

    private ICreatePracticeUseCase createPracticeUseCase;
    private IDeletePracticeUseCase deletePracticeUseCase;
    private IGetPracticesByUserUseCase getPracticesByUserUseCase;
    private IUpdateStreakUseCase updateStreakUseCase;
    private IGetCheckInsForUserUseCase getCheckInsForUserUseCase;
    private PracticeMapper practiceMapper;
    private UserInfoProvider userIdProvider;

    @PostMapping
    public ResponseEntity<PracticeDto> createPractice(@Valid @RequestBody CreatePracticeRequest request){
        Long userId = userIdProvider.getUserId();
        Practice practice = createPracticeUseCase.createPractice(
                request.getCategoryId(), request.getName(), request.getDescription(), userId);
        return ResponseEntity.ok(practiceMapper.convertToDto(practice));
    }

    @GetMapping("/my")
    public ResponseEntity<List<PracticeDto>> getPracticesByUser(){
        Long userId = userIdProvider.getUserId();
        List<Practice> practices = getPracticesByUserUseCase.getPracticesByUser(userId);
        Set<Long> checkedInTodayIds = getCheckInsForUserUseCase.findPracticeIdsCheckedInTodayByUserId(userId);
        return ResponseEntity.ok(practices.stream()
                .map(h -> practiceMapper.convertToDto(h, checkedInTodayIds))
                .toList());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@practiceSecurity.isOwnerByEmail(#id, authentication.name)")
    public ResponseEntity<Void> deletePractice(@PathVariable Long id){
        deletePracticeUseCase.deletePractice(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("@practiceSecurity.isOwnerByEmail(#id, authentication.name)")
    public ResponseEntity<PracticeDto> updateStreak(@PathVariable Long id){
        Practice updatedPractice = updateStreakUseCase.updateStreak(id);
        return ResponseEntity.ok(practiceMapper.convertToDto(updatedPractice));
    }
}