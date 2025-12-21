package com.joseph.rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordRules {
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