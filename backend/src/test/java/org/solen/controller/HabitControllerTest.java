package org.solen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.solen.business.checkin.IGetCheckInsForUserUseCase;
import org.solen.business.exceptions.HabitNotFoundByIdException;
import org.solen.business.habitcases.*;
import org.solen.business.usercases.UserDetailsService;
import org.solen.configuration.GlobalExceptionHandler;
import org.solen.configuration.security.JwtUtil;
import org.solen.configuration.security.UserIdProvider;
import org.solen.controller.dto.habit.CreateHabitRequest;
import org.solen.controller.dto.habit.HabitDto;
import org.solen.controller.mappers.HabitMapper;
import org.solen.domain.habits.Habit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HabitController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class HabitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ICreateHabitUseCase createHabitUseCase;

    @MockitoBean
    private IGetCheckInsForUserUseCase getCheckInsForUserUseCase;

    @MockitoBean
    private IGetHabitsByUserUseCase getHabitsByUserUseCase;

    @MockitoBean
    private IDeleteHabitUseCase deleteHabitUseCase;

    @MockitoBean
    private IUpdateStreakUseCase updateStreakUseCase;

    @MockitoBean
    private HabitMapper habitMapper;

    @MockitoBean
    private UserIdProvider userIdProvider;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser
    void createHabit_returnsCreated() throws Exception {
        CreateHabitRequest request = CreateHabitRequest.builder().name("Morning run").build();
        Habit habit = Habit.builder().id(1L).name("Morning run").build();
        when(userIdProvider.getUserId()).thenReturn(1L);
        when(createHabitUseCase.createHabit(request, 1L)).thenReturn(habit);
        when(habitMapper.convertToDto(habit))
                .thenReturn(HabitDto.builder().id(1L).name("Morning run").build());

        mockMvc.perform(post("/habits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Morning run"));
    }

    @Test
    @WithMockUser
    void getHabitsByUser_returnsList() throws Exception {
        when(userIdProvider.getUserId()).thenReturn(1L);
        Habit habit = Habit.builder().id(1L).name("Meditation").build();
        when(getHabitsByUserUseCase.getHabitsByUser(1L)).thenReturn(List.of(habit));
        when(getCheckInsForUserUseCase.findHabitIdsCheckedInTodayByUserId(1L)).thenReturn(Set.of());
        when(habitMapper.convertToDto(habit, Set.of()))
                .thenReturn(HabitDto.builder().id(1L).name("Meditation").checkedInToday(false).build());

        mockMvc.perform(get("/habits/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Meditation"));
    }

    @Test
    @WithMockUser
    void deleteHabit_returns204() throws Exception {
        mockMvc.perform(delete("/habits/1"))
                .andExpect(status().isNoContent());

        verify(deleteHabitUseCase).deleteHabit(1L);
    }

    @Test
    @WithMockUser
    void deleteHabit_notFound_returns404() throws Exception {
        doThrow(new HabitNotFoundByIdException(99L))
                .when(deleteHabitUseCase).deleteHabit(99L);

        mockMvc.perform(delete("/habits/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void updateStreak_returnsUpdated() throws Exception {
        Habit updated = Habit.builder().id(1L).name("Running").streak(5).build();
        when(updateStreakUseCase.updateStreak(1L)).thenReturn(updated);
        when(habitMapper.convertToDto(updated))
                .thenReturn(HabitDto.builder().id(1L).name("Running").streak(5).build());

        mockMvc.perform(put("/habits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.streak").value(5));
    }

    @Test
    @WithMockUser
    void updateStreak_notFound_returns404() throws Exception {
        when(updateStreakUseCase.updateStreak(99L))
                .thenThrow(new HabitNotFoundByIdException(99L));

        mockMvc.perform(put("/habits/99"))
                .andExpect(status().isNotFound());
    }
}
