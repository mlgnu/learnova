package org.mlgnu.learnova.common.exception;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        Map<String, String> errors,
        String path
) {

    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(Instant.now(), status, error, message, null, path);
    }

    public static ErrorResponse withErrors(int status, String error, Map<String, String> errors, String path) {
        return new ErrorResponse(Instant.now(), status, error, null, errors, path);
    }
}