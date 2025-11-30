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
        
        
     // Load data from files
        memberService.loadMembers("src/main/resources/members.txt");
        bookService.loadBooks("src/main/resources/books.txt");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        boolean adminLoggedIn = false;

        while (running) {
            System.out.println("\n=== Library Management System ===");
            System.out.println("1. Admin Login");
            System.out.println("2. Admin Logout");
            System.out.println("3. Add Book");
            System.out.println("4. Search Book");
            System.out.println("5. Add Member");
            System.out.println("6. Borrow Book");
            System.out.println("7. Return Book");
            System.out.println("8. Pay Fine");
            System.out.println("9. Send Reminders");
            System.out.println("10. Unregister Member");
            System.out.println("11. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
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

                case 2:
                	authService.logout();
                    System.out.println("Logged out.");
                    break;

                case 3:
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

                case 4:
                    System.out.print("Search by title/author/ISBN: ");
                    String query = scanner.nextLine();
                    bookService.searchBook(query).forEach(System.out::println);
                    break;

                case 5:
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

                case 6:
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

                case 7:
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

                case 8:
                    System.out.print("Member ID: ");
                    String payId = scanner.nextLine();
                    System.out.print("Amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    memberService.payFine(payId, amount);
                    memberService.saveMembers("src/main/resources/members.txt");
                    System.out.println("Fine paid.");
                    break;

                case 9:
                    if (!authService.isLoggedIn()) { System.out.println("Admin login required."); break; }
                    //reminderService.sendReminders(bookService.getAllBooks(), memberService);
                    System.out.println("Reminders sent.");
                    break;

                case 10:
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

                case 11:
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        // Save data before exit
        memberService.saveMembers("src/main/resources/members.txt");
        bookService.saveBooks("src/main/resources/books.txt");

        scanner.close();
        System.out.println("Goodbye!");
    }
		
}
	