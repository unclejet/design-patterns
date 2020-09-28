package com.uj.study.replaceConditionalLogicwithStrategy;

import com.uj.study.replaceConditionalLogicwithStrategy.strategy.CapitalStrategy;
import com.uj.study.replaceConditionalLogicwithStrategy.strategy.CapitalStrategyAdvisedLine;
import com.uj.study.replaceConditionalLogicwithStrategy.strategy.CapitalStrategyRevolver;
import com.uj.study.replaceConditionalLogicwithStrategy.strategy.CapitalStrategyTermLoan;

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

    private CapitalStrategy capitalStrategy;

    private Loan(double commitment, double outstanding,
                 Date start, Date expiry, Date maturity, int riskRating, CapitalStrategy capitalStrategy) {
        //...
        this.capitalStrategy = capitalStrategy;
    }

    public static Loan newTermLoan(double commitment, Date start, Date maturity, int riskRating) {
        return new Loan(commitment, commitment, start, null, maturity, riskRating, new CapitalStrategyTermLoan());
    }

    public static Loan newRevolver(
            double commitment, Date start, Date expiry, int riskRating) {

        return new Loan(commitment, 0, start, expiry,
                null, riskRating,
                new CapitalStrategyRevolver()
        );
    }

    public static Loan newAdvisedLine(
            double commitment, Date start, Date expiry, int riskRating) {
        if (riskRating > 3) return null;
        Loan advisedLine =
                new Loan(commitment, 0, start, expiry, null, riskRating,
                        new CapitalStrategyAdvisedLine());
        advisedLine.setUnusedPercentage(0.1);
        return advisedLine;
    }

    private void setUnusedPercentage(double v) {
        //...
    }

    public double capital() {
        return capitalStrategy.capital(this);
    }

    public double getUnusedPercentage() {
        return -1;
    }

    public double outstandingRiskAmount() {
        return outstanding;
    }

    public double unusedRiskAmount() {
        return (commitment - outstanding);
    }

    public double duration() {
        return capitalStrategy.duration(this);
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

    public double riskFactor() {
        return RiskFactor.getFactors().forRating(riskRating);
    }

    public double unusedRiskFactor() {
        return UnusedRiskFactors.getFactors().forRating(riskRating);
    }

    public void payment(double v, Date from) {

    }

    public static long getMillisPerDay() {
        return MILLIS_PER_DAY;
    }

    public static long getDaysPerYear() {
        return DAYS_PER_YEAR;
    }

    public Date getExpiry() {
        return expiry;
    }

    public Date getMaturity() {
        return maturity;
    }

    public double getCommitment() {
        return commitment;
    }

    public double getOutstanding() {
        return outstanding;
    }

    public ArrayList<Object> getPayments() {
        return payments;
    }

    public Date getToday() {
        return today;
    }

    public Risk getRiskRating() {
        return riskRating;
    }

    public Date getStart() {
        return today;
    }
}
