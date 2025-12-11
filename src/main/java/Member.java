/**
 * Represents a library member who can borrow items and accumulate fines.
 * Each member has a unique ID, name, contact information, and a fine balance.
 *
 * @author hoor
 * @version 1.0
 */

public class Member {

    /** The unique identifier of the member. */
	private String memberId;
    /** The full name of the member. */
    private String name;
    /** The contact information (email or phone) of the member. */
    private String contact;
    /** The current fine balance of the member. */
    private double fineBalance = 0.0;
    /**
     * Constructs a new Member with the given details.
     *
     * @param memberId the unique ID of the member
     * @param name     the member's full name
     * @param contact  the member's contact information
     */

    public Member(String memberId, String name, String contact) {
        this.memberId = memberId;
        this.name = name;
        this.contact = contact;
    } 
    /**
     * Gets the member's ID.
     *
     * @return the member ID
     */
    public String getMemberId() { return memberId; }
    /**
     * Gets the member's name.
     *
     * @return the member name
     */
    public String getName() { return name; }
    /**
     * Gets the member's contact information.
     *
     * @return the contact information
     */
    public String getContact() { return contact; }
    /**
     * Gets the member's current fine balance.
     *
     * @return the fine balance
     */
    public double getFineBalance() { return fineBalance; }
    /**
     * Adds a fine amount to the member's balance.
     *
     * @param amount the fine amount to add
     */
    public void addFine(double amount) { this.fineBalance += amount; }
    /**
     * Pays part or all of the member's fine balance.
     * Fine balance will not go below zero.
     *
     * @param amount the amount to pay
     */
    public void payFine(double amount) {
        this.fineBalance -= amount;
        if (fineBalance < 0) fineBalance = 0;
    }
    /**
     * Sets the member's fine balance directly.
     * Useful for testing or administrative overrides.
     *
     * @param fineBalance the new fine balance
     */
	public void setfineBalance(double fineBalance) {
		// TODO Auto-generated method stub
		this.fineBalance=fineBalance;
	}
	/**
     * Returns a string representation of the member.
     * Format: "ID - Name (Contact), Fine: balance"
     *
     * @return formatted string with member details
     */
	@Override
    public String toString() {
        return memberId + " - " + name + " (" + contact + "), Fine: " + fineBalance;
    }
}
