package com.joseph.rule.child;

import com.joseph.rule.Rule;

/**
 * NumberRule is a rule that validates a number.
 */
public class NumberRule extends Rule<Integer, NumberRule> {
    /**
     * NumberRule constructor.
     * @param value Value to validate
     * @param name Field name
     */
    public NumberRule(Integer value, String name) {
        super(value, name);
    }

    /**
     * Validates that the number is at least the specified minimum value.
     * @param min Minimum value
     * @return NumberRule
     */
    public NumberRule min(int min) {
        if (value != null && value < min) violations.add("must be at least " + min);
        return this;
    }

    /**
     * Validates that the number is at most the specified maximum value.
     * @param max Maximum value
     * @return NumberRule
     */
    public NumberRule max(int max) {
        if (value != null && value > max) violations.add("must be at most " + max);
        return this;
    }
}
