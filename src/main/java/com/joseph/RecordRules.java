package com.joseph;

import com.joseph.exception.RecordValidationException;
import com.joseph.rule.Rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RecordRules is a utility class that provides a static method for validating records.
 */
public final class RecordRules {

    /**
     * RecordRules constructor.
     */
    private RecordRules() {
    }


    public static void check(final Rule<?, ?>... rules) {
        Map<String, List<String>> allErrors = new HashMap<>();
        for (Rule<?, ?> rule : rules) {
            if (!rule.getViolations().isEmpty()) {
                allErrors.put(rule.getFieldName(), rule.getViolations());
            }
        }
        if (!allErrors.isEmpty()) {
            throw new RecordValidationException(allErrors);
        }
    }
}