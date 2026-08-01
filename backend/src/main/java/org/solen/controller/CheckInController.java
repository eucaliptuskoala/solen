package org.solen.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.solen.business.checkin.CheckInLikeEnricher;
import org.solen.business.checkin.ICreateCheckInUseCase;
import org.solen.business.checkin.IDeleteCheckInUseCase;
import org.solen.business.checkin.IGetCheckInsForUserUseCase;
import org.solen.business.checkin.IToggleCheckInLikeUseCase;
import org.solen.business.checkin.IUpdateCheckInUseCase;
import org.solen.business.checkin.ToggleLikeResult;
import org.solen.business.checkin.fypstrategy.IGetForYouCheckInsUseCase;
import org.solen.configuration.security.UserInfoProvider;
import org.solen.controller.dto.checkin.CheckInDto;
import org.solen.controller.dto.checkin.CreateCheckInRequest;
import org.solen.controller.dto.checkin.GetCheckInsDTO;
import org.solen.controller.dto.checkin.ToggleLikeResponse;
import org.solen.controller.dto.checkin.UpdateCheckInRequest;
import org.solen.controller.mappers.CheckInMapper;
import org.solen.domain.checkin.CheckIn;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/checkins")
@AllArgsConstructor
public class CheckInController {

    private ICreateCheckInUseCase createCheckInUseCase;
    private IGetCheckInsForUserUseCase getCheckInsForUserUseCase;
    private IUpdateCheckInUseCase updateCheckInUseCase;
    private IDeleteCheckInUseCase deleteCheckInUseCase;
    private IGetForYouCheckInsUseCase getForYouCheckInsUseCase;
    private IToggleCheckInLikeUseCase toggleCheckInLikeUseCase;
    private CheckInLikeEnricher checkInLikeEnricher;
    private CheckInMapper mapper;
    private UserInfoProvider userIdProvider;

    @GetMapping
    public ResponseEntity<List<CheckInDto>> getCheckIns(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        GetCheckInsDTO dto = new GetCheckInsDTO(from, to);
        List<CheckIn> checkIns = getCheckInsForUserUseCase.getCheckInsForUser(userIdProvider.getUserId(), dto.getFrom(), dto.getTo());
        return ResponseEntity.ok(checkIns.stream().map(mapper::convertToDto).toList());
    }

    @PostMapping("/checkin")
    public ResponseEntity<CheckInDto> createCheckIn(@Valid @RequestBody CreateCheckInRequest request) {
        CheckIn checkIn = createCheckInUseCase.createWithDetails(
                request.getPracticeId(), request.getContent(), request.isPublic(), request.getMood(), userIdProvider.getUserId());
        return ResponseEntity.ok(mapper.convertToDto(checkIn));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@checkInSecurity.isOwnerByEmail(#id, authentication.name)")
    public ResponseEntity<CheckInDto> updateCheckIn(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCheckInRequest request
    ) {
        CheckIn checkIn = updateCheckInUseCase.update(id, request.getContent(), request.isPublic(), request.getMood());
        return ResponseEntity.ok(mapper.convertToDto(checkIn));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@checkInSecurity.isOwnerByEmail(#id, authentication.name)")
    public ResponseEntity<Void> deleteCheckIn(@PathVariable Long id) {
        deleteCheckInUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/fyp")
    public ResponseEntity<List<CheckInDto>> getForYouCheckIns() {
        Long userId = userIdProvider.getUserId();
        List<CheckIn> checkIns = getForYouCheckInsUseCase.getForYouCheckIns(userId);
        List<CheckInDto> dtos = checkIns.stream().map(mapper::convertToDto).toList();
        checkInLikeEnricher.enrich(dtos, userId);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ToggleLikeResponse> toggleLike(@PathVariable Long id) {
        Long userId = userIdProvider.getUserId();
        ToggleLikeResult result = toggleCheckInLikeUseCase.toggle(id, userId);
        return ResponseEntity.ok(new ToggleLikeResponse(result.isLiked(), result.getLikeCount()));
    }
}
