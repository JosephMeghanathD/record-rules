package com.joseph.rule.child;

import com.joseph.RecordRules;
import com.joseph.exception.RecordValidationException;
import com.joseph.rule.Rule;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class StringRuleTest {

    @Test
    void testNotBlankBranches() {
        // Branch 1: Value is null (Should skip validation logic)
        var ruleNull = Rule.on((String) null, "field").notBlank();
        assertTrue(ruleNull.getViolations().isEmpty());

        // Branch 2: Value is blank (Should add violation)
        var ruleBlank = Rule.on("   ", "field").notBlank();
        assertEquals(1, ruleBlank.getViolations().size());
        assertEquals("must not be blank", ruleBlank.getViolations().get(0));

        // Branch 3: Value is valid (Should not add violation)
        var ruleValid = Rule.on("valid", "field").notBlank();
        assertTrue(ruleValid.getViolations().isEmpty());
    }

    @Test
    void testEmailBranches() {
        // Branch 1: Value is null
        var ruleNull = Rule.on((String) null, "email").email();
        assertTrue(ruleNull.getViolations().isEmpty());

        // Branch 2: Value is invalid email
        var ruleInvalid = Rule.on("not-an-email", "email").email();
        assertEquals(1, ruleInvalid.getViolations().size());

        // Branch 3: Value is valid email
        var ruleValid = Rule.on("test@example.com", "email").email();
        assertTrue(ruleValid.getViolations().isEmpty());
    }

    @Test
    void testMatchesBranches() {
        String regex = "^[0-9]+$";
        // Branch 1: Value is null
        var ruleNull = Rule.on((String) null, "field").matches(regex);
        assertTrue(ruleNull.getViolations().isEmpty());

        // Branch 2: Value does not match
        var ruleFail = Rule.on("abc", "field").matches(regex);
        assertEquals(1, ruleFail.getViolations().size());

        // Branch 3: Value matches
        var rulePass = Rule.on("123", "field").matches(regex);
        assertTrue(rulePass.getViolations().isEmpty());
    }

    @Test
    void testLengthBranches() {
        // Branch 1: Value is null
        var ruleNull = Rule.on((String) null, "field").length(1, 5);
        assertTrue(ruleNull.getViolations().isEmpty());

        // Branch 2: Value is too short (value.length() < min)
        var ruleShort = Rule.on("a", "field").length(3, 5);
        assertEquals(1, ruleShort.getViolations().size());
        assertTrue(ruleShort.getViolations().get(0).contains("must be between 3 and 5 characters"));

        // Branch 3: Value is too long (value.length() > max)
        var ruleLong = Rule.on("abcdef", "field").length(3, 5);
        assertEquals(1, ruleLong.getViolations().size());

        // Branch 4: Value is exactly min
        var ruleMin = Rule.on("abc", "field").length(3, 5);
        assertTrue(ruleMin.getViolations().isEmpty());

        // Branch 5: Value is exactly max
        var ruleMax = Rule.on("abcde", "field").length(3, 5);
        assertTrue(ruleMax.getViolations().isEmpty());
    }

    @Test
    void testMinLengthAndMaxLengthWrappers() {
        // minLength(int) calls length(min, Integer.MAX_VALUE)
        var ruleMin = Rule.on("a", "field").minLength(2);
        assertEquals(1, ruleMin.getViolations().size());

        // maxLength(int) calls length(0, max)
        var ruleMax = Rule.on("abc", "field").maxLength(2);
        assertEquals(1, ruleMax.getViolations().size());
    }


    @Test
    void shouldThrowAndContainBothMessages_WhenMinAndMaxFail() {
        record TestRecord(String someStringField) {
            TestRecord {
                RecordRules.check(
                        Rule.on(someStringField, "someStringField")
                                .minLength(3).message("Too short")
                                .maxLength(5).message("Too long")
                );
            }
        }

        assertThatThrownBy(() -> new TestRecord("A"))
                .isInstanceOf(RecordValidationException.class)
                .satisfies(ex -> {
                    var errors = ((RecordValidationException) ex)
                            .getErrors()
                            .get("someStringField");

                    assertThat(String.join(" ", errors))
                            .containsIgnoringCase("Too short");
                });

        assertThatThrownBy(() -> new TestRecord("ABCDEF"))
                .isInstanceOf(RecordValidationException.class)
                .satisfies(ex -> {
                    var errors = ((RecordValidationException) ex)
                            .getErrors()
                            .get("someStringField");

                    assertThat(String.join(" ", errors))
                            .containsIgnoringCase("Too long");
                });
    }

    @Test
    void shouldNotOverwriteErrorMessage_WhenPredicateFailsAndNextRulePasses() {
        record TestRecord(String code) {
            TestRecord {
                RecordRules.check(
                        Rule.on(code, "code")
                                .satisfies(v -> v.startsWith("A")).message("Must start with A")
                                .maxLength(5).message("Too long")
                );
            }
        }

        assertThatThrownBy(() -> new TestRecord("BCD"))
                .isInstanceOf(RecordValidationException.class)
                .satisfies(ex -> {
                    var errors = ((RecordValidationException) ex).getErrors().get("code");
                    assertEquals(1, errors.size());
                    assertThat(errors.get(0)).isEqualTo("Must start with A");
                });
    }

}