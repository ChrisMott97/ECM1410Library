

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

    public boolean searchBook(String query){
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
            return true;
        }else{
            System.out.println("No books found.");
        }
        System.out.println();
        return false;
    }

    public void searchBook(){
        Scanner in = new Scanner(System.in);
        System.out.println("Search Book");

        System.out.print("Query: ");
        String query = in.next();
        System.out.println();

        if(searchBook(query)){
            //TODO Ask for other input or handle errors etc
        }
    }

    public void borrowBook(String book, String fName, String lName){
        List<Book> bookResult = Book.getBook(book);
        Member memberResult = Member.getMember(fName, lName);

        if(bookResult.size() == 1 && memberResult != null){
            BookLoan bookLoan = new BookLoan(bookResult.get(0).getId(), memberResult.getId(), LocalDate.now());
            if(bookLoan.add()){
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
        }else{
            //TODO Handle wrong inputs by returning true/false to input functions
        }
    }

    public void borrowBook(){
        Scanner in = new Scanner(System.in);
        System.out.println("Borrow Book");

        System.out.print("Book: ");
        String book = in.next();
        System.out.println();

        System.out.print("Member First Name: ");
        String fName = in.next();
        System.out.println();

        System.out.print("Member Last Name: ");
        String lName = in.next();
        System.out.println();

        borrowBook(book, fName, lName);
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
                System.out.printf("%7s %-9s %-30s %-35s %-5s %-17s %-3s\n","|","Loan ID", "Title", "Author", "Year", "Number of Copies", "Available");
                for (Book book : bookResults){
                    bookCount++;
                    System.out.printf("%7s %-9d %-30s %-35s %-5d %-17d %-3d\n",
                            "|",
                            book.getId(),
                            book.getTitle(),
                            Arrays.toString(book.getAuthor()),
                            book.getYear(),
                            book.getNumberCopies(),
                            book.getAvailable()
                    );
                }
                System.out.printf("%7s\n","|");
                System.out.printf("%7s Total books: %d\n\n", "|", bookCount);
            }
        }else{
            System.out.println("No member found");
        }
    }

    public void searchMember(){
        Scanner in = new Scanner(System.in);
        System.out.println("Search Member");
        System.out.print("First Name: ");
        String fName = in.next();
        System.out.println();
        System.out.print("Last Name: ");
        String lName = in.next();
        System.out.println();
        searchMember(fName, lName);
    }

    public void returnBook(int id) {
        BookLoan bookLoan = BookLoan.getBookLoanById(id);

        if (bookLoan != null) {
            LocalDate today = LocalDate.now();
            LocalDate borrowDate = bookLoan.getBorrowDate();
            LocalDate dueDate = borrowDate.plus(30, ChronoUnit.DAYS);

            if (today.isAfter(dueDate)) {
                long daysOverdue = ChronoUnit.DAYS.between(dueDate, today);
                float fine = daysOverdue * 0.1f;

                System.out.printf("OVERDUE! Fine: £%.2f\n", fine);
                System.out.println("Is the fine paid? (y/n)");

                if(yesNoDecision("Is the fine paid? ")){
                    Library.bookLoans.remove(bookLoan);
                    bookLoan.write("data/bookloans.txt");
                    System.out.println("Book successfully returned");
                }else{
                    System.out.println("Book cannot be returned until fine is paid");
                }

            } else {
                System.out.println("Return successful");
            }
        }else{
            System.out.println("This book hasn't been taken out, therefore cannot be returned!");
        }
        System.out.println();
    }

    public void addNewBook(String title, String[] authors, int year, int qty){
        Book book = new Book(title, authors, year, qty);
        if(book.add()){
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
        if(member.add()){
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

    public static boolean yesNoDecision(String message){
        System.out.println(message + "y/n");
        Scanner in = new Scanner(System.in);
        char userInput = in.next().charAt(0);
        switch (userInput){
            case 'y':
            case 'Y':
                return true;
            case 'n':
            case 'N':
                return false;
            default:
                System.out.println("Invalid input.");
                return yesNoDecision(message);

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
                    }else{
                        System.out.println("Quantity invalid.");
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
