package com.uj.study.creationmethod;

import java.util.Date;

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
        Loan termLoan = Loan.createTermLoan(commitment, riskRating, maturity);
                
//      ...
    }

}