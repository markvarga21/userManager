package com.markvarga21.studentmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.Gender;
import com.markvarga21.studentmanager.service.StudentService;
import com.markvarga21.studentmanager.service.auth.webtoken.JwtService;
import com.markvarga21.studentmanager.service.file.FileUploadService;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import com.markvarga21.studentmanager.service.validation.face.FacialValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.markvarga21.studentmanager.data.TestingData.PAGE;
import static com.markvarga21.studentmanager.data.TestingData.SIZE;
import static com.markvarga21.studentmanager.data.TestingData.STUDENT_DTO;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {
    /**
     * The {@code MockMvc} object used for testing the API.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * The {@code StudentService} for using the service methods.
     */
    @MockBean
    private StudentService studentService;

    /**
     * The {@code FormRecognizerService} for form related usages.
     */
    @MockBean
    private FormRecognizerService formRecognizerService;

    /**
     * The {@code StudentRepository} for file manipulations.
     */
    @MockBean
    private FileUploadService fileUploadService;

    /**
     * The {@code FacialValidationService} for facial recognition.
     */
    @MockBean
    private FacialValidationService facialValidationService;

    /**
     * The URL used for testing the API.
     */
    static final String API_URL = "/api/v1/students";

    /**
     * The {@code ObjectMapper} used for mapping students to JSON strings.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * The {@code JwtService} for mocking the JWT service.
     */
    @MockBean
    private JwtService jwtService;

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldReturnAllStudentsTest() throws Exception {
        // Given
        List<StudentDto> studentDtoList = List.of(
                STUDENT_DTO,
                StudentDto.builder()
                        .id(2L)
                        .firstName("John").lastName("Wick")
                        .birthDate("2001-02-02")
                        .placeOfBirth("Washington")
                        .countryOfCitizenship("USA")
                        .gender(Gender.MALE)
                        .passportNumber("123455")
                        .passportDateOfIssue("2021-01-01")
                        .passportDateOfExpiry("2028-01-01")
                        .build()
        );
        Page<StudentDto> studentDtoPage = new PageImpl<>(studentDtoList);

        // When
        when(this.studentService.getAllStudents(PAGE, SIZE))
                .thenReturn(studentDtoPage);

        // Then
        this.mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*]", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].firstName").value("John"))
                .andExpect(jsonPath("$.content[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.content[0].birthDate").value("2001-02-02"))
                .andExpect(jsonPath("$.content[0].placeOfBirth").value("New York"))
                .andExpect(jsonPath("$.content[0].countryOfCitizenship").value("USA"))
                .andExpect(jsonPath("$.content[0].gender").value("MALE"))
                .andExpect(jsonPath("$.content[0].passportNumber").value("123456"))
                .andExpect(jsonPath("$.content[0].passportDateOfIssue").value("2020-01-01"))
                .andExpect(jsonPath("$.content[0].passportDateOfExpiry").value("2025-01-01"));
    }

    @WithMockUser(roles = "USER")
    @Test
    void shouldCreateStudentTest() throws Exception {
        // Given
        String username = "john.doe";
        String roles = "ROLE_USER";

        // When
        when(this.studentService.validPassportNumber(anyString()))
                .thenReturn(true);

        when(this.studentService.createStudent(STUDENT_DTO, username, roles))
                .thenReturn(STUDENT_DTO);

        // Then
        this.mockMvc.perform(post(API_URL)
                .contentType("application/json")
                    .queryParam("username", username)
                    .queryParam("roles", roles)
                    .content(this.objectMapper.writeValueAsString(STUDENT_DTO)).with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("2001-02-02"))
                .andExpect(jsonPath("$.placeOfBirth").value("New York"))
                .andExpect(jsonPath("$.countryOfCitizenship").value("USA"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.passportNumber").value("123456"))
                .andExpect(jsonPath("$.passportDateOfIssue").value("2020-01-01"))
                .andExpect(jsonPath("$.passportDateOfExpiry").value("2025-01-01"));
    }

    @WithMockUser(roles = "USER")
    @Test
    void shouldReturnStudentByIdTest() throws Exception {
        when(this.studentService.getStudentById(1L)).thenReturn(STUDENT_DTO);

        this.mockMvc.perform(get(API_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("2001-02-02"))
                .andExpect(jsonPath("$.placeOfBirth").value("New York"))
                .andExpect(jsonPath("$.countryOfCitizenship").value("USA"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.passportNumber").value("123456"))
                .andExpect(jsonPath("$.passportDateOfIssue").value("2020-01-01"))
                .andExpect(jsonPath("$.passportDateOfExpiry").value("2025-01-01"));
    }

    @WithMockUser(roles = "USER")
    @Test
    void shouldUpdateStudentByIdTest() throws Exception {
        when(this.studentService.modifyStudentById(STUDENT_DTO, 1L))
                .thenReturn(STUDENT_DTO);

        this.mockMvc.perform(put(API_URL + "/1")
                .contentType("application/json")
                .content(this.objectMapper.writeValueAsString(STUDENT_DTO)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("2001-02-02"))
                .andExpect(jsonPath("$.placeOfBirth").value("New York"))
                .andExpect(jsonPath("$.countryOfCitizenship").value("USA"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.passportNumber").value("123456"))
                .andExpect(jsonPath("$.passportDateOfIssue").value("2020-01-01"))
                .andExpect(jsonPath("$.passportDateOfExpiry").value("2025-01-01"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldDeleteStudentById() throws Exception {
        when(this.studentService.deleteStudentById(1L))
                .thenReturn(STUDENT_DTO);

        this.mockMvc.perform(delete(API_URL + "/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("2001-02-02"))
                .andExpect(jsonPath("$.placeOfBirth").value("New York"))
                .andExpect(jsonPath("$.countryOfCitizenship").value("USA"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.passportNumber").value("123456"))
                .andExpect(jsonPath("$.passportDateOfIssue").value("2020-01-01"))
                .andExpect(jsonPath("$.passportDateOfExpiry").value("2025-01-01"));
    }
}