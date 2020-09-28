package com.uj.study.formTemplateMethod.strategy;

import com.uj.study.replaceConditionalLogicwithStrategy.Loan;

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
        return (loan.outstandingRiskAmount() * duration(loan) * riskFactorFor(loan))
                + (loan.unusedRiskAmount() * duration(loan) * unusedRiskFactor(loan));
    }

    private double unusedRiskFactor(Loan loan) {
        return 0;
    }
}
