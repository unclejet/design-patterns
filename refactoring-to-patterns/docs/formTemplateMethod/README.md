![R2P](./Screenshot from 2020-09-29 05-50-18.png)
At the end of the example used in this catalog for the refactoring Replace Conditional Logic with Strategy (129) there are three subclasses of the abstract class, CapitalStrategy:

![R2P](./Screenshot from 2020-09-29 06-03-17.png)


These three subclasses happen to contain a small amount of duplication, which, as we'll see in this section, can be removed by applying Form Template Method. It is relatively common to combine the Strategy and Template Method patterns to produce concrete Strategy classes that have little or no duplicate code in them.

The CapitalStrategy class defines an abstract method for the capital calculation:

public abstract class CapitalStrategy...
   public abstract double capital(Loan loan);

Subclasses of CapitalStrategy calculate capital similarly:

public class CapitalStrategyAdvisedLine...
   public double capital(Loan loan) {
      return loan.getCommitment() * loan.getUnusedPercentage() *
             duration(loan) * riskFactorFor(loan);
   }

public class CapitalStrategyRevolver...
   public double capital(Loan loan) {
      return (loan.outstandingRiskAmount() * duration(loan) * riskFactorFor(loan))
           + (loan.unusedRiskAmount() * duration(loan) * unusedRiskFactor(loan));
   }

public class CapitalStrategyTermLoan...
   public double capital(Loan loan) {
      return loan.getCommitment() * duration(loan) * riskFactorFor(loan);
   }
   protected double duration(Loan loan) {
      return weightedAverageDuration(loan);
   }
   private double weightedAverageDuration(Loan loan)...

I observe that CapitalStrategyAdvisedLine's calculation is identical to CapitalStrategyTermLoan's calculation, except for a step that multiplies the result by the loan's unused percentage (loan.getUnusedPercentage()). Spotting this similar sequence of steps with a slight variation means I can generalize the algorithm by refactoring to Template Method. I'll do that in the following steps and then deal with the third class, CapitalStrategyRevolver, at the end of this Example section.