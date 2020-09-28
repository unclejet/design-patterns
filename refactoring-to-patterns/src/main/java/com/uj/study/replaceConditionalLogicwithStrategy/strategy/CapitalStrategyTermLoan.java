package com.uj.study.replaceConditionalLogicwithStrategy.strategy;

import com.uj.study.replaceConditionalLogicwithStrategy.Loan;
import com.uj.study.replaceConditionalLogicwithStrategy.Payment;

import java.util.Iterator;

/**
 * @author ：unclejet
 * @date ：Created in 2020/9/29 上午5:31
 * @description：
 * @modified By：
 * @version:
 */
public class CapitalStrategyTermLoan extends CapitalStrategy {

    public double capital(Loan loan) {
        return loan.getCommitment() * duration(loan) * riskFactorFor(loan);
    }

    public double duration(Loan loan) {
        return weightedAverageDuration(loan);
    }

    private double weightedAverageDuration(Loan loan) {
        double duration = 0.0;
        double weightedAverage = 0.0;
        double sumOfPayments = 0.0;

        Iterator loanPayments = loan.getPayments().iterator();
        while (loanPayments.hasNext()) {
            Payment payment = (Payment)loanPayments.next();
            sumOfPayments += payment.amount();
            weightedAverage += yearsTo(payment.date(), loan) * payment.amount();
        }
        if (loan.getCommitment() != 0.0)
            duration = weightedAverage / sumOfPayments;
        return duration;
    }
}
