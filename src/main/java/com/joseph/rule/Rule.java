package com.joseph.rule;

import com.joseph.exception.RecordValidationException;
import com.joseph.rule.child.DateRule;
import com.joseph.rule.child.NumberRule;
import com.joseph.rule.child.ObjectRule;
import com.joseph.rule.child.StringRule;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Rule is a base class for all rules.
 * @param <T> Type of the value to validate
 * @param <R> Type of the rule
 */
public abstract class Rule<T, R extends Rule<T, R>> {
    /**
     * Value to validate
     */
    protected final T value;
    /**
     * Field name
     */
    protected final String fieldName;
    /**
     * List of violations
     */
    protected final List<String> violations = new ArrayList<>();

    /**
     * Rule constructor.
     * @param value Value to validate
     * @param fieldName Field name
     */
    protected Rule(final T value, final String fieldName) {
        this.value = value;
        this.fieldName = fieldName;
    }

    /**
     *  Creates a StringRule.
     * @param value Value to validate
     * @param name Field name
     * @return StringRule
     */
    public static StringRule on(final String value, final String name) {
        return new StringRule(value, name);
    }

    /**
     * Creates a NumberRule.
     * @param value Value to validate
     * @param name Field name
     * @return NumberRule
     */
    public static NumberRule on(final Integer value, final String name) {
        return new NumberRule(value, name);
    }

    /**
     * Creates a NumberRule.
     * @param value Value to validate.
     * @param name Field name
     * @return NumberRule
     */
    public static NumberRule on(final Long value, final String name) {
        return new NumberRule(value, name);
    }

    /**
     * Creates a NumberRule.
     * @param value Value to validate.
     * @param name Field name
     * @return NumberRule
     */
    public static NumberRule on(final Short value, final String name) {
        return new NumberRule(value, name);
    }

    /**
     * Creates a NumberRule.
     * @param value Value to validate.
     * @param name Field name
     * @return NumberRule
     */
    public static NumberRule on(final Byte value, final String name) {
        return new NumberRule(value, name);
    }

    /**
     * Creates a NumberRule.
     * @param value Value to validate.
     * @param name Field name
     * @return NumberRule
     */
    public static NumberRule on(final Double value, final String name) {
        return new NumberRule(value, name);
    }

    /**
     * Creates a NumberRule.
     * @param value Value to validate.
     * @param name Field name
     * @return NumberRule
     */
    public static NumberRule on(final Float value, final String name) {
        return new NumberRule(value, name);
    }

    /**
     * Creates a NumberRule.
     * @param value Value to validate.
     * @param name Field name
     * @return NumberRule
     */
    public static NumberRule on(final BigDecimal value, final String name) {
        return new NumberRule(value, name);
    }

    /**
     * Creates a NumberRule.
     * @param value Value to validate.
     * @param name Field name
     * @return NumberRule
     */
    public static NumberRule on(final BigInteger value, final String name) {
        return new NumberRule(value, name);
    }

    /**
     * Returns the current rule.
     * @param <T> Type of the value to validate
     * @param value Value to validate
     * @param name Field name
     * @return the current rule
     */
    public static <T> ObjectRule<T> on(final T value, final String name) {
        return new ObjectRule<>(value, name);
    }

    /**
     * Returns the current rule.
     * @param value Value to validate
     * @param name Field name
     * @return the current rule
     */
    public static DateRule on(final java.time.LocalDate value, final String name) {
        return new DateRule(value, name);
    }

    /**
     * Allows nesting validation logic. If the nested validator throws a
     * RecordValidationException, the errors are caught and flattened into
     * the parent's violation list.
     * @param nestedValidator Nested validator
     * @return the current rule
     */
    public R check(final Consumer<T> nestedValidator) {
        if (value != null) {
            try {
                nestedValidator.accept(value);
            } catch (RecordValidationException e) {
                e.getErrors().forEach((nestedField, nestedErrors) ->
                        violations.add(nestedField + " " + nestedErrors));
            }
        }
        return self();
    }

    /**
     * Returns the current rule.
     * @return the current rule
     */
    @SuppressWarnings("unchecked")
    protected R self() {
        return (R) this;
    }

    /**
     * Get the field name.
     * @return Field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Get the violations.
     * @return List of violations
     */
    public List<String> getViolations() {
        return violations;
    }

    /**
     * Validates that the value is not null.
     * @return the current rule
     */
    public R required() {
        if (value == null) violations.add("must not be null");
        return self();
    }

    /**
     * Validates that the value satisfies the given predicate.
     * @param predicate Predicate to validate
     * @return the current rule
     */
    public R satisfies(final Predicate<T> predicate) {
        return satisfies(predicate, "must satisfy predicate");
    }

    /**
     * Validates that the value satisfies the given predicate.
     * @param predicate Predicate to validate
     * @param message Message to display if the predicate is not satisfied
     * @return the current rule
     */
    public R satisfies(final Predicate<T> predicate, final String message) {
        if (value != null && !predicate.test(value)) violations.add(message);
        return self();
    }

    /**
     * Replaces the last violation with a custom message.
     * @param customMessage Custom message to display
     * @return the current rule
     */
    public R message(final String customMessage) {
        if (!violations.isEmpty()) {
            violations.remove(violations.size() - 1);
            violations.add(customMessage);
        }
        return self();
    }
}
