import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores all basic information about a single BookLoan.
 */
public class BookLoan {
    private BookLoanFactory bookLoanFactory;
    private int id;
    private int bookId;
    private int memberId;
    private LocalDate borrowDate;

    /**
     * Constructs a new BookLoan with given parameters only if it's being read from a file (because the ID is known).
     *
     * @param id the id (primary key) of the bookloan.
     * @param bookId  the id of the book that is being taken out.
     * @param memberId the id of the member that owns the book loan.
     * @param borrowDate the date that the book was taken out.
     */
    public BookLoan(int id, int bookId, int memberId, LocalDate borrowDate){
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
    }

    /**
     * Constructs a new BookLoan with given parameters with id = -1 to ensure it gets assigned a proper id before
     *  being added to the list.
     *
     * @param bookId the id of the book that is being taken out.
     * @param memberId the id of the member that owns the book loan.
     * @param borrowDate the date that the book was taken out.
     */
    public BookLoan(int bookId, int memberId, LocalDate borrowDate){
        this.id = -1;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
    }

    /**
     * Sets the dependencies that some functions in the bookloan may require.
     *
     * @param bookLoanFactory
     */
    public void setDependencies(BookLoanFactory bookLoanFactory){
        this.bookLoanFactory = bookLoanFactory;
    }


    /**
     * Getter for borrow date.
     *
     * @return LocalDate borrowed date.
     */
    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    /**
     * Getter for id.
     *
     * @return int id.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for id.
     *
     * @param id the id to set the bookloan id to.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for book id.
     *
     * @return int book id.
     */
    public int getBookId() {
        return bookId;
    }

    /**
     * Getter for member id.
     *
     * @return int member id.
     */
    public int getMemberId() {
        return memberId;
    }

    /**
     * Calculates the due date based on the borrow date.
     *
     * @return Date the book is due.
     */
    public LocalDate getDueDate(){ return borrowDate.plus(30, ChronoUnit.DAYS); }

    /**
     * Calculates the fine amount if a book is overdue.
     *
     * @return amount in pounds that the member owes.
     */
    public float getFine() {
        long daysOverdue = ChronoUnit.DAYS.between(getDueDate(), LocalDate.now());
        return daysOverdue * 0.1f;
    }

    /**
     * Calculates whether a book is overdue or not.
     *
     * @return boolean as to whether the book is overdue or not.
     */
    public boolean isOverdue(){
        LocalDate today = LocalDate.now();

        return today.isAfter(getDueDate());
    }
}
