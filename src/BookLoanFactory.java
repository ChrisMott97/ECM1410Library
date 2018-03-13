import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookLoanFactory {
    private List<BookLoan> bookLoans;

    public BookFactory bookFactory;
    public MemberFactory memberFactory;


    /**
     * @param bookLoans
     * @param bookFactory
     * @param memberFactory
     */
    public void setDependencies(List<BookLoan> bookLoans, BookFactory bookFactory, MemberFactory memberFactory){
        this.bookFactory = bookFactory;
        this.memberFactory = memberFactory;
        this.bookLoans = bookLoans;
    }

    /**
     * @return
     */
    public List<BookLoan> getBookLoans() {
        return bookLoans;
    }

    /**
     * @param file
     * @return
     */
    public List<BookLoan> read(String file){
        BufferedReader br;
        String line;
        List<BookLoan> bookLoans = new ArrayList<>();

        try {
            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);

            while((line = br.readLine()) != null){
                String[] bookLoan = line.split(",");

                LocalDate borrowDate = LocalDate.parse(bookLoan[3]);

                bookLoans.add(new BookLoan(
                        Integer.parseInt(bookLoan[0]),
                        Integer.parseInt(bookLoan[1]),
                        Integer.parseInt(bookLoan[2]),
                        borrowDate
                ));
            }
            for (BookLoan bookLoan : bookLoans) {
                bookLoan.setDependencies(this);
            }

            br.close();

        }catch (IOException e){
            e.printStackTrace();
        }

        return bookLoans;
    }

    /**
     * @param file
     */
    public void write(String file){
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            for (BookLoan bookLoan : bookLoans) {
                writer.printf("%d,%d,%d,%s\n",
                        bookLoan.getId(),
                        bookLoan.getBookId(),
                        bookLoan.getMemberId(),
                        bookLoan.getBorrowDate()
                );
            }
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * @param memberId
     * @return
     */
    public int countMemberLoans(int memberId){
        int count = 0;
        for (BookLoan bookLoan : bookLoans) {
            if(bookLoan.getMemberId() == memberId){
                count += 1;
            }
        }
        return count;
    }

    /**
     * @param id
     * @return
     */
    public BookLoan getBookLoanById(int id) {
        for (BookLoan bookLoan: bookLoans) {
            if (bookLoan.getId() == id) {
                return bookLoan;
            }
        }
        return null;
    }

    /**
     * @param bookId
     * @return
     */
    public List<BookLoan> getByBookId(int bookId){
        List<BookLoan> results = new ArrayList<>();

        for (BookLoan bookLoan : bookLoans) {
            if(bookLoan.getBookId() == bookId){
                results.add(bookLoan);
            }
        }

        return results;
    }

    /**
     * @param bookId
     * @param loans
     * @return
     */
    public List<BookLoan> getByBookId(int bookId, List<BookLoan> loans){
        List<BookLoan> results = new ArrayList<>();

        for (BookLoan bookLoan : loans) {
            if(bookLoan.getBookId() == bookId){
                results.add(bookLoan);
            }
        }

        return results;
    }

    /**
     * @param memberId
     * @return
     */
    public List<BookLoan> getByMemberId(int memberId){
        List<BookLoan> results = new ArrayList<>();

        for (BookLoan bookLoan : bookLoans){
            if(bookLoan.getMemberId() == memberId){
                results.add(bookLoan);
            }
        }

        return results;
    }

    /**
     * @param bookId
     * @param memberId
     * @return
     */
    public BookLoan getBookLoan(int bookId, int memberId) {
        List<BookLoan> loans = getByMemberId(memberId);
        if (!loans.isEmpty()) {
            List<BookLoan> loan = getByBookId(bookId, loans);
            if (!loan.isEmpty()) {
                return loan.get(0);
            }
        }
        return null;
    }

    /**
     * @param bookLoan
     * @return
     */
    public boolean add(BookLoan bookLoan){
        if(bookLoan.getId() == -1) {
            int bookLoanCount = bookLoans.size();

            int newId = bookLoans.get(bookLoans.size() - 1).getId() + 1;
            bookLoan.setId(newId);
        }
        if(getBookLoanById(bookLoan.getId()) == null){
            if(getBookLoan(bookLoan.getBookId(), bookLoan.getMemberId()) == null){
                if(countMemberLoans(bookLoan.getMemberId()) < 5){
                    if(bookFactory.getBookById(bookLoan.getBookId()).getAvailable() > 0){
                        bookLoans.add(bookLoan);
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
        System.out.println();
        return false;
    }
}
