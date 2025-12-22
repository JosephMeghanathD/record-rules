package com.joseph.rule.child;

import com.joseph.rule.Rule;

import java.time.LocalDate;

/**
 * DateRule handles validation for dates using the base Rule functionality.
 */
public class DateRule extends Rule<LocalDate, DateRule> {
    /**
     * DateRule constructor.
     * @param value Value to validate
     * @param name Field name
     */
    public DateRule(LocalDate value, String name) {
        super(value, name);
    }

    /**
     * Validates that the date is in the future.
     * @return the current rule
     */
    public DateRule isFuture() {
        if (value != null && !value.isAfter(LocalDate.now())) violations.add("must be a future date");
        return this;
    }

    /**
     * Validates that the date is in the past.
     * @return the current rule
     */
    public DateRule isPast() {
        if (value != null && !value.isBefore(LocalDate.now())) violations.add("must be a past date");
        return this;
    }
}