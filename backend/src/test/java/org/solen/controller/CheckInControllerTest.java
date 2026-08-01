package org.solen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.solen.business.checkin.CheckInLikeEnricher;
import org.solen.business.checkin.*;
import org.solen.business.checkin.fypstrategy.IGetForYouCheckInsUseCase;
import org.solen.business.exceptions.CheckInNotFoundException;
import org.solen.business.exceptions.PracticeNotFoundByIdException;
import org.solen.business.usercases.UserDetailsService;
import org.solen.configuration.GlobalExceptionHandler;
import org.solen.configuration.security.JwtUtil;
import org.solen.configuration.security.UserInfoProvider;
import org.solen.controller.dto.checkin.CheckInDto;
import org.solen.controller.dto.checkin.CreateCheckInRequest;
import org.solen.controller.dto.checkin.UpdateCheckInRequest;
import org.solen.controller.mappers.CheckInMapper;
import org.solen.domain.checkin.CheckIn;
import org.solen.domain.checkin.Mood;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CheckInController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class CheckInControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ICreateCheckInUseCase createCheckInUseCase;

    @MockitoBean
    private IGetCheckInsForUserUseCase getCheckInsForUserUseCase;

    @MockitoBean
    private IUpdateCheckInUseCase updateCheckInUseCase;

    @MockitoBean
    private IDeleteCheckInUseCase deleteCheckInUseCase;

    @MockitoBean
    private IGetForYouCheckInsUseCase getForYouCheckInsUseCase;

    @MockitoBean
    private CheckInMapper mapper;

    @MockitoBean
    private UserInfoProvider userIdProvider;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private IToggleCheckInLikeUseCase toggleCheckInLikeUseCase;

    @MockitoBean
    private CheckInLikeEnricher checkInLikeEnricher;

    @Test
    @WithMockUser
    void getCheckIns_returnsList() throws Exception {
        when(userIdProvider.getUserId()).thenReturn(1L);
        CheckIn checkIn = CheckIn.builder().id(1L).date(LocalDate.now()).build();
        when(getCheckInsForUserUseCase.getCheckInsForUser(eq(1L), isNull(), isNull()))
                .thenReturn(List.of(checkIn));
        when(mapper.convertToDto(checkIn))
                .thenReturn(CheckInDto.builder().id(1L).build());

        mockMvc.perform(get("/checkins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser
    void getCheckIns_withDateRange_returnsList() throws Exception {
        when(userIdProvider.getUserId()).thenReturn(1L);
        CheckIn checkIn = CheckIn.builder().id(1L).date(LocalDate.of(2026, 5, 1)).build();
        when(getCheckInsForUserUseCase.getCheckInsForUser(1L, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 21)))
                .thenReturn(List.of(checkIn));
        when(mapper.convertToDto(checkIn))
                .thenReturn(CheckInDto.builder().id(1L).build());

        mockMvc.perform(get("/checkins")
                        .param("from", "2026-05-01")
                        .param("to", "2026-05-21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser
    void createCheckIn_returnsCreated() throws Exception {
        when(userIdProvider.getUserId()).thenReturn(1L);
        CreateCheckInRequest request = CreateCheckInRequest.builder()
                .practiceId(1L)
                .content("Great session")
                .isPublic(false)
                .mood(Mood.GOOD)
                .build();
        CheckIn checkIn = CheckIn.builder().id(1L).build();
        when(createCheckInUseCase.createWithDetails(1L, "Great session", false, Mood.GOOD, 1L))
                .thenReturn(checkIn);
        when(mapper.convertToDto(checkIn))
                .thenReturn(CheckInDto.builder().id(1L).content("Great session").build());

        mockMvc.perform(post("/checkins/checkin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void createCheckIn_practiceNotFound_returns404() throws Exception {
        when(userIdProvider.getUserId()).thenReturn(1L);
        CreateCheckInRequest request = CreateCheckInRequest.builder()
                .practiceId(99L).content("test").isPublic(false).mood(Mood.OKAY)
                .build();
        when(createCheckInUseCase.createWithDetails(99L, "test", false, Mood.OKAY, 1L))
                .thenThrow(new PracticeNotFoundByIdException(99L));

        mockMvc.perform(post("/checkins/checkin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void updateCheckIn_returnsUpdated() throws Exception {
        UpdateCheckInRequest request = UpdateCheckInRequest.builder()
                .content("Updated content")
                .isPublic(true)
                .mood(Mood.AWESOME)
                .build();
        CheckIn updated = CheckIn.builder().id(1L).content("Updated content").build();
        when(updateCheckInUseCase.update(1L, "Updated content", true, Mood.AWESOME))
                .thenReturn(updated);
        when(mapper.convertToDto(updated))
                .thenReturn(CheckInDto.builder().id(1L).content("Updated content").build());

        mockMvc.perform(put("/checkins/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void updateCheckIn_notFound_returns404() throws Exception {
        UpdateCheckInRequest request = UpdateCheckInRequest.builder()
                .content("x").isPublic(false).mood(Mood.OKAY)
                .build();
        when(updateCheckInUseCase.update(99L, "x", false, Mood.OKAY))
                .thenThrow(new CheckInNotFoundException(99L));

        mockMvc.perform(put("/checkins/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void deleteCheckIn_returns204() throws Exception {
        mockMvc.perform(delete("/checkins/1"))
                .andExpect(status().isNoContent());

        verify(deleteCheckInUseCase).delete(1L);
    }

    @Test
    @WithMockUser
    void deleteCheckIn_notFound_returns404() throws Exception {
        doThrow(new CheckInNotFoundException(99L))
                .when(deleteCheckInUseCase).delete(99L);

        mockMvc.perform(delete("/checkins/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getForYouCheckIns_returnsList() throws Exception {
        when(userIdProvider.getUserId()).thenReturn(1L);
        CheckIn checkIn = CheckIn.builder().id(1L).build();
        when(getForYouCheckInsUseCase.getForYouCheckIns(1L)).thenReturn(List.of(checkIn));
        when(mapper.convertToDto(checkIn))
                .thenReturn(CheckInDto.builder().id(1L).build());

        mockMvc.perform(get("/checkins/fyp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser
    void createCheckIn_missingPracticeId_returns400() throws Exception {
        String body = "{\"content\":\"test\",\"isPublic\":false,\"mood\":\"OKAY\"}";

        mockMvc.perform(post("/checkins/checkin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void toggleLike_returnsResult() throws Exception {
        when(userIdProvider.getUserId()).thenReturn(1L);
        when(toggleCheckInLikeUseCase.toggle(10L, 1L)).thenReturn(new ToggleLikeResult(true, 5));

        mockMvc.perform(post("/checkins/10/like"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.liked").value(true))
                .andExpect(jsonPath("$.likeCount").value(5));
    }

    @Test
    @WithMockUser
    void toggleLike_unlike_returnsResult() throws Exception {
        when(userIdProvider.getUserId()).thenReturn(1L);
        when(toggleCheckInLikeUseCase.toggle(10L, 1L)).thenReturn(new ToggleLikeResult(false, 0));

        mockMvc.perform(post("/checkins/10/like"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.liked").value(false))
                .andExpect(jsonPath("$.likeCount").value(0));
    }
}
