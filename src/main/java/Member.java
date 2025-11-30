
public class Member {

	private String memberId;
    private String name;
    private String contact;
    private double fineBalance = 0.0;

    public Member(String memberId, String name, String contact) {
        this.memberId = memberId;
        this.name = name;
        this.contact = contact;
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public double getFineBalance() { return fineBalance; }

    public void addFine(double amount) { this.fineBalance += amount; }
    public void payFine(double amount) {
        this.fineBalance -= amount;
        if (fineBalance < 0) fineBalance = 0;
    }

	public void setfineBalance(double fineBalance) {
		// TODO Auto-generated method stub
		this.fineBalance=fineBalance;
	}
	
	@Override
    public String toString() {
        return memberId + " - " + name + " (" + contact + "), Fine: " + fineBalance;
    }
}
