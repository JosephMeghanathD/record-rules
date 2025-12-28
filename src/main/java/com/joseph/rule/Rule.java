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
import java.util.Objects;
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
    private final List<String> violations = new ArrayList<>();

    /**
     * Internal representation of a validation rule.
     * Encapsulates the logic (predicate) and the resulting error message.
     * @param <T> Type of the value to validate
     */
    private static class Constraint<T> {
        private final Predicate<T> predicate;
        private String message;

        Constraint(final Predicate<T> predicate, final String message) {
            this.predicate = predicate;
            this.message = message;
        }
    }

    /**
     * Collection of constraints to be evaluated lazily.
     */
    private final List<Constraint<T>> constraints = new ArrayList<>();

    /**
     * Internal helper to register a new validation requirement.
     * @param predicate The condition to test (returns true if invalid)
     * @param message The error message if the predicate is true
     */
    protected void addConstraint(final Predicate<T> predicate, final String message) {
        constraints.add(new Constraint<>(predicate, message));
    }

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
     * Triggers the evaluation of all registered constraints against the current value.
     * This method implements lazy evaluation, ensuring rules are only tested when
     * the results are explicitly requested.
     * @return A list of all validation violation messages
     */
    public List<String> getViolations() {
        if (violations.isEmpty()) {
            violations.addAll(
                    constraints.stream()
                        .filter(constraint -> constraint.predicate.test(value))
                        .map(constraint -> constraint.message)
                        .toList()
            );
        }
        return violations;
    }

    /**
     * Validates that the value is not null.
     * @return the current rule
     */
    public R required() {
        addConstraint(Objects::isNull, "must not be null");
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
        addConstraint(val -> val != null && !predicate.test(val), message);
        return self();
    }

    /**
     * Modifies the error message of the most recently added constraint.
     * This allows for descriptive custom messages in fluent chains.
     * @param customMessage The new error message to assign to the last rule
     * @return the current rule for chaining
     */
    public R message(final String customMessage) {
        if (!constraints.isEmpty()) {
            constraints.get(constraints.size() - 1).message = customMessage;
        }
        return self();
    }
}
