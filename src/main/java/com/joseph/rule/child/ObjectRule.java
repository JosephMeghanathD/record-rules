package com.joseph.rule.child;

import com.joseph.rule.Rule;

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
    public ObjectRule(final T value, final String name) {
        super(value, name);
    }

    /**
     * Iterates over the list and validates each item.
     * @param itemValidator Validator for each item
     * @return the current rule
     */
    public ObjectRule<T> forEach(final java.util.function.BiConsumer<Object, Integer> itemValidator) {
        if (value instanceof Iterable<?> list) {
            int i = 0;
            for (Object item : list) {
                itemValidator.accept(item, i++);
            }
        }
        return this;
    }

    /**
     * Validates that the collection has at least the given size.
     * @param min Minimum size
     * @return the current rule
     */
    public ObjectRule<T> minSize(final int min) {
        addConstraint(val -> val instanceof java.util.Collection<?> col && col.size() < min, "must have at least " + min + " items");
        return this;
    }

    /**
     * Validates that the collection has at most the given size.
     * @param max Maximum size
     * @return the current rule
     */
    public ObjectRule<T> maxSize(final int max) {
        addConstraint(val -> val instanceof java.util.Collection<?> col && col.size() > max, "must have at most " + max + " items");
        return this;
    }

}