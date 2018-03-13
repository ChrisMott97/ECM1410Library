import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains all functions involved in manipulating BookLoan objects.
 * Includes reading and writing to the storage text file.
 */
public class BookLoanFactory {
    private List<BookLoan> bookLoans;

    public BookFactory bookFactory;
    public MemberFactory memberFactory;


    /**
     * Used to set dependencies that the factory requires.
     * Does things that can't be done upon construction due to the fact other factories have to be constructed first.
     *
     * @param bookLoans list of book loans to pass by reference to the factory so the functions can use it.
     * @param bookFactory factory with functions mainly used to manipulate members but can be useful in other factories.
     * @param memberFactory factory with functions mainly used to manipulate members but can be useful in
     *                      other factories.
     */
    public void setDependencies(List<BookLoan> bookLoans, BookFactory bookFactory, MemberFactory memberFactory){
        this.bookFactory = bookFactory;
        this.memberFactory = memberFactory;
        this.bookLoans = bookLoans;
    }

    /**
     * Getter for book loans list.
     *
     * @return the full list of book loans.
     */
    public List<BookLoan> getBookLoans() {
        return bookLoans;
    }

    /**
     * Reads from the given file and creates a list of book loans to be accessible by the rest of the program.
     *
     * @param file the file path to the file that needs to be read.
     * @return the new list of book loans created from the text file.
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
     * Used to write the book loans list to the given text file.
     *
     * @param file the file to write to.
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
     * Counts the amount of loans a given member has out.
     *
     * @param memberId the id of the member in question.
     * @return number of loans the member has out.
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
     * Find a book loan by its Id.
     *
     * @param id the id of the book loan being looked for.
     * @return the book loan associated with the given id, or null if no such book loan exists.
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
     * Finds all book loans associated to a given book id.
     *
     * @param bookId the book id used to search for book loans.
     * @return a list of book loans associated with the book id.
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
     * Find all book loans associated to a given book id in a given list of book loans.
     *
     * @param bookId the book id to use to find the books.
     * @param loans the list to scan through.
     * @return list of book loans found.
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
     * Find all book loans associated to a given member id in a given list of book loans.
     *
     * @param memberId the member id to use to find book loans.
     * @return the list of book loans that are found.
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
     * Find a book loan given a book id and a member id.
     *
     * @param bookId the book id to use to search.
     * @param memberId the member id to use to search.
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
     * Adds a new instance of BookLoan to the book loans list.
     *
     * @param bookLoan the book loan to add.
     * @return boolean as to whether or not the function is successful.
     */
    public boolean add(BookLoan bookLoan){
        if(bookLoans.size() == 0 && bookLoan.getId() == -1) {
            bookLoan.setId(300000);
        } else {
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
