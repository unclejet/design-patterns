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