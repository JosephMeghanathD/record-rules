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
    public StringRule(String value, String name) {
        super(value, name);
    }

    /**
     * Validates that the string is not blank.
     * @return StringRule
     */
    public StringRule notBlank() {
        if (value != null && value.isBlank()) violations.add("must not be blank");
        return this;
    }

    /**
     * Validates that the string is a valid email.
     * @return StringRule
     */
    public StringRule email() {
        if (value != null && !EMAIL_REGEX.matcher(value).matches()) violations.add("must be a valid email");
        return this;
    }

    /**
     * Validates that the string matches the specified regex.
     * @param regex Regex to match
     * @return StringRule
     */
    public StringRule matches(String regex) {
        if (value != null && !Pattern.matches(regex, value)) violations.add("must match pattern " + regex);
        return this;
    }

    /**
     * Validates that the string has a length between min and max.
     * @param min Minimum length
     * @param max Maximum length
     * @return StringRule
     */
    public StringRule length(int min, int max) {
        if (value != null && (value.length() < min || value.length() > max)) violations.add("must be between " + min + " and " + max + " characters");
        return this;
    }

    /**
     * Validates that the string has a minimum length.
     * @param min Minimum length
     * @return StringRule
     */
    public StringRule minLength(int min) {
        return length(min, Integer.MAX_VALUE);
    }

    /**
     * Validates that the string has a maximum length.
     * @param max Maximum length
     * @return StringRule
     */
    public StringRule maxLength(int max) {
        return length(0, max);
    }
}
