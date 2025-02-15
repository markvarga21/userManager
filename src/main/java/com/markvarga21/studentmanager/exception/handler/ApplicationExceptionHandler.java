package com.markvarga21.studentmanager.exception.handler;

import com.markvarga21.studentmanager.exception.InvalidDateException;
import com.markvarga21.studentmanager.exception.InvalidDateFormatException;
import com.markvarga21.studentmanager.exception.InvalidFacesException;
import com.markvarga21.studentmanager.exception.InvalidPassportException;
import com.markvarga21.studentmanager.exception.InvalidStudentException;
import com.markvarga21.studentmanager.exception.InvalidUserCredentialsException;
import com.markvarga21.studentmanager.exception.OperationType;
import com.markvarga21.studentmanager.exception.PassportValidationDataNotFoundException;
import com.markvarga21.studentmanager.exception.ReportNotFoundException;
import com.markvarga21.studentmanager.exception.StudentNotFoundException;
import com.markvarga21.studentmanager.exception.TokenNotFoundException;
import com.markvarga21.studentmanager.exception.UserNotFoundException;
import com.markvarga21.studentmanager.exception.util.ApiError;
import com.markvarga21.studentmanager.exception.util.InvalidFacesApiError;
import com.markvarga21.studentmanager.util.Generated;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A custom exception handler for dealing with
 * certain exceptions in the applications.
 */
@ControllerAdvice
@Slf4j
@Generated
public class ApplicationExceptionHandler {
    /**
     * A bullet point character in unicode format.
     */
    public static final String POINT_UNICODE = "\u2022";

    /**
     * Handles if the user did not input the information correctly when using
     * the application's endpoints.
     *
     * @param ex The exception is caused by the invalid field value.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationExceptions(
            final MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error(String.format("Invalid field(s): %s", errors));
        String message = String.format(
                "The data you've entered is not valid!%nCauses:%n%s",
                this.formatInvalidFieldsMap(errors)
        );
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.BAD_REQUEST,
                message,
                OperationType.CREATE,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Formats the invalid fields map into a more readable format.
     *
     * @param map The map containing the invalid fields.
     * @return The formatted {@code String}.
     */
    private String formatInvalidFieldsMap(final Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 0;
        for (var entry : map.entrySet()) {
            String value = POINT_UNICODE + " " + getErrorMessageForFieldViolation(entry.getKey());
            stringBuilder.append(value);
            if (counter < map.size() - 1) {
                stringBuilder.append("\n");
            }
            counter++;
        }
        return stringBuilder.toString();
    }

    /**
     * Convert a field violation into a more readable format.
     *
     * @param violation The field violation.
     * @return The readable {@code String} representation of the violation.
     */
    private String getErrorMessageForFieldViolation(
            final String violation
    ) {
        return switch (violation) {
            case "firstName" -> "First name cannot be empty!";
            case "lastName" -> "Last name cannot be empty!";
            case "birthDate" -> "Birth date cannot be empty!";
            case "placeOfBirth" -> "Birthplace cannot be empty!";
            case "countryOfCitizenship" -> "Country of citizenship cannot be empty!";
            case "gender" -> "Gender cannot be empty!";
            case "passportNumber" -> "Passport number cannot be empty!";
            case "passportDateOfExpiry" -> "Passport date of expiry cannot be empty!";
            case "passportDateOfIssue" -> "Passport date of issue cannot be empty!";
            default -> "";
        };
    }

    /**
     * Handles if the input fields are invalid.
     *
     * @param ex The exception is caused by a constraint violation.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolationException(
            final ConstraintViolationException ex
    ) {
        String message = String.format(
                "Invalid field in creating the student! Violations: %s",
                ex.getMessage()
        );
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.BAD_REQUEST,
                message,
                OperationType.CREATE,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Handles if the passport validation data cannot be found.
     *
     * @param ex The exception caused by not founding the passport validation data.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(PassportValidationDataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handlePassportValidationDataNotFoundException(
            final PassportValidationDataNotFoundException ex
    ) {
        String message = "Passport validation data not found!";
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.NOT_FOUND,
                message,
                OperationType.CREATE,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Handles if the user cannot be found in the database.
     *
     * @param ex The exception is caused by not founding the user.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleUserNotFoundException(
            final UserNotFoundException ex
    ) {
        log.error("User not found!");
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.NOT_FOUND,
                "User not found!",
                OperationType.READ,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Handles if the user's credentials are invalid.
     *
     * @param ex The exception is caused by the invalid user credentials.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(InvalidUserCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleInvalidUserCredentialsException(
            final InvalidUserCredentialsException ex
    ) {
        log.error("Invalid user credentials!");
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.UNAUTHORIZED,
                "Invalid user credentials!",
                OperationType.READ,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Handles the exception if a student cannot be found in the database.
     *
     * @param ex The exception is caused by not founding the student.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleStudentNotFoundException(
            final StudentNotFoundException ex
    ) {
        log.error("Student not found!");
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.NOT_FOUND,
                "Student not found!",
                ex.getType(),
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Handles the exception if a report could not be found in the database.
     *
     * @param ex The exception is caused by not founding the report.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(ReportNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleReportNotFoundException(
            final StudentNotFoundException ex
    ) {
        log.error("Report not found!");
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.NOT_FOUND,
                "Report not found!",
                ex.getType(),
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Handles if the format of the student's birthdate
     * is invalid or not yet supported.
     *
     * @param ex The exception caused by incorrectly formatting the birthdate.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler({
            DateTimeParseException.class,
            InvalidDateFormatException.class
    })
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<Object> handleInvalidDateFormatException(
            final DateTimeParseException ex
    ) {
        log.error("Invalid date format!");
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.NOT_ACCEPTABLE,
                "Invalid date format! Allowed format is: YYYY-MM-DD",
                OperationType.CREATE,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Handles if the date is invalid.
     *
     * @param ex The exception is caused by the invalid date.
     * @return A readable {@code ResponseEntity} containing useful information about the error.
     */
    @ExceptionHandler(InvalidDateException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<Object> handleInvalidDateException(
            final InvalidDateException ex
    ) {
        log.error("Invalid date!");
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.NOT_ACCEPTABLE,
                ex.getMessage(),
                OperationType.CREATE,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Handles the exception if the inputted gender is invalid.
     *
     * @param ex The exception is caused by the invalid gender.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidGenderException(
            final HttpMessageNotReadableException ex
    ) {
        String message = "Invalid gender! Allowed genders are: MALE or FEMALE";
        log.error(message);
        String stackTraceAsString = getStackTraceAsString(ex);
        if (stackTraceAsString.contains("LocalDate")) {
            String invalidDateMessage = "Invalid date format! Allowed format is: YYYY-MM-DD";
            ApiError apiError = new ApiError(
                    new Date(),
                    HttpStatus.BAD_REQUEST,
                    invalidDateMessage,
                    OperationType.CREATE,
                    stackTraceAsString
            );
            return new ResponseEntity<>(
                    apiError,
                    new HttpHeaders(),
                    apiError.getStatus()
            );
        } else {
            ApiError apiError = new ApiError(
                    new Date(),
                    HttpStatus.BAD_REQUEST,
                    message,
                    OperationType.CREATE,
                    stackTraceAsString
            );
            return new ResponseEntity<>(
                    apiError,
                    new HttpHeaders(),
                    apiError.getStatus()
            );
        }

    }

    /**
     * Handles the exception if the passport is invalid.
     *
     * @param ex The exception is caused by the invalid passport.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(InvalidPassportException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<Object> handleInvalidPassportException(
            final InvalidPassportException ex
    ) {
        log.error(ex.getMessage());
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.NOT_ACCEPTABLE,
                ex.getMessage(),
                OperationType.CREATE,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Handles the exception if the faces are not identical.
     *
     * @param ex The exception is caused by the faces not being identical.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(InvalidFacesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidFacesException(
            final InvalidFacesException ex
    ) {
        log.error(ex.getMessage());
        InvalidFacesApiError invalidFacesApiError = new InvalidFacesApiError(
                ex.getMessage(),
                ex.getPercentage()
        );
        return new ResponseEntity<>(
                invalidFacesApiError,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );

    }

    /**
     * Handles the exception if the student's data are invalid.
     *
     * @param ex The exception is caused by an invalid field.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(InvalidStudentException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<Object> handleInvalidStudentException(
            final InvalidStudentException ex
    ) {
        log.error(ex.getMessage());
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.NOT_ACCEPTABLE,
                ex.getMessage(),
                OperationType.CREATE,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Handles the exception if the faces are not identical.
     *
     * @param ex The exception is caused by the faces not being identical.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(TokenNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleTokenNotFoundException(
            final TokenNotFoundException ex
    ) {
        log.error("Token not found!");
        ApiError apiError = new ApiError(
                new Date(),
                HttpStatus.NOT_FOUND,
                "Token not found!",
                OperationType.READ,
                getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                apiError.getStatus()
        );
    }

    /**
     * Handles the exception if the JWT token has expired.
     *
     * @param ex The exception is caused by the expired JWT token.
     * @return A readable {@code ResponseEntity} containing useful information.
     */
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleTokenExpiredException(
        final ExpiredJwtException ex
    ) {
        log.error("Token has expired!");
        ApiError apiError = new ApiError(
            new Date(),
            HttpStatus.UNAUTHORIZED,
            "Token has expired!",
            OperationType.READ,
            getStackTraceAsString(ex)
        );
        return new ResponseEntity<>(
            apiError,
            new HttpHeaders(),
            apiError.getStatus()
        );
    }

    /**
     * Extracts the exception's stacktrace into a
     * more readable {@code String} format.
     *
     * @param throwable The throwable object.
     * @return The chained {@code String} representation of the stacktrace.
     */
    private String getStackTraceAsString(final Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
