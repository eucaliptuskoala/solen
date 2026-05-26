package org.solen.business.checkin;

import org.solen.business.exceptions.CheckInNotFoundException;
import org.solen.business.exceptions.SelfLikeNotAllowedException;
import org.solen.business.repos.ICheckInLikeRepository;
import org.solen.business.repos.ICheckInRepository;
import org.solen.domain.checkin.CheckIn;
import org.solen.domain.checkin.CheckInLike;
import org.solen.domain.practices.Practice;
import org.solen.domain.users.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToggleCheckInLikeUseCaseImplTest {

    @Mock
    private ICheckInRepository checkInRepository;

    @Mock
    private ICheckInLikeRepository likeRepository;

    @InjectMocks
    private ToggleCheckInLikeUseCaseImpl toggleUseCase;

    private User makeUser(Long id) {
        return User.builder().id(id).build();
    }

    private Practice makePractice(Long id, User creator) {
        return Practice.builder().id(id).creator(creator).build();
    }

    private CheckIn makeCheckIn(Long id, Long practiceId, User creator) {
        return CheckIn.builder()
                .id(id)
                .practice(makePractice(practiceId, creator))
                .build();
    }

    @Test
    void toggle_like_success() {
        makeUser(1L);
        User other = makeUser(2L);
        CheckIn checkIn = makeCheckIn(10L, 100L, other);

        when(checkInRepository.findById(10L)).thenReturn(checkIn);
        when(likeRepository.findByCheckInIdAndUserId(10L, 1L)).thenReturn(Optional.empty());
        when(likeRepository.countByCheckInId(10L)).thenReturn(1);

        ToggleLikeResult result = toggleUseCase.toggle(10L, 1L);

        assertTrue(result.isLiked());
        assertEquals(1, result.getLikeCount());
        verify(likeRepository).save(any(CheckInLike.class));
        verify(likeRepository, never()).delete(any());
    }

    @Test
    void toggle_unlike_success() {
        makeUser(1L);
        User other = makeUser(2L);
        CheckIn checkIn = makeCheckIn(10L, 100L, other);
        CheckInLike existingLike = CheckInLike.builder().id(99L).checkInId(10L).userId(1L).build();

        when(checkInRepository.findById(10L)).thenReturn(checkIn);
        when(likeRepository.findByCheckInIdAndUserId(10L, 1L)).thenReturn(Optional.of(existingLike));
        when(likeRepository.countByCheckInId(10L)).thenReturn(0);

        ToggleLikeResult result = toggleUseCase.toggle(10L, 1L);

        assertFalse(result.isLiked());
        assertEquals(0, result.getLikeCount());
        verify(likeRepository).delete(existingLike);
        verify(likeRepository, never()).save(any());
    }

    @Test
    void toggle_checkInNotFound_throws() {
        when(checkInRepository.findById(99L)).thenReturn(null);

        assertThrows(CheckInNotFoundException.class, () -> toggleUseCase.toggle(99L, 1L));
        verify(checkInRepository).findById(99L);
        verifyNoInteractions(likeRepository);
    }

    @Test
    void toggle_selfLike_throws() {
        User user = makeUser(1L);
        CheckIn checkIn = makeCheckIn(10L, 100L, user);

        when(checkInRepository.findById(10L)).thenReturn(checkIn);

        assertThrows(SelfLikeNotAllowedException.class, () -> toggleUseCase.toggle(10L, 1L));
        verifyNoInteractions(likeRepository);
    }
}
