package com.uj.study.formTemplateMethod.strategy;


import com.uj.study.formTemplateMethod.Loan;

/**
 * @author ：unclejet
 * @date ：Created in 2020/9/29 上午5:34
 * @description：
 * @modified By：
 * @version:
 */
public class CapitalStrategyAdvisedLine extends CapitalStrategy {
    @Override
    protected double riskAmountFor(Loan loan) {
        return loan.getCommitment();
    }

    protected double unusedPercentageFor(Loan loan) {
        return loan.getUnusedPercentage();
    };
}
