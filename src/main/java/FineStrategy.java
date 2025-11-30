//1. Strategy Interface
public interface FineStrategy {

    int calculateFine(int overdueDays);
}
//2. Concrete Strategies
 class BookFineStrategy implements FineStrategy {
    @Override
    public int calculateFine(int overdueDays) {
        return overdueDays * 10; // 10 NIS per day
    }
}

 class CDFineStrategy implements FineStrategy {
    @Override
    public int calculateFine(int overdueDays) {
        return overdueDays * 20; // 20 NIS per day
    }
}

 class JournalFineStrategy implements FineStrategy {
    @Override
    public int calculateFine(int overdueDays) {
        return overdueDays * 15; // 15 NIS per day
    }
}

 //3. Context Class (e.g., Loan or Borrowing)
  class Loan {
	    private FineStrategy fineStrategy;
	    private int overdueDays;

	    public Loan(FineStrategy fineStrategy, int overdueDays) {
	        this.fineStrategy = fineStrategy;
	        this.overdueDays = overdueDays;
	    }

	    public int calculateFine() {
	        return fineStrategy.calculateFine(overdueDays);
	    }
	}
