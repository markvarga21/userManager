package com.markvarga21.studentmanager.entity;

import com.markvarga21.studentmanager.dto.StudentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * An entity class which is used to store the data extracted
 * from the passport.
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PassportValidationData {
    /**
     * The ID of the passport validation entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The time of the validation.
     */
    private LocalDateTime timestamp;

    /**
     * The first name of the student.
     */
    private String firstName;

    /**
     * The last name of the student.
     */
    private String lastName;

    /**
     * The birthdate of the student.
     */
    private LocalDate birthDate;

    /**
     * The birthplace of the student.
     */
    private String placeOfBirth;

    /**
     * The nationality of the student.
     */
    private String countryOfCitizenship;

    /**
     * The student's gender.
     */
    private Gender gender;

    /**
     * The student's passport number.
     */
    private String passportNumber;

    /**
     * The student's passports expiry date.
     */
    private LocalDate passportDateOfExpiry;

    /**
     * The student's passports issue date.
     */
    private LocalDate passportDateOfIssue;

    /**
     * The student's passport photo.
     */
    @Lob
    private byte[] passportPhoto;

    /**
     * The student's selfie photo.
     */
    @Lob
    private byte[] selfiePhoto;

    /**
     * Creates a {@code PassportValidationData} object from the
     * given {@code StudentDto} object and the photos.
     *
     * @param studentDto The student itself.
     * @param passportPhotoFromUser The photo of the passport.
     * @param selfiePhotoFromUser The selfie of the student.
     * @return A {@code PassportValidationData} object.
     */
    public static PassportValidationData createPassportValidationFromStudent(
            final StudentDto studentDto,
            final byte[] passportPhotoFromUser,
            final byte[] selfiePhotoFromUser
    ) {
        PassportValidationData passportValidationData =
                new PassportValidationData();
        passportValidationData
                .setId(studentDto.getId());
        passportValidationData
                .setFirstName(studentDto.getFirstName());
        passportValidationData
                .setLastName(studentDto.getLastName());
        passportValidationData
                .setBirthDate(studentDto.getBirthDate());
        passportValidationData
                .setPlaceOfBirth(studentDto.getPlaceOfBirth());
        passportValidationData
                .setCountryOfCitizenship(studentDto.getCountryOfCitizenship());
        passportValidationData
                .setPassportNumber(studentDto.getPassportNumber());
        passportValidationData
                .setPassportDateOfExpiry(studentDto.getPassportDateOfExpiry());
        passportValidationData
                .setPassportDateOfIssue(studentDto.getPassportDateOfIssue());

        passportValidationData.setTimestamp(LocalDateTime.now());

        passportValidationData.setPassportPhoto(passportPhotoFromUser);
        passportValidationData.setSelfiePhoto(selfiePhotoFromUser);

        return passportValidationData;
    }
}
