package com.uj.study.formTemplateMethod.strategy;

import com.uj.study.formTemplateMethod.Loan;

/**
 * @author ：unclejet
 * @date ：Created in 2020/9/29 上午5:34
 * @description：
 * @modified By：
 * @version:
 */
public class CapitalStrategyRevolver extends CapitalStrategy {
    @Override
    public double capital(Loan loan) {
        return super.capital(loan) * unusedCapital(loan);
    }

    @Override
    protected double riskAmountFor(Loan loan) {
        return loan.outstandingRiskAmount();
    }

    public double unusedCapital(Loan loan) {
        return loan.unusedRiskAmount() * duration(loan) * unusedRiskFactor(loan);
    }

    private double unusedRiskFactor(Loan loan) {
        return 0;
    }
}
