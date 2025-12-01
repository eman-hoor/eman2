import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
//import io.github.cdimascio.dotenv.Dotenv;

public class LibraryApp {

	public static void main(String[] args) {
		//Dotenv dotenv = Dotenv.load();
       // String emailUser = dotenv.get("EMAIL_USER");
        //String emailPass = dotenv.get("EMAIL_PASS");

        // Initialize services
        //EmailService emailService = new EmailService(emailUser, emailPass);
       // ReminderService reminderService = new ReminderService(emailService);
		MemberService memberService = new MemberService();
        BookService bookService = new BookService();
        AuthService authService = new AuthService(); // multiple admins supported
        CDService cdService = new CDService();
        
     // Load data from files
        memberService.loadMembers("src/main/resources/members.txt");
        bookService.loadBooks("src/main/resources/books.txt");
        try {
            cdService.loadCDs("src/main/resources/cds.txt");
        } catch (IOException e) {
            System.out.println("Could not load CDs: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        boolean adminLoggedIn = false;

        while (running) {
        	System.out.println("\n=== Library Management System ===");

        	System.out.println("---- Admin Operations ----");
        	System.out.println("1.  Admin Login");
        	System.out.println("2.  Admin Logout");

        	System.out.println("\n---- Book Operations ----");
        	System.out.println("3.  Add Book");
        	System.out.println("4.  Search Book");
        	System.out.println("5.  Borrow Book");
        	System.out.println("6.  Return Book");

        	System.out.println("\n---- CD Operations ----");
        	System.out.println("7. Add CD");
        	System.out.println("8. Search CD");
        	System.out.println("9. Borrow CD");
        	System.out.println("10. Return CD");

        	System.out.println("\n---- Member Operations ----");
        	System.out.println("11.  Add Member");
        	System.out.println("12.  Pay Fine");
        	System.out.println("13. Unregister Member");

        	System.out.println("\n---- Notifications & Reports ----");
        	System.out.println("14.  Send Reminders");
        	System.out.println("15. View Overdue Items Report");

        	System.out.println("\n---- System ----");
        	System.out.println("16. Exit");

        	System.out.print("\nChoose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1://Admin Login
                	System.out.print("Username: ");
                    String u = scanner.nextLine();
                    System.out.print("Password: ");
                    String p = scanner.nextLine();
                    if (authService.login(u, p)) {
                        System.out.println("Login successful as " + authService.getCurrentAdmin().getUsername());
                    } else {
                        System.out.println("Invalid credentials.");
                    }
                    break;

                case 2://Admin Logout
                	authService.logout();
                    System.out.println("Logged out.");
                    break;

                case 3://Add Book
                    if (!authService.isLoggedIn()) { System.out.println("Admin login required."); break; }
                    System.out.print("Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Author: ");
                    String author = scanner.nextLine();
                    System.out.print("ISBN: ");
                    String isbn = scanner.nextLine();
                    bookService.addBook(new Book(title, author, isbn));
                    bookService.saveBooks("src/main/resources/books.txt");
                    System.out.println("Book added.");
                    break;

                case 4://Search Book
                    System.out.print("Search by title/author/ISBN: ");
                    String query = scanner.nextLine();
                    bookService.searchBook(query).forEach(System.out::println);
                    break;

                case 11://Add Member
                    System.out.print("Member ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    memberService.addMember(new Member(id, name, email));
                    memberService.saveMembers("src/main/resources/members.txt");     

                    System.out.println("Member added.");
                    break;

                case 5://Borrow Book
                    System.out.print("ISBN: ");
                    String borrowIsbn = scanner.nextLine();
                    System.out.print("Member ID: ");
                    String memberId = scanner.nextLine();
                    Member m = memberService.findById(memberId);
                    if (m != null) {
                    	try {
                            
                        bookService.borrowBook(m, borrowIsbn);
                        System.out.println("Book borrowed.");                        
                        bookService.saveBooks("src/main/resources/books.txt");
                    	}
                    	catch (IllegalArgumentException e) {
                            System.out.println("Sorry, no book found with ISBN " + borrowIsbn);
                        } catch (IllegalStateException e) {
                            // Differentiate based on message
                            String msg = e.getMessage();
                            if (msg.contains("already borrowed")) {
                                System.out.println("Sorry, that book is already borrowed.");
                            } else if (msg.contains("unpaid fines")) {
                                System.out.println("Member has unpaid fines. Please pay before borrowing.");
                            } else {
                                System.out.println("Error: " + msg);
                            }
                        }
                    } else {
                        System.out.println("Member not found.");
                    }
                    break;

                case 6://Return Book
                    System.out.print("ISBN: ");
                    String returnIsbn = scanner.nextLine();
                    Book b = bookService.findBook(returnIsbn);
                    if (b != null) {
                        if (b.isBorrowed()) {
                            try {
                        Member borrower = memberService.findById(b.getBorrowedBy());
                        bookService.returnBook(borrower, returnIsbn);                        
                        bookService.saveBooks("src/main/resources/books.txt");
                        System.out.println("Book returned.");
                    	} catch (IllegalArgumentException e) {
                            System.out.println("Sorry, no book found with ISBN " + returnIsbn);
                        } catch (IllegalStateException e) {
                            System.out.println("Error: " + e.getMessage());
                        } catch (Exception e) {
                            System.out.println("Unexpected error while returning book: " + e.getMessage());
                        }
                    } else {
                        System.out.println("This book is not currently borrowed.");
                    }
            }
                 else {
                    System.out.println("Sorry, no book found with ISBN " + returnIsbn);
                }
                    break;

                case 12:// pay fine
                    System.out.print("Member ID: ");
                    String payId = scanner.nextLine();
                    System.out.print("Amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    memberService.payFine(payId, amount);
                    memberService.saveMembers("src/main/resources/members.txt");
                    System.out.println("Fine paid.");
                    break;

                case 14://Send Reminders
                    if (!authService.isLoggedIn()) { System.out.println("Admin login required."); break; }
                    //reminderService.sendReminders(bookService.getAllBooks(), memberService);
                    System.out.println("Reminders sent.");
                    break;

                case 13:// unregister member
                	if (!authService.isLoggedIn()) {
                        System.out.println("Admin login required.");
                        break;
                    }
                    System.out.print("Member ID: ");
                    String unregisterId = scanner.nextLine();
                    try {
                        memberService.unregisterMember(unregisterId, bookService);
                        memberService.saveMembers("src/main/resources/members.txt");
                        System.out.println("Member unregistered and file updated.");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 15://reports
                	System.out.println("\n--- Overdue Items Report ---");
                    LocalDate today = LocalDate.now();
                    // Books
                    for (Book book : bookService.getAllBooks()) {
                        if (book.isBorrowed() && book.getDueDate() != null 
                            && book.getDueDate().isBefore(today)) {
                        	int overdueDays = (int) ChronoUnit.DAYS.between(book.getDueDate(), today);
                            FineStrategy fineStrategy = new BookFineStrategy();
                            int fine = fineStrategy.calculateFine(overdueDays);
                            Member borrower = memberService.findById(book.getBorrowedBy());
                            System.out.println("Book: " + book.getTitle() 
                            + " | Borrower: " + (borrower != null ? borrower.getName() : "Unknown")
                            + " | Due: " + book.getDueDate()
                            + " | Overdue Days: " + overdueDays
                            + " | Fine: " + fine + " NIS");
                        }
                    }

                    // CDs
                    for (CD cd : cdService.getAllCDs()) {
                        if (cd.isBorrowed() && cd.getDueDate() != null 
                            && cd.getDueDate().isBefore(today)) {
                        	int overdueDays = (int) ChronoUnit.DAYS.between(cd.getDueDate(), today);
                            FineStrategy fineStrategy = new CDFineStrategy();
                            int fine = fineStrategy.calculateFine(overdueDays);
                            Member borrower = memberService.findById(cd.getBorrowedBy());
                            System.out.println("CD: " + cd.getTitle() 
                            + " | Borrower: " + (borrower != null ? borrower.getName() : "Unknown")
                            + " | Due: " + cd.getDueDate()
                            + " | Overdue Days: " + overdueDays
                            + " | Fine: " + fine + " NIS");
                        }
                    }

                    System.out.println("--- End of Report ---");
                    
                    break;

                case 16://exit
                    running = false;
                    break;
                    
                    
                case 7: // Add CD
                	if (!authService.isLoggedIn()) { System.out.println("Admin login required."); break; }
                    System.out.print("CD ID: ");
                    String cdId = scanner.nextLine();
                    System.out.print("Title: ");
                    String cdTitle = scanner.nextLine();
                    System.out.print("Artist: ");
                    String artist = scanner.nextLine();
                    cdService.addCD(new CD(cdId, cdTitle, artist));
                    try {
                        cdService.saveCDs("src/main/resources/cds.txt");
                        System.out.println("CD added and saved.");
                    } catch (IOException e) {
                        System.out.println("Error saving CDs: " + e.getMessage());
                    }
                    break;

                case 8: // Search CD
                    System.out.print("Search by title/artist/ID: ");
                    String cdQuery = scanner.nextLine();
                   // bookService.searchBook(cdQuery).forEach(System.out::println);

                    cdService.getAllCDs().stream()
                             .filter(cd -> cd.getTitle().contains(cdQuery) 
                                        || cd.getArtist().contains(cdQuery) 
                                        || cd.getId().equals(cdQuery))
                             .forEach(System.out::println);
                    break;
                    
                    
                case 9: // Borrow CD
                    System.out.print("CD ID: ");
                    String borrowCdId = scanner.nextLine();
                    System.out.print("Member ID: ");
                    String cdMemberId = scanner.nextLine();
                    Member cdMember = memberService.findById(cdMemberId);
                    if (cdMember != null) {
                        try {
                            cdService.borrowCD(cdMember, borrowCdId);
                            System.out.println("CD borrowed.");
                            cdService.saveCDs("src/main/resources/cds.txt");
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Member not found.");
                    }
                    break;
                case 10: // Return CD
                	System.out.print("CD ID: ");
                    String returnCdId = scanner.nextLine();
                    System.out.print("Member ID: ");
                    String returnMemberId = scanner.nextLine();
                    Member returnMember = memberService.findById(returnMemberId);

                    if (returnMember != null) {
                        try {
                            cdService.returnCD(returnMember, returnCdId);
                            cdService.saveCDs("src/main/resources/cds.txt");
                            System.out.println("CD returned.");
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Member not found.");
                    }
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        // Save data before exit
        memberService.saveMembers("src/main/resources/members.txt");
        bookService.saveBooks("src/main/resources/books.txt");
        try {
			cdService.saveCDs("src/main/resources/cds.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


        scanner.close();
        System.out.println("Goodbye!");
    }
		
}
	
