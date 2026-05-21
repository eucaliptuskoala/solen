package org.solen.business.checkin.fypstrategy;

import org.solen.business.exceptions.UserNotFoundByIdException;
import org.solen.business.repos.IUserRepository;
import org.solen.domain.checkin.CheckIn;
import org.solen.domain.users.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetForYouCheckInsUseCaseImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private GetForYouCheckInsUseCaseImpl useCase;

    @Test
    void getForYouCheckIns_userExists_returnsCheckIns() {
        when(userRepository.findById(1L)).thenReturn(User.builder().id(1L).build());
        CheckIn ci = CheckIn.builder().id(1L).build();
        when(recommendationService.findPublicCheckIns(1L)).thenReturn(List.of(ci));

        List<CheckIn> result = useCase.getForYouCheckIns(1L);

        assertEquals(1, result.size());
        verify(userRepository).findById(1L);
        verify(recommendationService).findPublicCheckIns(1L);
    }

    @Test
    void getForYouCheckIns_userNotFound_throws() {
        when(userRepository.findById(99L)).thenReturn(null);

        assertThrows(UserNotFoundByIdException.class, () -> useCase.getForYouCheckIns(99L));
        verify(userRepository).findById(99L);
        verify(recommendationService, never()).findPublicCheckIns(anyLong());
    }
}
