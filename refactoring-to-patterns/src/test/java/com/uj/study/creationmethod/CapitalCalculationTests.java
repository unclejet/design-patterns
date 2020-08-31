package com.uj.study.creationmethod;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ：unclejet
 * @date ：Created in 2020/9/1 上午7:56
 * @description：
 * @modified By：
 * @version:
 */
class CapitalCalculationTests {
    private double commitment;
    private int riskRating;
    private Date maturity;

    public void testTermLoanNoPayments() {
//      ...
        Loan termLoan = new Loan(commitment, riskRating, maturity);
//      ...
    }
}