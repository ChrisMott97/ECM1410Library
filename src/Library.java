

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Library {

    static List<Book> books;
    static List<Member>  members;
    static List<BookLoan> bookLoans;

    String booksFileName;
    String membersFileName;
    String bookLoansFileName;

    public Library(String booksFileName, String membersFileName, String bookLoansFileName){
        books = Book.read(booksFileName);
        this.booksFileName = booksFileName;

        members = Member.read(membersFileName);
        this.membersFileName = membersFileName;

        bookLoans = BookLoan.read(bookLoansFileName);
        this.bookLoansFileName = bookLoansFileName;
    }

    public void showAllBooks(){
        System.out.printf("%-7s %-30s %-35s %-5s %-17s %-3s\n","ID", "Title", "Author(s)", "Year", "Number of Copies", "Available");
        for (Book book : books) {
            System.out.printf("%-7d %-30s %-35s %-5d %-17d %-3s\n",
                    book.getId(),
                    book.getTitle(),
                    Arrays.toString(book.getAuthor()),
                    book.getYear(),
                    book.getNumberCopies(),
                    book.getAvailable()
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
        //TODO show full book and member info for each loan
        System.out.println();
    }

    public void searchBook(String query){
        List<Book> results = Book.getBook(query);
        if(!results.isEmpty()){
            System.out.printf("%-7s %-30s %-35s %-5s %-17s %-3s\n","ID", "Title", "Author", "Year", "Number of Copies", "Available");
            for (Book book : results) {
                System.out.printf("%-7d %-30s %-35s %-5d %-17d %-3d\n",
                        book.getId(),
                        book.getTitle(),
                        Arrays.toString(book.getAuthor()),
                        book.getYear(),
                        book.getNumberCopies(),
                        book.getAvailable()
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
                BookLoan.write(bookLoansFileName);

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
                int bookCount = 0;
                System.out.printf("%7s\n","|");
                System.out.printf("%7s %-9s %-30s %-35s %-5s %-17s %-17s %-10s\n","|","Loan ID", "Title", "Author", "Year", "Borrow Date", "Due Date", "Status");
                for (Book book : bookResults){
                    bookCount++;

                    BookLoan loan = BookLoan.getBookLoan(book.getId(), member.getId());

                    LocalDate dueDate = loan.getBorrowDate().plus(30, ChronoUnit.DAYS);
                    LocalDate today = LocalDate.now();
                    String warning = "On Loan";
                    if (today.isAfter(dueDate)){
                        warning = "OVERDUE";
                    }


                    System.out.printf("%7s %-9d %-30s %-35s %-5d %-17s %-17s %-10s\n",
                            "|",
                            book.getId(),
                            book.getTitle(),
                            Arrays.toString(book.getAuthor()),
                            book.getYear(),
                            loan.getBorrowDate().toString(),
                            dueDate.toString(),
                            warning

                    );
                }
                System.out.printf("%7s\n","|");
                System.out.printf("%7s Total books: %d\n\n", "|", bookCount);
            }
        }else{
            System.out.println("No member found");
        }
    }

    public void returnBook(int id) {
        BookLoan bookLoan = BookLoan.getBookLoanById(id);

        LocalDate today = LocalDate.now();
        LocalDate borrowDate = bookLoan.getBorrowDate();
        LocalDate dueDate = borrowDate.plus(30, ChronoUnit.DAYS);


        if (bookLoan != null) {
            if (today.isAfter(dueDate)) {
                long daysOverdue = ChronoUnit.DAYS.between(dueDate, today);
                float fine = daysOverdue * 0.1f;

                System.out.printf("OVERDUE! Fine: £%.2f\n", fine);
                System.out.println("Is the fine paid? (y/n)");

                Scanner in = new Scanner(System.in);
                String userInput = in.next();
                switch (userInput) {
                    case "y":
                    case "Y":
                        Library.bookLoans.remove(bookLoan);
                        bookLoan.write("data/bookloans.txt");
                        System.out.println("Book successfully returned");
                        break;
                    case "n":
                    case "N":
                        System.out.println("Book cannot be returned until fine is paid");
                        break;
                    default:
                        System.out.println("Invalid input, book remains overdue");
                }
            } else {
                System.out.println("Return successful");
            }
        }
    }

    public void addNewBook(String title, String[] authors, int year, int qty){
        Book book = new Book(title, authors, year, qty);
        if(book.addTo()){
            Book.write(booksFileName);

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
        Member member = new Member(fName, lName, date);
        if(member.addTo()){
            Member.write(membersFileName);

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

    public void changeQuantity(String query, int qty){
        List<Book> results = Book.getBook(query);
        if(results.size() > 1) {
            System.out.printf("%-7s %-30s %-35s %-5s %-17s %-3s\n", "ID", "Title", "Author", "Year", "Number of Copies", "Available");
            for (Book book : results) {
                System.out.printf("%-7d %-30s %-35s %-5d %-17d %-3d\n",
                        book.getId(),
                        book.getTitle(),
                        Arrays.toString(book.getAuthor()),
                        book.getYear(),
                        book.getNumberCopies(),
                        book.getAvailable()
                );
            }
            System.out.printf("%d results found. Please enter ID of the one you want to change:", results.size());
            System.out.println();
            boolean found = false;
            while(!found){
                Scanner in = new Scanner(System.in);
                String id = in.next();
                Book book = Book.getBookById(Integer.parseInt(id));
                if(book != null){
                    found = true;
                    if(book.changeNumberCopies(qty)){
                        Book.write(booksFileName);
                        System.out.println("Book quantity updated successfully!");
                        System.out.printf("%-7s %-30s %-35s %-5s %-17s %-3s\n", "ID", "Title", "Author", "Year", "Number of Copies", "Available");
                        System.out.printf("%-7d %-30s %-35s %-5d %-17d %-3d\n",
                                book.getId(),
                                book.getTitle(),
                                Arrays.toString(book.getAuthor()),
                                book.getYear(),
                                book.getNumberCopies(),
                                book.getAvailable()
                        );
                        System.out.println();
                    }
                }else{
                    System.out.println("Book ID not found, please try again.");
                }
                System.out.println();
            }
        }else if(results.size() == 1){
            Book book = results.get(0);
            if(book.changeNumberCopies(qty)){
                Book.write(booksFileName);

                System.out.println("Book quantity updated successfully!");
                System.out.printf("%-7s %-30s %-35s %-5s %-17s %-3s\n", "ID", "Title", "Author", "Year", "Number of Copies", "Available");
                System.out.printf("%-7d %-30s %-35s %-5d %-17d %-3d\n",
                        book.getId(),
                        book.getTitle(),
                        Arrays.toString(book.getAuthor()),
                        book.getYear(),
                        book.getNumberCopies(),
                        book.getAvailable()
                );
                System.out.println();
            }
        }else{
            System.out.println("No books found.");
            System.out.println();
        }
    }
}
