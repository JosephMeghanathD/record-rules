package com.joseph.rule;

import com.joseph.RecordRules;
import com.joseph.exception.RecordValidationException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObjectRuleTest {

    // --- Helper Records for Testing ---

    @Test
    void shouldFailWhenNestedRecordIsInvalid() {
        // City is blank, Zip is wrong format
        Address invalidAddress = new Address(" ", "abc");

        RecordValidationException exception = assertThrows(RecordValidationException.class, () -> {
            new User("Joseph", invalidAddress);
        });

        String message = exception.getMessage();

        // Check that the parent field "address" contains flattened nested errors
        assertTrue(message.contains("[address: [zip [must match pattern \\d{5}], city [must not be blank]]]"), "Error message should contain nested address violations");
    }

    @Test
    void shouldFailWhenListItemsAreInvalid() {
        List<String> badEmails = List.of("valid@test.com", "invalid-email", "also-bad");

        RecordValidationException exception = assertThrows(RecordValidationException.class, () -> {
            new Team("Dev Team", badEmails);
        });

        String message = exception.getMessage();

        // Verify that the list indices are captured
        assertTrue(message.contains("email[1] [must be a valid email]"));
        assertFalse(message.contains("email[0]"), "Valid email should not have violations");
    }

    @Test
    void shouldPassWhenNestedObjectsAreValid() {
        assertDoesNotThrow(() -> {
            new User("Joseph", new Address("New York", "10001"));
        });

        assertDoesNotThrow(() -> {
            new Team("QA", List.of("tester@company.com", "lead@company.com"));
        });
    }

    // --- Tests ---

    @Test
    void shouldNotExecuteNestedRulesIfObjectIsNull() {
        // If address is null, required() will catch it, but check() won't throw a NullPointerException
        RecordValidationException exception = assertThrows(RecordValidationException.class, () -> {
            new User("Joseph", null);
        });

        assertEquals(1, exception.getErrors().size());
        assertTrue(exception.getMessage().contains("address: [must not be null]"));
        assertFalse(exception.getMessage().contains("city"), "Should not try to validate city of a null address");
    }

    public record Address(String city, String zip) {
    }

    public record User(String name, Address address) {
        public User {
            RecordRules.check(Rule.on(name, "name").required().notBlank(), Rule.on(address, "address").required().check(addr -> {
                // Nested rules inside the rule
                RecordRules.check(Rule.on(addr.city(), "city").required().notBlank(), Rule.on(addr.zip(), "zip").required().matches("\\d{5}"));
            }));
        }
    }

    public record Team(String teamName, List<String> memberEmails) {
        public Team {
            RecordRules.check(Rule.on(teamName, "teamName").required(), Rule.on(memberEmails, "members").required().check(emails -> {
                // Rule inside a rule for List iteration
                for (int i = 0; i < emails.size(); i++) {
                    String email = emails.get(i);
                    RecordRules.check(Rule.on(email, "email[" + i + "]").email());
                }
            }));
        }
    }
}