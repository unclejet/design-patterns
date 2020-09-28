package com.uj.study.formTemplateMethod.strategy;

import com.uj.study.formTemplateMethod.Loan;
import com.uj.study.formTemplateMethod.Payment;
import com.uj.study.formTemplateMethod.RiskFactor;

import java.util.Date;
import java.util.Iterator;

/**
 * @author ：unclejet
 * @date ：Created in 2020/9/28 上午6:59
 * @description：
 * @modified By：
 * @version:
 */
public abstract class CapitalStrategy {
    private static final int MILLIS_PER_DAY = 86400000;
    private static final int DAYS_PER_YEAR = 365;

    public double capital(Loan loan) {
        return riskAmountFor(loan) *
                duration(loan) * riskFactorFor(loan);
    }

    protected abstract double riskAmountFor(Loan loan);

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

    protected double riskFactorFor(Loan loan) {        // moved from Loan
        return RiskFactor.getFactors().forRating(loan.getRiskRating());
    }

    protected double yearsTo(Date endDate, Loan loan) {
        Date beginDate = (loan.getToday() == null ? loan.getStart() : loan.getToday());
        return ((endDate.getTime() - beginDate.getTime()) / MILLIS_PER_DAY) / DAYS_PER_YEAR;
    }
}
