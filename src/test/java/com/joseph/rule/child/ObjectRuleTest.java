package com.joseph.rule.child;

import com.joseph.RecordRules;
import com.joseph.exception.RecordValidationException;
import com.joseph.rule.Rule;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObjectRuleTest {

    @Test
    void testFactoryMethod() {
        // Targets Rule.on(T, String) which returns an ObjectRule
        Object obj = new Object();
        ObjectRule<Object> rule = Rule.on(obj, "item");
        assertNotNull(rule);
        assertEquals("item", rule.getFieldName());
    }

    @Test
    void testMinSizeAndMaxSize() {
        List<String> list = List.of("A", "B", "C");

        // Branch: Valid size
        ObjectRule<List<String>> ruleValid = Rule.on(list, "list").minSize(1).maxSize(5);
        assertTrue(ruleValid.getViolations().isEmpty());

        // Branch: Too small
        ObjectRule<List<String>> ruleSmall = Rule.on(list, "list").minSize(5);
        assertEquals(1, ruleSmall.getViolations().size());
        assertTrue(ruleSmall.getViolations().get(0).contains("must have at least 5 items"));

        // Branch: Too large
        ObjectRule<List<String>> ruleLarge = Rule.on(list, "list").maxSize(2);
        assertEquals(1, ruleLarge.getViolations().size());
        assertTrue(ruleLarge.getViolations().get(0).contains("must have at most 2 items"));
    }

    @Test
    void testSizeMethodsWithNonCollection() {
        // Branch: Value is not a collection (should not add violations or crash)
        ObjectRule<Object> rule = Rule.on(new Object(), "field").minSize(1).maxSize(10);
        assertTrue(rule.getViolations().isEmpty());

        // Branch: Value is null
        ObjectRule<List<Object>> ruleNull = Rule.on(List.of(), "field").minSize(1);
        assertFalse(ruleNull.getViolations().isEmpty());
    }

    @Test
    void testForEach() {
        List<String> items = List.of("First", "Second");
        List<String> capturedValues = new ArrayList<>();
        List<Integer> capturedIndices = new ArrayList<>();

        // Branch: value is Iterable
        Rule.on(items, "list").forEach((val, idx) -> {
            capturedValues.add((String) val);
            capturedIndices.add(idx);
        });

        assertEquals(items, capturedValues);
        assertEquals(List.of(0, 1), capturedIndices);
    }

    @Test
    void testForEachWithNonIterable() {
        Rule.on(new Object(), "field").forEach((val, idx) ->
                fail("BiConsumer should not be called for non-iterable types"));
    }


    @Test
    void shouldFailWhenNestedRecordIsInvalid() {
        Address invalidAddress = new Address(" ", "abc");
        RecordValidationException exception = assertThrows(RecordValidationException.class, () ->
                new User("Joseph", invalidAddress));
        assertTrue(exception.getMessage().contains("zip [must match pattern \\d{5}]"));
    }

    @Test
    void shouldFailWhenListItemsAreInvalid() {
        List<String> badEmails = List.of("valid@test.com", "invalid-email");
        RecordValidationException exception = assertThrows(RecordValidationException.class, () ->
                new Team("Dev Team", badEmails));
        assertTrue(exception.getMessage().contains("email[1]: [must be a valid email]"));
    }

    @Test
    void shouldNotExecuteNestedRulesIfObjectIsNull() {
        RecordValidationException exception = assertThrows(RecordValidationException.class, () ->
                new User("Joseph", null));
        assertTrue(exception.getErrors().containsKey("address"));
        assertFalse(exception.getMessage().contains("city"), "Should skip nested check for null value");
    }

    // --- Helper Records ---

    public record Address(String city, String zip) {
    }

    public record User(String name, Address address) {
        public User {
            RecordRules.check(
                    Rule.on(name, "name").required(),
                    Rule.on(address, "address").required().check(addr ->
                            RecordRules.check(
                                    Rule.on(addr.city(), "city").required().notBlank(),
                                    Rule.on(addr.zip(), "zip").required().matches("\\d{5}")
                            ))
            );
        }
    }

    public record Team(String teamName, List<String> memberEmails) {
        public Team {
            RecordRules.check(
                    Rule.on(teamName, "teamName").required(),
                    Rule.on(memberEmails, "members").required()
                            .minSize(1)
                            .forEach((email, i) ->
                                    RecordRules.check(Rule.on((String) email, "email[" + i + "]").email()))
            );
        }
    }
}