package com.joseph;

import com.joseph.exception.RecordValidationException;
import com.joseph.rule.Rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RecordRules is a utility class that provides a static method for validating records.
 */
public class RecordRules {

    /**
     * RecordRules constructor.
     */
    public RecordRules() {
    }

    /**
     * Validates a list of rules and throws a RecordValidationException if any of the rules are violated.
     *
     * @param rules the rules to validate
     */
    public static void check(Rule<?, ?>... rules) {
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