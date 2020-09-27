package com.uj.study.replaceConditionalLogicwithStrategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * @author ：unclejet
 * @date ：Created in 2020/9/27 上午6:35
 * @description：
 * @modified By：
 * @version:
 */
public class Loan {

    private static final long MILLIS_PER_DAY = -1;
    private static final long DAYS_PER_YEAR = -1;
    private Date expiry;
    private Date maturity;
    private double commitment;
    private double outstanding;
    private ArrayList<Object> payments;
    private Date today;
    private Risk riskRating;

    public double capital() {
        if (expiry == null && maturity != null)
            return commitment * duration() * riskFactor();
        if (expiry != null && maturity == null) {
            if (getUnusedPercentage() != 1.0)
                return commitment * getUnusedPercentage() * duration() * riskFactor();
            else
                return (outstandingRiskAmount() * duration() * riskFactor())
                        + (unusedRiskAmount() * duration() * unusedRiskFactor());
        }
        return 0.0;
    }

    private double getUnusedPercentage() {
        return -1;
    }

    private double outstandingRiskAmount() {
        return outstanding;
    }

    private double unusedRiskAmount() {
        return (commitment - outstanding);
    }

    public double duration() {
        if (expiry == null && maturity != null)
            return weightedAverageDuration();
        else if (expiry != null && maturity == null)
            return yearsTo(expiry);
        return 0.0;
    }

    private double weightedAverageDuration() {
        double duration = 0.0;
        double weightedAverage = 0.0;
        double sumOfPayments = 0.0;
        Iterator loanPayments = payments.iterator();
        while (loanPayments.hasNext()) {
            Payment payment = (Payment)loanPayments.next();
            sumOfPayments += payment.amount();
            weightedAverage += yearsTo(payment.date()) * payment.amount();
        }
        if (commitment != 0.0)
            duration = weightedAverage / sumOfPayments;
        return duration;
    }

    private double yearsTo(java.util.Date endDate) {
        Date start = new Date();
        Date beginDate = (today == null ? start : today);
        return ((endDate.getTime() - beginDate.getTime()) / MILLIS_PER_DAY) / DAYS_PER_YEAR;
    }

    private double riskFactor() {
        return RiskFactor.getFactors().forRating(riskRating);
    }

    private double unusedRiskFactor() {
        return UnusedRiskFactors.getFactors().forRating(riskRating);
    }
}
