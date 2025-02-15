package com.markvarga21.studentmanager.controller;

import com.markvarga21.studentmanager.dto.ReportMessage;
import com.markvarga21.studentmanager.entity.Report;
import com.markvarga21.studentmanager.exception.util.ApiError;
import com.markvarga21.studentmanager.exception.util.AuthError;
import com.markvarga21.studentmanager.service.report.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller which is used to handle user reports/feedback.
 */
@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
@CrossOrigin
@Tag(
    name = "Reporting services",
    description = "The reporting related endpoints."
)
public class ReportingController {
    /**
     * The service which handles the reports.
     */
    private final ReportService reportService;

    /**
     * Retrieves all the reports.
     *
     * @param page The page number.
     * @param size The number of elements in a single page.
     * @return A page containing the reports.
     */
    @Operation(
        summary = "Retrieves all reports from the database.",
        responses = {
            @ApiResponse(responseCode = "200", description = "A page of reports.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<Report> getReports(
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(defaultValue = "10") final Integer size
    ) {
        return this.reportService.getAllReports(page, size);
    }

    /**
     * Deletes a report by its id.
     *
     * @param id The id of the report.
     * @return An informational message.
     */
    @Operation(
        summary = "Deletes a report with a given id.",
        responses = {
            @ApiResponse(responseCode = "200", description = "A status message.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteReport(@PathVariable final Long id) {
        this.reportService.deleteReport(id);
        String message = String.format(
                "The report with the id '%d' was deleted.",
                id
        );
        return ResponseEntity.ok(message);
    }

    /**
     * Submits a report.
     *
     * @param reportMessage The message of the report.
     * @return An informational message.
     */
    @Operation(
        summary = "Submits a report to the system.",
        responses = {
            @ApiResponse(responseCode = "200", description = "A status message.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> sendReport(
            @RequestBody @Validated final ReportMessage reportMessage
    ) throws MessagingException {
        String status = this.reportService
                .sendReport(reportMessage);
        return ResponseEntity.ok(status);
    }
}
