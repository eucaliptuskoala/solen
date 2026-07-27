package org.solen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.solen.business.checkin.IGetCheckInsForUserUseCase;
import org.solen.business.exceptions.PracticeNotFoundByIdException;
import org.solen.business.practicecases.*;
import org.solen.business.usercases.UserDetailsService;
import org.solen.configuration.GlobalExceptionHandler;
import org.solen.configuration.security.JwtUtil;
import org.solen.configuration.security.UserInfoProvider;
import org.solen.controller.dto.practice.CreatePracticeRequest;
import org.solen.controller.dto.practice.PracticeDto;
import org.solen.controller.mappers.PracticeMapper;
import org.solen.domain.practices.Practice;
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

@WebMvcTest(PracticeController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class PracticeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ICreatePracticeUseCase createPracticeUseCase;

    @MockitoBean
    private IGetCheckInsForUserUseCase getCheckInsForUserUseCase;

    @MockitoBean
    private IGetPracticesByUserUseCase getPracticesByUserUseCase;

    @MockitoBean
    private IDeletePracticeUseCase deletePracticeUseCase;

    @MockitoBean
    private IUpdateStreakUseCase updateStreakUseCase;

    @MockitoBean
    private PracticeMapper practiceMapper;

    @MockitoBean
    private UserInfoProvider userIdProvider;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser
    void createPractice_returnsCreated() throws Exception {
        CreatePracticeRequest request = CreatePracticeRequest.builder().name("Morning run").description("A morning run").build();
        Practice practice = Practice.builder().id(1L).name("Morning run").build();
        when(userIdProvider.getUserId()).thenReturn(1L);
        when(createPracticeUseCase.createPractice(request, 1L)).thenReturn(practice);
        when(practiceMapper.convertToDto(practice))
                .thenReturn(PracticeDto.builder().id(1L).name("Morning run").build());

        mockMvc.perform(post("/practices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Morning run"));
    }

    @Test
    @WithMockUser
    void getPracticesByUser_returnsList() throws Exception {
        when(userIdProvider.getUserId()).thenReturn(1L);
        Practice practice = Practice.builder().id(1L).name("Meditation").build();
        when(getPracticesByUserUseCase.getPracticesByUser(1L)).thenReturn(List.of(practice));
        when(getCheckInsForUserUseCase.findPracticeIdsCheckedInTodayByUserId(1L)).thenReturn(Set.of());
        when(practiceMapper.convertToDto(practice, Set.of()))
                .thenReturn(PracticeDto.builder().id(1L).name("Meditation").checkedInToday(false).build());

        mockMvc.perform(get("/practices/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Meditation"));
    }

    @Test
    @WithMockUser
    void deletePractice_returns204() throws Exception {
        mockMvc.perform(delete("/practices/1"))
                .andExpect(status().isNoContent());

        verify(deletePracticeUseCase).deletePractice(1L);
    }

    @Test
    @WithMockUser
    void deletePractice_notFound_returns404() throws Exception {
        doThrow(new PracticeNotFoundByIdException(99L))
                .when(deletePracticeUseCase).deletePractice(99L);

        mockMvc.perform(delete("/practices/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void updateStreak_returnsUpdated() throws Exception {
        Practice updated = Practice.builder().id(1L).name("Running").streak(5).build();
        when(updateStreakUseCase.updateStreak(1L)).thenReturn(updated);
        when(practiceMapper.convertToDto(updated))
                .thenReturn(PracticeDto.builder().id(1L).name("Running").streak(5).build());

        mockMvc.perform(put("/practices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.streak").value(5));
    }

    @Test
    @WithMockUser
    void updateStreak_notFound_returns404() throws Exception {
        when(updateStreakUseCase.updateStreak(99L))
                .thenThrow(new PracticeNotFoundByIdException(99L));

        mockMvc.perform(put("/practices/99"))
                .andExpect(status().isNotFound());
    }
}
