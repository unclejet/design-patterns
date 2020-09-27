package com.uj.study.replaceConditionalLogicwithStrategy.strategy;

import com.uj.study.replaceConditionalLogicwithStrategy.Loan;
import com.uj.study.replaceConditionalLogicwithStrategy.Payment;
import com.uj.study.replaceConditionalLogicwithStrategy.RiskFactor;
import com.uj.study.replaceConditionalLogicwithStrategy.UnusedRiskFactors;

import java.util.Date;
import java.util.Iterator;

/**
 * @author ：unclejet
 * @date ：Created in 2020/9/28 上午6:59
 * @description：
 * @modified By：
 * @version:
 */
public class CapitalStrategy {
    private static final int MILLIS_PER_DAY = 86400000;
    private static final int DAYS_PER_YEAR = 365;

    public double capital(Loan loan) {
        if (loan.getExpiry() == null && loan.getMaturity() != null)
            return loan.getCommitment() * loan.duration() * riskFactorFor(loan);
        if (loan.getExpiry() != null && loan.getMaturity() == null) {
            if (loan.getUnusedPercentage() != 1.0)
                return loan.getCommitment() * loan.getUnusedPercentage() * loan.duration() * riskFactorFor(loan);
            else
                return (loan.outstandingRiskAmount() * loan.duration() * riskFactorFor(loan))
                        + (loan.unusedRiskAmount() * loan.duration() * unusedRiskFactorFor(loan));
        }
        return 0.0;
    }

    private double riskFactorFor(Loan loan) {        // moved from Loan
        return RiskFactor.getFactors().forRating(loan.getRiskRating());
    }


    private double unusedRiskFactorFor(Loan loan) {    // moved from Loan
        return UnusedRiskFactors.getFactors().forRating(loan.getRiskRating());
    }

    public double duration(Loan loan) {
        if (loan.getExpiry() == null && loan.getMaturity() != null)
            return weightedAverageDuration(loan);
        else if (loan.getExpiry() != null && loan.getMaturity() == null)
            return yearsTo(loan.getExpiry(), loan);
        return 0.0;
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

    private double yearsTo(Date endDate, Loan loan) {
        Date beginDate = (loan.getToday() == null ? loan.getStart() : loan.getToday());
        return ((endDate.getTime() - beginDate.getTime()) / MILLIS_PER_DAY) / DAYS_PER_YEAR;
    }
}
