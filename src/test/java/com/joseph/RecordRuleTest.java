package com.joseph;

import com.joseph.exception.RecordValidationException;
import com.joseph.rule.Rule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecordRuleTest {

    @Test
    void shouldCollectMultipleValidationErrors() {
        RecordValidationException exception = assertThrows(RecordValidationException.class, () -> {
            new User("bad-email", 15, "  ");
        });

        String message = exception.getMessage();

        // Assert field-specific errors are present
        assertTrue(message.contains("email: [must be a valid email, must match pattern ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$]"));
        assertTrue(message.contains("age: [must be at least 18]"));
        assertTrue(message.contains("username: [must not be blank]"));

        // Assert programmatic access to map
        assertEquals(3, exception.getErrors().size());
    }

    @Test
    void shouldPassWhenDataIsValid() {
        assertDoesNotThrow(() -> new User("test@example.com", 25, "java_dev"));
    }

    public record User(String email, int age, String username) {
        public User {
            RecordRules.check(Rule.on(email, "email").required().email().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"), Rule.on(age, "age").min(18).max(120), Rule.on(username, "username").required().notBlank());
        }
    }
}