package com.markvarga21.studentmanager.service.faceapi;

import com.markvarga21.studentmanager.dto.FaceApiResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * An interface for validating and comparing the faces on the
 * passport and the selfie itself.
 */
public interface FaceApiService {
    /**
     * Compares the faces found on the passport and the
     * selfie, and then sends it back to the client.
     *
     * @param passport The student's passport image.
     * @param selfiePhoto The selfie image of the student.
     * @return The validity and the percentage of the matching.
     */
    FaceApiResponse getValidityOfFaces(
            MultipartFile passport,
            MultipartFile selfiePhoto
    );

    /**
     * Returns the face ID for the given file.
     *
     * @param file The file to get the face ID for.
     * @return The face ID for the given file.
     */
    String getFaceIdForFile(MultipartFile file);

    /**
     * Validates the faces for the given passport number.
     *
     * @param studentId The id of the student.
     * @param passportNumber The passport number.
     * @return {@code true} if the faces are valid, {@code false} otherwise.
     */
    boolean validateFacesForPassportNumber(String passportNumber, Long studentId);

    /**
     * Deletes the face with the given passport number.
     *
     * @param passportNumber The passport number.
     */
    void deleteFace(String passportNumber);
}
