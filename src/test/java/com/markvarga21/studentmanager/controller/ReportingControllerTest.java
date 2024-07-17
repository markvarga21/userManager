package com.markvarga21.studentmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.markvarga21.studentmanager.dto.ReportMessage;
import com.markvarga21.studentmanager.entity.Report;
import com.markvarga21.studentmanager.service.auth.webtoken.JwtService;
import com.markvarga21.studentmanager.service.report.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportingController.class)
class ReportingControllerTest {
    /**
     * The {@code MockMvc} object used for testing the API.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * The {@code ReportService} for using the service methods.
     */
    @MockBean
    private ReportService reportService;

    /**
     * The {@code JwtService} for mocking the JWT service.
     */
    @MockBean
    private JwtService jwtService;

    /**
     * The {@code ObjectMapper} used for mapping students to JSON strings.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * The API URL for the report endpoints.
     */
    static final String API_URL = "/api/v1/report";

    /**
     * A sample report for testing purposes.
     */
    static final Report REPORT = new Report(
            1L,
            "issuer",
            "subject",
            "description",
            LocalDateTime.now()
    );

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldReturnAllReportsTest() throws Exception {
        // Given
        List<Report> expected = List.of(REPORT);

        // When
        when(this.reportService.getAllReports())
                .thenReturn(expected);

        // Then
        this.mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldDeleteReportTest() throws Exception {
        // Given
        Long id = REPORT.getId();
        String expected = String.format(
                "The report with the id '%d' was deleted.",
                id
        );

        // When
        // Then
        this.mockMvc.perform(delete("/api/v1/report/{id}", id).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(expected));
    }

    @WithMockUser(roles = "USER")
    @Test
    void shouldSendReportTest() throws Exception {
        // Given
        String expected = "Report sent successfully.";
        ReportMessage reportMessage = new ReportMessage(
                "issuer",
                "subject",
                "description"
        );

        // When
        when(this.reportService.sendReport(reportMessage))
                .thenReturn(expected);

        // Then
        this.mockMvc.perform(post("/api/v1/report")
                        .contentType("application/json")
                        .content(this.objectMapper.writeValueAsString(reportMessage))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(expected));
    }
}