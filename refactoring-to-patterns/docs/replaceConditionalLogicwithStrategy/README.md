![Introduce Polymorphic Creation with Factory Method](./Screenshot from 2020-09-27 06-28-57.png)

## project introduction
The example in the code sketch presented in the introduction to this refactoring deals with calculating capital for three different kinds of bank loans: a term loan, a revolver, and an advised line. It contains a fair amount of conditional logic used in performing the capital calculation, though it's less complicated and contains less conditional logic than was present in the original code, which handled capital calculations for seven distinct loan types.

In this example, we'll see how Loan's method for calculating capital can be strategized (i.e., delegated to a Strategy object). As you study the example, you may wonder why Loan wasn't simply subclassed to support the different styles of capital calculations. That would not have been a good design choice because the application that used Loan needed to accomplish the following.

Calculate capital for loans in a variety of ways. Had there been one Loan subclass for each type of capital calculation, the Loan hierarchy would have been overburdened with subclasses, as shown in the diagram on the following page.

![Introduce Polymorphic Creation with Factory Method](./Screenshot from 2020-09-27 06-32-46.png)

Change a loan's capital calculation at runtime, without changing the class type of the Loan instance. This is easier to do when it involves exchanging a Loan object's Strategy instance for another Strategy instance, rather than changing the whole Loan object from one subclass of Loan into another.

Now let's look at some code. The Loan class, which plays the role of the context (as defined in the Mechanics section), contains a calculation method called capital():

public class Loan...
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

Much of the conditional logic deals with figuring out whether the loan is a term loan, a revolver, or an advised line. For example, a null expiry date and a non-null maturity date indicate a term loan. That code doesn't reveal its intentions well, does it? Once the code figures out what type of loan it has, a specific capital calculation can be performed. There are three such capital calculations, one for each loan type. All three of these calculations rely on the following helper methods:

public class Loan...
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

   private double yearsTo(Date endDate) {
      Date beginDate = (today == null ? start : today);
      return ((endDate.getTime() - beginDate.getTime()) / MILLIS_PER_DAY) / DAYS_PER_YEAR;
   }

   private double riskFactor() {
      return RiskFactor.getFactors().forRating(riskRating);
   }

   private double unusedRiskFactor() {
      return UnusedRiskFactors.getFactors().forRating(riskRating);
   }

The Loan class can be simplified by extracting specific calculation logic into individual strategy classes, one for each loan type. For example, the method weightedAverageDuration() is used only to calculate capital for a term loan.