import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BookLoan {
    private int id;
    private int bookId;
    private int memberId;
    private Date borrowDate;

    public BookLoan(int id, int bookId, int memberId, Date borrowDate){
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
    }

    public BookLoan(int bookId, int memberId, Date borrowDate){
        this.id = -1;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
    }

    public static List<BookLoan> read(String file){
        BufferedReader br;
        String line;
        List<BookLoan> bookLoans = new ArrayList<>();

        try {
            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);

            while((line = br.readLine()) != null){
                String[] bookLoan = line.split(",");
                Date borrowDate = null;

                try {
                    borrowDate = new SimpleDateFormat("yyyy-MM-dd").parse(bookLoan[3]);
                } catch (ParseException e){
                    e.printStackTrace();
                }

                bookLoans.add(new BookLoan(
                        Integer.parseInt(bookLoan[0]),
                        Integer.parseInt(bookLoan[1]),
                        Integer.parseInt(bookLoan[2]),
                        borrowDate
                ));
            }

            br.close();

        }catch (IOException e){
            e.printStackTrace();
        }

        return bookLoans;
    }

    public static void write(String file){
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (BookLoan bookLoan : Library.bookLoans) {
                writer.printf("%d,%d,%d,%s\n",
                        bookLoan.getId(),
                        bookLoan.getBookId(),
                        bookLoan.getMemberId(),
                        dateFormat.format(bookLoan.getBorrowDate())
                );
            }
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Date getBorrowDate() {
        return borrowDate;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }
    public int getMemberId() {
        return memberId;
    }

    public static int countMemberLoans(int memberId){
        int count = 0;
        for (BookLoan bookLoan : Library.bookLoans) {
            if(bookLoan.getMemberId() == memberId){
                count += 1;
            }
        }
        return count;
    }

    public static BookLoan getBookLoanById(int id) {
        for (BookLoan bookLoan: Library.bookLoans) {
            if (bookLoan.getId() == id) {
                return bookLoan;
            }
        }
        return null;
    }

    public static List<BookLoan> getByBookId(int bookId){
        List<BookLoan> results = new ArrayList<>();

        for (BookLoan bookLoan : Library.bookLoans) {
            if(bookLoan.getBookId() == bookId){
                results.add(bookLoan);
            }
        }

        return results;
    }

    public static List<BookLoan> getByBookId(int bookId, List<BookLoan> loans){
        List<BookLoan> results = new ArrayList<>();

        for (BookLoan bookLoan : loans) {
            if(bookLoan.getBookId() == bookId){
                results.add(bookLoan);
            }
        }

        return results;
    }

    public static List<BookLoan> getByMemberId(int memberId){
        List<BookLoan> results = new ArrayList<>();

        for (BookLoan bookLoan : Library.bookLoans){
            if(bookLoan.getMemberId() == memberId){
                results.add(bookLoan);
            }
        }

        return results;
    }

    public static BookLoan getBookLoan(int bookId, int memberId) {
        List<BookLoan> loans = getByMemberId(memberId);
        if (!loans.isEmpty()) {
            List<BookLoan> loan = getByBookId(bookId, loans);
            if (!loan.isEmpty()) {
                return loan.get(0);
            }
        }
        return null;
    }

    public boolean addTo(){
        if(getId() == -1) {
            int bookLoanCount = Library.bookLoans.size();

            int newId = Library.bookLoans.get(Library.bookLoans.size() - 1).getId() + 1;
            setId(newId);
        }
        if(getBookLoanById(getId()) == null){
            if(getBookLoan(getBookId(), getMemberId()) == null){
                if(BookLoan.countMemberLoans(getMemberId()) < 5){
                    if(Book.getBookById(getBookId()).getAvailable() > 0){
                        Library.bookLoans.add(this);
                        return true;
                    }
                    else{
                        System.out.println("This book has 0 copies available!");
                    }
                }else{
                    System.out.println("Member already has the maximum of 5 book loans!");
                }
            }else{
                System.out.println("Member already owns book!");
            }
        }else{
            System.out.println("Book Loan already exists!");
            //TODO handle better
        }
        return false;
    }
}
