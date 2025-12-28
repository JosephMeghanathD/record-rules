package com.joseph.rule;

import com.joseph.RecordRules;
import com.joseph.exception.RecordValidationException;
import com.joseph.rule.child.DateRule;
import com.joseph.rule.child.NumberRule;
import com.joseph.rule.child.ObjectRule;
import com.joseph.rule.child.StringRule;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class RuleTest {

}