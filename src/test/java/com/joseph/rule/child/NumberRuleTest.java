package com.joseph.rule.child;

import com.joseph.rule.Rule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class NumberRuleTest {

    @Test
    void testMinBranches_Integer() {
        // Branch 1: Value is null (Should skip validation)
        NumberRule ruleNull = Rule.on((Integer) null, "age").min(18);
        assertTrue(ruleNull.getViolations().isEmpty());

        // Branch 2: Value is exactly the minimum (Pass)
        NumberRule ruleEqual = Rule.on(18, "age").min(18);
        assertTrue(ruleEqual.getViolations().isEmpty());

        // Branch 3: Value is greater than minimum (Pass)
        NumberRule ruleGreater = Rule.on(20, "age").min(18);
        assertTrue(ruleGreater.getViolations().isEmpty());

        // Branch 4: Value is less than minimum (Fail)
        NumberRule ruleFail = Rule.on(15, "age").min(18);
        assertEquals(1, ruleFail.getViolations().size());
        assertEquals("must be at least 18", ruleFail.getViolations().get(0));
    }

    @Test
    void testMinBranches_Long() {
        NumberRule ruleNull = Rule.on((Long) null, "age").min(18L);
        assertTrue(ruleNull.getViolations().isEmpty());

        NumberRule ruleEqual = Rule.on(18L, "age").min(18L);
        assertTrue(ruleEqual.getViolations().isEmpty());

        NumberRule ruleGreater = Rule.on(20L, "age").min(18L);
        assertTrue(ruleGreater.getViolations().isEmpty());

        NumberRule ruleFail = Rule.on(15L, "age").min(18L);
        assertEquals(1, ruleFail.getViolations().size());
        assertEquals("must be at least 18", ruleFail.getViolations().get(0));
    }

    @Test
    void testMinBranches_Short() {
        NumberRule ruleNull = Rule.on((Short) null, "age").min((short) 18);
        assertTrue(ruleNull.getViolations().isEmpty());

        NumberRule ruleEqual = Rule.on((short) 18, "age").min((short) 18);
        assertTrue(ruleEqual.getViolations().isEmpty());

        NumberRule ruleGreater = Rule.on((short) 20, "age").min((short) 18);
        assertTrue(ruleGreater.getViolations().isEmpty());

        NumberRule ruleFail = Rule.on((short) 15, "age").min((short) 18);
        assertEquals(1, ruleFail.getViolations().size());
        assertEquals("must be at least 18", ruleFail.getViolations().get(0));
    }

    @Test
    void testMinBranches_Byte() {
        NumberRule ruleNull = Rule.on((Byte) null, "age").min((byte) 18);
        assertTrue(ruleNull.getViolations().isEmpty());

        NumberRule ruleEqual = Rule.on((byte) 18, "age").min((byte) 18);
        assertTrue(ruleEqual.getViolations().isEmpty());

        NumberRule ruleGreater = Rule.on((byte) 20, "age").min((byte) 18);
        assertTrue(ruleGreater.getViolations().isEmpty());

        NumberRule ruleFail = Rule.on((byte) 15, "age").min((byte) 18);
        assertEquals(1, ruleFail.getViolations().size());
        assertEquals("must be at least 18", ruleFail.getViolations().get(0));
    }

    @Test
    void testMinBranches_Double() {
        NumberRule ruleNull = Rule.on((Double) null, "age").min(18.0);
        assertTrue(ruleNull.getViolations().isEmpty());

        NumberRule ruleEqual = Rule.on(18.0, "age").min(18.0);
        assertTrue(ruleEqual.getViolations().isEmpty());

        NumberRule ruleGreater = Rule.on(20.5, "age").min(18.0);
        assertTrue(ruleGreater.getViolations().isEmpty());

        NumberRule ruleFail = Rule.on(15.9, "age").min(18.0);
        assertEquals(1, ruleFail.getViolations().size());
        assertEquals("must be at least 18.0", ruleFail.getViolations().get(0));
    }

    @Test
    void testMinBranches_Float() {
        NumberRule ruleNull = Rule.on((Float) null, "age").min(18.0f);
        assertTrue(ruleNull.getViolations().isEmpty());

        NumberRule ruleEqual = Rule.on(18.0f, "age").min(18.0f);
        assertTrue(ruleEqual.getViolations().isEmpty());

        NumberRule ruleGreater = Rule.on(20.0f, "age").min(18.0f);
        assertTrue(ruleGreater.getViolations().isEmpty());

        NumberRule ruleFail = Rule.on(15.5f, "age").min(18.0f);
        assertEquals(1, ruleFail.getViolations().size());
        assertEquals("must be at least 18.0", ruleFail.getViolations().get(0));
    }

    @Test
    void testMinBranches_BigDecimal() {
        NumberRule ruleNull = Rule.on((BigDecimal) null, "age").min(new BigDecimal("18"));
        assertTrue(ruleNull.getViolations().isEmpty());

        NumberRule ruleEqual = Rule.on(new BigDecimal("18"), "age").min(new BigDecimal("18"));
        assertTrue(ruleEqual.getViolations().isEmpty());

        NumberRule ruleGreater = Rule.on(new BigDecimal("20"), "age").min(new BigDecimal("18"));
        assertTrue(ruleGreater.getViolations().isEmpty());

        NumberRule ruleFail = Rule.on(new BigDecimal("15"), "age").min(new BigDecimal("18"));
        assertEquals(1, ruleFail.getViolations().size());
        assertEquals("must be at least 18", ruleFail.getViolations().get(0));
    }

    @Test
    void testMinBranches_BigInteger() {
        NumberRule ruleNull = Rule.on((BigInteger) null, "age").min(BigInteger.valueOf(18));
        assertTrue(ruleNull.getViolations().isEmpty());

        NumberRule ruleEqual = Rule.on(BigInteger.valueOf(18), "age").min(BigInteger.valueOf(18));
        assertTrue(ruleEqual.getViolations().isEmpty());

        NumberRule ruleGreater = Rule.on(BigInteger.valueOf(20), "age").min(BigInteger.valueOf(18));
        assertTrue(ruleGreater.getViolations().isEmpty());

        NumberRule ruleFail = Rule.on(BigInteger.valueOf(15), "age").min(BigInteger.valueOf(18));
        assertEquals(1, ruleFail.getViolations().size());
        assertEquals("must be at least 18", ruleFail.getViolations().get(0));
    }


    @Test
    void testMaxBranches() {
        // Branch 1: Value is null
        NumberRule ruleNull = Rule.on((Integer) null, "score").max(100);
        assertTrue(ruleNull.getViolations().isEmpty());

        // Branch 2: Value is exactly the maximum (Pass)
        NumberRule ruleEqual = Rule.on(100, "score").max(100);
        assertTrue(ruleEqual.getViolations().isEmpty());

        // Branch 3: Value is less than maximum (Pass)
        NumberRule ruleLess = Rule.on(50, "score").max(100);
        assertTrue(ruleLess.getViolations().isEmpty());

        // Branch 4: Value is greater than maximum (Fail)
        NumberRule ruleFail = Rule.on(101, "score").max(100);
        assertEquals(1, ruleFail.getViolations().size());
        assertEquals("must be at most 100", ruleFail.getViolations().get(0));
    }

    @Test
    void testLongConversionFactory() {
        // This targets Rule.on(Long, String) in the base Rule class
        // which internally calls the NumberRule(Integer, String) constructor.
        NumberRule rule = Rule.on(500L, "count").min(100);
        assertNotNull(rule);
        assertEquals("count", rule.getFieldName());
    }
}