package com.joseph.rule;

import com.joseph.RecordRules;
import com.joseph.rule.child.DateRule;
import com.joseph.rule.child.NumberRule;
import com.joseph.rule.child.ObjectRule;
import com.joseph.rule.child.StringRule;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RuleTest {

    @Test
    void testStaticFactoryMethods() {
        // Tests on(String, String)
        assertInstanceOf(StringRule.class, Rule.on("test", "field"));

        // Tests on(Integer, String)
        assertInstanceOf(NumberRule.class, Rule.on(10, "field"));

        // Tests on(Long, String)
        assertInstanceOf(NumberRule.class, Rule.on(10L, "field"));

        // Tests on(T, String)
        assertInstanceOf(ObjectRule.class, Rule.on(new Object(), "field"));

        // Tests on(LocalDate, String)
        assertInstanceOf(DateRule.class, Rule.on(LocalDate.now(), "field"));
    }

    @Test
    void testMetadataGetters() {
        Rule<String, StringRule> rule = Rule.on("value", "username");
        assertEquals("username", rule.getFieldName());
        assertTrue(rule.getViolations().isEmpty());
    }

    @Test
    void testRequiredBranching() {
        // Branch: value is null
        var rule1 = Rule.on((String) null, "field").required();
        assertEquals(1, rule1.getViolations().size());
        assertEquals("must not be null", rule1.getViolations().get(0));

        // Branch: value is NOT null
        var rule2 = Rule.on("exists", "field").required();
        assertTrue(rule2.getViolations().isEmpty());
    }

    @Test
    void testSatisfiesOverloadsAndBranching() {
        // Test single arg overload: satisfies(Predicate)
        var rule1 = Rule.on("short", "field").satisfies(s -> s.length() > 10);
        assertEquals("must satisfy predicate", rule1.getViolations().get(0));

        // Test double arg: satisfies(Predicate, String)
        // Branch: Value is null (should not run predicate)
        var ruleNull = Rule.on((String) null, "field").satisfies(s -> !s.isEmpty(), "error");
        assertTrue(ruleNull.getViolations().isEmpty());

        // Branch: Value fails predicate
        var ruleFail = Rule.on("test", "field").satisfies(s -> s.equals("match"), "not match");
        assertEquals("not match", ruleFail.getViolations().get(0));
    }

    @Test
    void testMessageBranching() {
        // Branch: violations list is empty
        var ruleEmpty = Rule.on("val", "field").message("custom");
        assertTrue(ruleEmpty.getViolations().isEmpty());

        // Branch: violations list has errors (replace last)
        var ruleWithErrors = Rule.on("val", "field")
                .satisfies(v -> false, "original error")
                .message("custom error");
        assertEquals(1, ruleWithErrors.getViolations().size());
        assertEquals("custom error", ruleWithErrors.getViolations().get(0));
    }

    @Test
    void testCheckNestingAndFlattening() {
        // Branch: value is null (nested validator should not run)
        assertDoesNotThrow(() -> Rule.on((String) null, "field").check(v -> {
            throw new RuntimeException("Should not run");
        }));

        // Branch: value is not null, nested throws RecordValidationException
        // This tests the catch block and the flattening of nested violations
        var parentRule = Rule.on("parentValue", "parentField").check(v -> {
            // Simulate a failed nested check
            RecordRules.check(
                    Rule.on("childValue", "childField").matches("NON_EXISTENT_PATTERN")
            );
        });

        List<String> violations = parentRule.getViolations();
        assertEquals(1, violations.size());
        // Verify formatted string: "childField [error_message]"
        assertTrue(violations.get(0).contains("childField"));
        assertTrue(violations.get(0).contains("must match pattern"));
    }
}