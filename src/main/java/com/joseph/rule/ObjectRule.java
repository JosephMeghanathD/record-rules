package com.joseph.rule;

/**
 * ObjectRule handles validation for complex types using the base Rule functionality.
 * @param <T> Type of the value to validate
 */
public class ObjectRule<T> extends Rule<T, ObjectRule<T>> {
    /**
     * ObjectRule constructor.
     * @param value Value to validate
     * @param name Field name
     */
    public ObjectRule(T value, String name) {
        super(value, name);
    }
}