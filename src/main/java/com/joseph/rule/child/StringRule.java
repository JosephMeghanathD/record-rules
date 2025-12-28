package com.joseph.rule.child;

import com.joseph.rule.Rule;

import java.util.regex.Pattern;

/**
 * StringRule is a rule that validates a string.
 */
public class StringRule extends Rule<String, StringRule> {
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * StringRule constructor.
     * @param value Value to validate
     * @param name Field name
     */
    public StringRule(final String value, final String name) {
        super(value, name);
    }

    /**
     * Validates that the string is not blank.
     * @return StringRule
     */
    public StringRule notBlank() {
        if (value != null && value.isBlank()) addViolation("must not be blank");
        else clearLastViolationIndex();
        return this;
    }

    /**
     * Validates that the string is a valid email.
     * @return StringRule
     */
    public StringRule email() {
        if (value != null && !EMAIL_REGEX.matcher(value).matches()) addViolation("must be a valid email");
        else clearLastViolationIndex();
        return this;
    }

    /**
     * Validates that the string matches the specified regex.
     * @param regex Regex to match
     * @return StringRule
     */
    public StringRule matches(final String regex) {
        if (value != null && !Pattern.matches(regex, value)) addViolation("must match pattern " + regex);
        else clearLastViolationIndex();
        return this;
    }

    /**
     * Validates that the string has a length between min and max.
     * @param min Minimum length
     * @param max Maximum length
     * @return StringRule
     */
    public StringRule length(final int min, final int max) {
        if (value != null && (value.length() < min || value.length() > max)) addViolation("must be between " + min + " and " + max + " characters");
        else clearLastViolationIndex();
        return this;
    }

    /**
     * Validates that the string has a minimum length.
     * @param min Minimum length
     * @return StringRule
     */
    public StringRule minLength(final int min) {
        return length(min, Integer.MAX_VALUE);
    }

    /**
     * Validates that the string has a maximum length.
     * @param max Maximum length
     * @return StringRule
     */
    public StringRule maxLength(final int max) {
        return length(0, max);
    }
}
