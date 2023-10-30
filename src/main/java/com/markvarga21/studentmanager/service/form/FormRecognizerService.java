package com.markvarga21.studentmanager.service.form;

import com.azure.ai.formrecognizer.documentanalysis.models.DocumentField;
import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.dto.PassportValidationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * An interface containing methods for the Form Recognizer service.
 */
public interface FormRecognizerService {
    /**
     * Extracts and returns the data from the passport.
     *
     * @param passport The photo of the passport.
     * @return The extracted {@code StudentDto} object.
     */
    StudentDto extractDataFromPassport(
            MultipartFile passport,
            MultipartFile selfie
    );

    /**
     * Extracts all the fields from the uploaded passport.
     *
     * @param passport The uploaded passport.
     * @return The extracted fields stored in a {@code Map}.
     */
    Map<String, DocumentField> getFieldsFromDocument(
            MultipartFile passport
    );

    /**
     * Validates the data entered by the user against the data
     * which can be found on the passport.
     *
     * @param passport The photo of the passport.
     * @param studentJson The student itself in a JSON string.
     * @return A {@code PassportValidationResponse} object.
     */
    PassportValidationResponse validatePassport(
            MultipartFile passport,
            String studentJson
    );

    /**
     * Checks if the student is present in the validation database.
     *
     * @param studentDto The student to be checked.
     * @return {@code true} if the student is present in the database,
     */
    boolean isStudentPresentInValidationDatabase(
            StudentDto studentDto
    );

    /**
     * Deletes the passport validation data by the passport number.
     *
     * @param passportNumber the passport number.
     */
    void deletePassportValidationByPassportNumber(
            String passportNumber
    );
}
