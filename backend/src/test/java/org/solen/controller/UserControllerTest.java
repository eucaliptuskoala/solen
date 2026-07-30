package org.solen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.solen.business.exceptions.UserNotFoundByIdException;
import org.solen.business.usercases.*;
import org.solen.configuration.AdminBootstrapRunner;
import org.solen.configuration.GlobalExceptionHandler;
import org.solen.configuration.security.JwtUtil;
import org.solen.configuration.security.UserInfoProvider;
import org.solen.controller.dto.user.CreateUserRequest;
import org.solen.controller.dto.user.UpdateUserRequest;
import org.solen.controller.dto.user.UserDto;
import org.solen.controller.mappers.UserMapper;
import org.solen.domain.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private DeleteUserUseCase deleteUserUseCase;

    @MockitoBean
    private GetUsersUseCase getUsersUseCase;

    @MockitoBean
    private GetUserByIdUseCase getUserByIdUseCase;

    @MockitoBean
    private UpdateUserUseCase updateUserUseCase;

    @MockitoBean
    private IPromoteToAdminUseCase promoteToAdminUseCase;

    @MockitoBean
    private UserMapper mapper;

    @MockitoBean
    private UserInfoProvider userIdProvider;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private AdminBootstrapRunner adminBootstrapRunner;

    @Test
    void createUser_returnsCreated() throws Exception {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("Alice")
                .email("alice@test.com")
                .password("secret")
                .build();
        User user = User.builder().id(1L).name("Alice").email("alice@test.com").build();
        when(createUserUseCase.createUser(any(CreateUserRequest.class))).thenReturn(user);
        when(mapper.convertToDto(user))
                .thenReturn(UserDto.builder().id(1L).name("Alice").email("alice@test.com").build());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void deleteUser_returns204() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(deleteUserUseCase).deleteUser(1L);
    }

    @Test
    void deleteUser_notFound_returns404() throws Exception {
        doThrow(new UserNotFoundByIdException(99L))
                .when(deleteUserUseCase).deleteUser(99L);

        mockMvc.perform(delete("/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUsers_returnsList() throws Exception {
        User user = User.builder().id(1L).name("Alice").email("alice@test.com").build();
        when(getUsersUseCase.getUsers()).thenReturn(List.of(user));
        when(mapper.convertToDto(user))
                .thenReturn(UserDto.builder().id(1L).name("Alice").email("alice@test.com").build());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    @Test
    void getUserById_returnsUser() throws Exception {
        User user = User.builder().id(1L).name("Bob").email("bob@test.com").build();
        when(getUserByIdUseCase.getUserById(1L)).thenReturn(user);
        when(mapper.convertToDto(user))
                .thenReturn(UserDto.builder().id(1L).name("Bob").email("bob@test.com").build());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"));
    }

    @Test
    void getUserById_notFound_returns404() throws Exception {
        when(getUserByIdUseCase.getUserById(99L))
                .thenThrow(new UserNotFoundByIdException(99L));

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_returnsUpdated() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .name("Alice Updated")
                .email("alice@test.com")
                .password("newpass")
                .build();
        User user = User.builder().id(1L).name("Alice Updated").email("alice@test.com").build();
        when(updateUserUseCase.updateUser(any(UpdateUserRequest.class), eq(1L))).thenReturn(user);
        when(mapper.convertToDto(user))
                .thenReturn(UserDto.builder().id(1L).name("Alice Updated").build());

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Updated"));
    }

    @Test
    void updateUser_notFound_returns404() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .name("Ghost")
                .email("ghost@test.com")
                .password("p")
                .build();
        when(updateUserUseCase.updateUser(any(UpdateUserRequest.class), eq(99L)))
                .thenThrow(new UserNotFoundByIdException(99L));

        mockMvc.perform(put("/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_withMissingFields_returns400() throws Exception {
        String body = "{\"name\":\"\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void promoteToAdmin_returnsUpdated() throws Exception {
        User user = User.builder().id(1L).name("Alice").email("alice@test.com").isAdmin(true).build();
        when(promoteToAdminUseCase.promote(1L)).thenReturn(user);
        when(mapper.convertToDto(user))
                .thenReturn(UserDto.builder().id(1L).name("Alice").email("alice@test.com").build());

        mockMvc.perform(patch("/users/1/role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void promoteToAdmin_notFound_returns404() throws Exception {
        doThrow(new UserNotFoundByIdException(99L))
                .when(promoteToAdminUseCase).promote(99L);

        mockMvc.perform(patch("/users/99/role"))
                .andExpect(status().isNotFound());
    }
}
