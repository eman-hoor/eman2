//1. Strategy Interface
/**
 * Strategy interface for calculating fines on overdue items.
 * Different media types (books, CDs, journals) can implement
 * their own fine calculation rules.
 *
 * @author Your Name
 * @version 1.0
 */
public interface FineStrategy {
	/**
     * Calculates the fine based on the number of overdue days.
     *
     * @param overdueDays the number of days the item is overdue
     * @return the fine amount
     */

    int calculateFine(int overdueDays); 
}
/**
 * Fine calculation strategy for books.
 * Charges 10 NIS per overdue day.
 */
//2. Concrete Strategies

 class BookFineStrategy implements FineStrategy {
    @Override
    public int calculateFine(int overdueDays) {
        return overdueDays * 10; // 10 NIS per day
    }
} 

 /**
  * Fine calculation strategy for CDs.
  * Charges 20 NIS per overdue day.
  */
 class CDFineStrategy implements FineStrategy {
    @Override
    public int calculateFine(int overdueDays) {
        return overdueDays * 20; // 20 NIS per day
    }
}
 

 /*class JournalFineStrategy implements FineStrategy {
    @Override
    public int calculateFine(int overdueDays) {
        return overdueDays * 15; // 15 NIS per day
    }
}*/

 //3. Context Class (e.g., Loan or Borrowing)
 /**
  * Represents a loan of a library item.
  * Uses a {@link FineStrategy} to calculate fines
  * based on the type of item and overdue days.
  */
  class Loan {	   
	  /** The fine calculation strategy (e.g., book, CD). */
	    private FineStrategy fineStrategy;
	 /** The number of days the item is overdue. */
	    private int overdueDays;
	    /**
	     * Constructs a Loan with the given fine strategy and overdue days.
	     *
	     * @param fineStrategy the strategy used to calculate fines
	     * @param overdueDays  the number of days the item is overdue
	     */

	    public Loan(FineStrategy fineStrategy, int overdueDays) {
	        this.fineStrategy = fineStrategy;
	        this.overdueDays = overdueDays;
	    }
	    /**
	     * Calculates the fine using the configured strategy.
	     *
	     * @return the fine amount
	     */

	    public int calculateFine() {
	        return fineStrategy.calculateFine(overdueDays);
	    }
	}
