import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BookLoan {
    private BookLoanFactory bookLoanFactory;
    private int id;
    private int bookId;
    private int memberId;
    private LocalDate borrowDate;

    /**
     * @param id
     * @param bookId
     * @param memberId
     * @param borrowDate
     */
    public BookLoan(int id, int bookId, int memberId, LocalDate borrowDate){
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
    }

    /**
     * @param bookId
     * @param memberId
     * @param borrowDate
     */
    public BookLoan(int bookId, int memberId, LocalDate borrowDate){
        this.id = -1;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
    }

    /**
     * @param bookLoanFactory
     */
    public void setDependencies(BookLoanFactory bookLoanFactory){
        this.bookLoanFactory = bookLoanFactory;
    }


    /**
     * @return
     */
    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    /**
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return
     */
    public int getBookId() {
        return bookId;
    }

    /**
     * @return
     */
    public int getMemberId() {
        return memberId;
    }

    /**
     * @return
     */
    public LocalDate getDueDate(){ return borrowDate.plus(30, ChronoUnit.DAYS); }

    /**
     * @return
     */
    public float getFine() {
        long daysOverdue = ChronoUnit.DAYS.between(getDueDate(), LocalDate.now());
        return daysOverdue * 0.1f;
    }

    /**
     * @return
     */
    public boolean isOverdue(){
        LocalDate today = LocalDate.now();

        return today.isAfter(getDueDate());
    }
}
