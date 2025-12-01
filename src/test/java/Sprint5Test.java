import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class Sprint5Test {

    @Test
    public void testBookAndCDHaveDifferentRules() {
        Book book = new Book("Java", "Oracle", "B123");
        CD cd = new CD("Thriller", "MJ", "CD999");

        assertEquals(28, book.getLoanPeriodDays());
        assertEquals(10, book.getFinePerDay());

        assertEquals(7, cd.getLoanPeriodDays());
        assertEquals(20, cd.getFinePerDay());
    }

    @Test
    public void testFineCalculation_Book5DaysLate() {
        Book book = new Book("Test", "Author", "B1");
        book.borrow("M1");
        book.setDueDate(LocalDate.now().minusDays(5));

        int fine = FineCalculator.calculateFine(book, LocalDate.now());
        assertEquals(50, fine); // 5 × 10
    }

    @Test
    public void testFineCalculation_CD3DaysLate() {
        CD cd = new CD("Test CD", "Artist", "C1");
        cd.borrow("M1");
        cd.setDueDate(LocalDate.now().minusDays(3));

        int fine = FineCalculator.calculateFine(cd, LocalDate.now());
        assertEquals(60, fine); // 3 × 20
    }

    @Test
    public void testMixedFineCalculation() {
        Book book = new Book("B", "A", "1");
        CD cd = new CD("C", "A", "2");
        book.borrow("M1");
        cd.borrow("M1");
        book.setDueDate(LocalDate.now().minusDays(4));
        cd.setDueDate(LocalDate.now().minusDays(2));

        int total = FineCalculator.calculateFine(book, LocalDate.now()) +
                    FineCalculator.calculateFine(cd, LocalDate.now());

        assertEquals(80, total); // 4×10 + 2×20 = 80
    }
}