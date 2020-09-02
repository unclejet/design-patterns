![use creation method substitute constructor](./Screenshot%20from%202020-09-01%2007-43-14.png)

# step1
1. My first step is to find a client that calls one of Loan's constructors. Here is one such caller that resides in a test case:

public class CapitalCalculationTests...
   public void testTermLoanNoPayments() {
      ...
      Loan termLoan = new Loan(commitment, riskRating, maturity);
      ...
   }

In this case, a call to the above Loan constructor produces a term loan. I apply Extract Method [F] on that call to produce a public, static method called createTermLoan:

public class CapitalCalculationTests...
   public void testTermLoanNoPayments() {
      ...
      Loan termLoan = 
createTermLoan(commitment, riskRating, maturity);
      ...
   }
   
public static Loan createTermLoan(double commitment, int riskRating, Date maturity) {
      
return new Loan(commitment, riskRating, maturity);
   
}


Next, I apply Move Method [F] on the creation method, createTermLoan, to move it to Loan. This produces the following changes:

public class Loan...
   
public static Loan createTermLoan(double commitment, int riskRating, Date maturity) {
      
return new Loan(commitment, riskRating, maturity);
   
}

public class CapitalCalculationTest...
   public void testTermLoanNoPayments() {
      ...
      Loan termLoan = 
Loan.createTermLoan(commitment, riskRating, maturity);
      ...
   }
   
# step2
2. Next, I find all callers on the constructor that createTermLoan calls, and I update them to call createTermLoan.
public class CapitalCalculationTest...
   public void testTermLoanOnePayment() {
      ...
      

Loan termLoan = new Loan(commitment, riskRating, maturity);
      
Loan termLoan = Loan.createTermLoan(commitment, riskRating, maturity);
      ...
   }

Once again, I compile and test to confirm that everything is working.

# step3 
3. The createTermLoan method is now the only caller on the constructor. Because this constructor is chained to another constructor, I can remove it by applying Inline Method [F] (which, in this case, is actually "inline constructor"). This leads to the following changes:

public class Loan...
   

public Loan(double commitment, int riskRating, Date maturity) {
      

this(commitment, 0.00, riskRating, maturity, null);
   

}

   public static Loan createTermLoan(double commitment, int riskRating, Date maturity) {
      return 
new Loan(commitment, 0.00, riskRating, maturity, null);
   }

I compile and test to confirm that the change works.

# step4
. Now I repeat steps 1–3 to produce additional creation methods on Loan. For example, here is some code that calls Loan's catch-all constructor:

public class CapitalCalculationTest...
   public void testTermLoanWithRiskAdjustedCapitalStrategy() {
      ...
      Loan termLoan = new Loan(riskAdjustedCapitalStrategy, commitment,
                               outstanding, riskRating, maturity, null);
      ...
   }

Notice the null value that is passed in as the last parameter to the constructor. Passing in null values to a constructor is bad practice. It reduces the code's readability. It usually happens because programmers can't find the exact constructor they need, so instead of creating yet another constructor they call a more general-purpose one.

To refactor this code to use a creation method, I'll follow steps 1 and 2. Step 1 leads to another createTermLoan method on Loan:

public class CapitalCalculationTest...
   public void testTermLoanWithRiskAdjustedCapitalStrategy() {
      ...
      Loan termLoan = 
Loan.createTermLoan(riskAdjustedCapitalStrategy, commitment,
                                          outstanding, riskRating, maturity

, null);
      ...
   }

public class Loan...
   public static Loan createTermLoan(double commitment, int riskRating, Date maturity) {
      return new Loan(commitment, 0.00, riskRating, maturity, null);
   }

   
public static Loan createTermLoan(CapitalStrategy riskAdjustedCapitalStrategy,
      
double commitment, double outstanding, int riskRating, Date maturity) {
      
return new Loan(riskAdjustedCapitalStrategy, commitment,
         
outstanding, riskRating, maturity, null);
   
}


Why did I choose to overload createTermLoan(…) instead of producing a creation method with a unique name, like createTermLoanWithStrategy(…)? Because I felt that the presence of the CapitalStrategy parameter sufficiently communicated the difference between the two overloaded versions of createTermLoan(…).

Now for step 2 of the refactoring. Because the new createTermLoan(…) calls Loan's catch-all constructor, I must find other clients that call the catch-all constructor to instantiate the same kind of Loan produced by createTermLoan(…). This requires careful work because some callers of the catch-all constructor produce revolver or RCTL instances of Loan. So I update only the client code that produces term loan instances of Loan.

I don't have to perform any work for step 3 because the catch-all constructor isn't chained to any other constructors. I continue to implement step 4, which involves repeating steps 1–3. When I'm done, I end up with the following creation methods:

# step5
5. The last step is to change the visibility of the only remaining public constructor, which happens to be Loan's catch-all constructor. Since it has no subclasses and it now has no external callers, I make it private:

public class Loan...
   
private Loan(CapitalStrategy capitalStrategy, double commitment,
                double outstanding, int riskRating,
                Date maturity, Date expiry)...

I compile to confirm that everything still works. The refactoring is complete. 