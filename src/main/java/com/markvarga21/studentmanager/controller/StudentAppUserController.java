package com.markvarga21.studentmanager.controller;


import com.markvarga21.studentmanager.exception.util.ApiError;
import com.markvarga21.studentmanager.exception.util.AuthError;
import com.markvarga21.studentmanager.repository.StudentAppUserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller for the student application user.
 */
@RestController
@RequestMapping("/api/v1/studentUser")
@RequiredArgsConstructor
@CrossOrigin
@Tag(
    name = "Student user controller",
    description = "A controller which associates students with app users."
)
public class StudentAppUserController {
    /**
     * The repository for the student application user.
     */
    private final StudentAppUserRepository repository;

    /**
     * This method is used to get the student ID by the username.
     *
     * @param username The username of the student.
     * @return The found student ID.
     */
    @Operation(
        summary = "Retrieves the student id of a user identified by its username.",
        responses = {
            @ApiResponse(responseCode = "200", description = "The fetched student id.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{username}")
    public ResponseEntity<Long> getStudentIdByUsername(
            @PathVariable final String username
    ) {
        return ResponseEntity.ok(
                repository
                        .findByUsername(username)
                        .get()
                        .getStudentId()
        );
    }
}
