package org.solen.business.usercases;

import org.solen.business.exceptions.UserNotFoundByIdException;
import org.solen.business.repos.IUserRepository;
import org.solen.domain.users.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GetUserByIdUseCaseImplTest {

    @Mock
    private IUserRepository repository;

    @InjectMocks
    private GetUserByIdUseCaseImpl getUserByIdUseCase;

    @Test
    void getUserById_successfull(){

        User mockUser = User.builder()
                .id(1L)
                .name("test")
                .email("test@example.com")
                .password("test")
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(mockUser));

        User user = getUserByIdUseCase.getUserById(1L);

        verify(repository, times(1)).findById(1L);

        assertNotNull(user);
        assertEquals(1L, user.getId());
    }

    @Test
    void getUserById_failed(){
        UserNotFoundByIdException exception = assertThrows(UserNotFoundByIdException.class, () -> getUserByIdUseCase.getUserById(2L));
        assertEquals("User with id 2 does not exist", exception.getMessage());

    }
}