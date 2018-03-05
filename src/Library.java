

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Library {

    static List<Book> books;
    static List<Member>  members;
    static List<BookLoan> bookLoans;

    public Library(String booksFileName, String membersFileName, String bookLoansFileName){
        books = Book.read(booksFileName);
        members = Member.read(membersFileName);
        bookLoans = BookLoan.read(bookLoansFileName);
    }

    public void showAllBooks(){
        System.out.printf("%-7s %-30s %-35s %-5s %-3s\n","ID", "Title", "Author(s)", "Year", "Number of Copies");
        for (Book book : books) {
            System.out.printf("%-7d %-30s %-35s %-5d %-3d\n",
                    book.getId(),
                    book.getTitle(),
                    Arrays.toString(book.getAuthor()),
                    book.getYear(),
                    book.getNumberCopies()
            );
        }
        System.out.println();
    }
    public void showAllMembers(){
        System.out.printf("%-7s %-12s %-12s %-15s\n","ID", "First Name", "Last Name", "Date Joined");
        for (Member member : members) {
            System.out.printf("%-7d %-12s %-12s %-15s\n",
                    member.getId(),
                    member.getFName(),
                    member.getLName(),
                    member.getDateJoin().toString()
            );
        }
        System.out.println();
    }
    public void showAllBookLoans(){
        System.out.printf("%-8s %-10s %-11s %-15s\n","ID", "Book ID", "Member ID", "Borrow Date");
        for (BookLoan bookLoan : bookLoans) {
            System.out.printf("%-8d %-10d %-11d %-15s\n",
                    bookLoan.getId(),
                    bookLoan.getBookId(),
                    bookLoan.getMemberId(),
                    bookLoan.getBorrowDate().toString()
            );
        }
        System.out.println();
    }

    public void searchBook(String query){
        List<Book> results = Book.getBook(query);
        if(!results.isEmpty()){
            System.out.printf("%-7s %-30s %-35s %-5s %-3s\n","ID", "Title", "Author", "Year", "Number of Copies");
            for (Book book : results) {
                System.out.printf("%-7s %-30s %-35s %-5s %-3s\n",
                        book.getId(),
                        book.getTitle(),
                        Arrays.toString(book.getAuthor()),
                        book.getYear(),
                        book.getNumberCopies()
                );
            }
        }else{
            System.out.println("No books found.");
        }
        System.out.println();
    }

    public void borrowBook(String book, String fName, String lName){
        List<Book> bookResult = Book.getBook(book);
        Member memberResult = Member.getMember(fName, lName);

        if(bookResult.size() == 1 && memberResult != null){
            BookLoan bookLoan = new BookLoan(bookResult.get(0).getId(), memberResult.getId(), LocalDate.now());
            if(bookLoan.addTo()){
                BookLoan.write("data/bookloans.txt");

                System.out.println("Book successfully borrowed: ");
                System.out.printf("%-8s %-10s %-11s %-15s\n","ID", "Book ID", "Member ID", "Borrow Date");
                System.out.printf("%-8d %-10d %-11d %-15s\n",
                        bookLoan.getId(),
                        bookLoan.getBookId(),
                        bookLoan.getMemberId(),
                        bookLoan.getBorrowDate()
                );
                System.out.println();
            }
        }
    }

    public void searchMember(String fName, String lName){
        Member member = Member.getMember(fName, lName);
        if(member != null){
            System.out.printf("%-7s %-30s %-35s %-8s\n","ID", "First Name", "Last Name", "Date Joined");
            System.out.printf("%-7s %-30s %-35s %-8s\n",
                    member.getId(),
                    member.getFName(),
                    member.getLName(),
                    member.getDateJoin()
            );
            List<Book> bookResults = new ArrayList<>();
            for (BookLoan bookLoan : bookLoans){
                if (bookLoan.getMemberId() == member.getId()){
                   bookResults.add(Book.getBookById(bookLoan.getBookId()));
                }
            }
            if(!bookResults.isEmpty()) {
                System.out.printf("%7s\n","|");
                System.out.printf("%7s %-9s %-30s %-35s %-5s %-3s\n","|","Loan ID", "Title", "Author", "Year", "Number of Copies");
                for (Book book : bookResults){
                    System.out.printf("%7s %-9s %-30s %-35s %-5s %-3s\n",
                            "|",
                            book.getId(),
                            book.getTitle(),
                            Arrays.toString(book.getAuthor()),
                            book.getYear(),
                            book.getNumberCopies()
                    );
                }
                System.out.println();
            }
        }else{
            System.out.println("No member found");
        }
    }

    public void returnBook(int id) {
        BookLoan bookLoan = BookLoan.getBookLoanById(id);

        Calendar cal = Calendar.getInstance();

        LocalDate today = LocalDate.now();
        LocalDate borrowDate = bookLoan.getBorrowDate();
        LocalDate returnDate = borrowDate.plus(30, ChronoUnit.DAYS);

        System.out.println(borrowDate);
        System.out.println(returnDate);

        if (bookLoan != null) {
            if (today.isAfter(returnDate)) {
                System.out.println("YOU PAY!!");
            } else {
                System.out.println("YOU NO PAY!!");
            }
        }
    }

    public void addNewBook(String title, String[] authors, int year, int qty){
        Book book = new Book(title, authors, year, qty);
        if(book.addTo()){
            Book.write("data/books.txt", books);

            System.out.println("Book successfully added to library: ");
            System.out.printf("%-7s %-30s %-35s %-5s %-3s\n","ID", "Title", "Author(s)", "Year", "Number of Copies");
            System.out.printf("%-7d %-30s %-35s %-5d %-3d\n",
                book.getId(),
                book.getTitle(),
                Arrays.toString(book.getAuthor()),
                book.getYear(),
                book.getNumberCopies()
            );
            System.out.println();
        }
    }

    public void addNewMember(String fName, String lName, LocalDate date){
        LocalDate dateNow = LocalDate.now();
        Member member = new Member(fName, lName, dateNow);
        if(member.addTo()){
            Member.write("data/members.txt");

            System.out.println("Member successfully added: ");
            System.out.printf("%-7s %-12s %-12s %-15s\n","ID", "First Name", "Last Name", "Date Joined");
            System.out.printf("%-7d %-12s %-12s %-15s\n",
                    member.getId(),
                    member.getFName(),
                    member.getLName(),
                    member.getDateJoin()
            );
            System.out.println();
        }
    }
}
