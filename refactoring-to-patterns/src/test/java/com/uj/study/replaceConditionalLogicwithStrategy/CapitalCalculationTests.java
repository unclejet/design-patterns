package com.uj.study.replaceConditionalLogicwithStrategy;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author ：unclejet
 * @date ：Created in 2020/9/28 上午6:39
 * @description：
 * @modified By：
 * @version:
 */
class CapitalCalculationTests {
    private static final double TWO_DIGIT_PRECISION = 0.01;
    private static final double LOAN_AMOUNT = 100;
    private static final double HIGH_RISK_RATING = 0.5;

    @Test
    public void testTermLoanSamePayments() {
        Date start = Date.from(LocalDate.of(2003, Month.NOVEMBER, 20).atStartOfDay().toInstant(ZoneOffset.of("+08:00")));
        Date maturity = Date.from(LocalDate.of(2006, Month.NOVEMBER, 20).atStartOfDay().toInstant(ZoneOffset.of("+08:00")));
        Loan termLoan = Loan.newTermLoan(LOAN_AMOUNT, start, maturity, HIGH_RISK_RATING);
        termLoan.payment(1000.00, Date.from(LocalDate.of(2004, Month.NOVEMBER, 20).atStartOfDay().toInstant(ZoneOffset.of("+08:00"))));
        termLoan.payment(1000.00, Date.from(LocalDate.of(2005, Month.NOVEMBER, 20).atStartOfDay().toInstant(ZoneOffset.of("+08:00"))));
        termLoan.payment(1000.00, Date.from(LocalDate.of(2006, Month.NOVEMBER, 20).atStartOfDay().toInstant(ZoneOffset.of("+08:00"))));
        assertEquals(2.0, termLoan.duration(), TWO_DIGIT_PRECISION, "duration");
        assertEquals(210.00, termLoan.capital(), TWO_DIGIT_PRECISION, "capital");
    }
}