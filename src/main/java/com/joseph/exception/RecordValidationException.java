package com.joseph.exception;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RecordValidationException is an exception that is thrown when a record fails validation.
 */
public class RecordValidationException extends RuntimeException {
    /**
     * Map of field names to list of error messages
     */
    private final Map<String, List<String>> errors;

    /**
     * RecordValidationException constructor.
     * @param errors Map of field names to list of error messages
     */
    public RecordValidationException(Map<String, List<String>> errors) {
        super("Validation failed: " + formatErrors(errors));
        this.errors = errors;
    }

    /**
     * Formats the errors into a string.
     * @param errors Map of field names to list of error messages
     * @return String
     */
    private static String formatErrors(Map<String, List<String>> errors) {
        return errors.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.joining(", ", "[", "]"));
    }

    /**
     * Gets the errors.
     * @return Map of field names to list of error messages
     */
    public Map<String, List<String>> getErrors() {
        return errors;
    }
}