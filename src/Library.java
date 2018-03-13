
import java.time.LocalDate;
import java.util.*;

public class Library {

    List<Book> books;
    List<Member>  members;
    List<BookLoan> bookLoans;

    String booksFileName;
    String membersFileName;
    String bookLoansFileName;

    BookFactory bookFactory;
    MemberFactory memberFactory;
    BookLoanFactory bookLoanFactory;

    public Library(String booksFileName, String membersFileName, String bookLoansFileName){

        this.booksFileName = booksFileName;
        this.membersFileName = membersFileName;
        this.bookLoansFileName = bookLoansFileName;

        bookFactory = new BookFactory();
        memberFactory = new MemberFactory();
        bookLoanFactory = new BookLoanFactory();

        books = bookFactory.read(booksFileName);
        members = memberFactory.read(membersFileName);
        bookLoans = bookLoanFactory.read(bookLoansFileName);

        bookFactory.setDependencies(books, memberFactory, bookLoanFactory);
        memberFactory.setDependencies(members, bookFactory, bookLoanFactory);
        bookLoanFactory.setDependencies(bookLoans, bookFactory, memberFactory);

    }

    public void showAllBooks(){
        displayBooks(books);
        System.out.println();
    }
    public void showAllMembers(){
        displayMembers(members);
        System.out.println();
    }
    public void showAllBookLoans(){
        displayBookLoans(bookLoans);
        //TODO show full book and member info for each loan
        System.out.println();
    }

    public boolean searchBook(String query){
        List<Book> results = bookFactory.getBook(query);
        if(!results.isEmpty()){
            displayBooks(results);
            System.out.println();
            return true;
        }else{
            System.out.println("No books found.");
        }
        System.out.println();
        return false;
    }

    public boolean borrowBook(String book, String fName, String lName){
        List<Book> bookResults = bookFactory.getBook(book);
        Member memberResult = memberFactory.getMember(fName, lName);

        if(memberResult == null) {
            System.out.println("Member does not exist.");
            return false;
        }
        if(bookResults.size() == 1){
            BookLoan bookLoan = new BookLoan(bookResults.get(0).getId(), memberResult.getId(), LocalDate.now());
            bookLoan.setDependencies(bookLoanFactory);

            if (bookLoanFactory.add(bookLoan)) {
                bookLoanFactory.write(bookLoansFileName);

                displayBookLoan(bookLoan);

                System.out.println();
                return true;
            }
        }else if(bookResults.size() > 1){
            Book bookResult = bookFactory.multipleBooks(bookResults);
            if(bookResult != null){
                BookLoan bookLoan = new BookLoan(bookResult.getId(), memberResult.getId(), LocalDate.now());
                if (bookLoanFactory.add(bookLoan)) {
                    bookLoanFactory.write(bookLoansFileName);

                    System.out.println("Book successfully borrowed: ");

                    displayBookLoan(bookLoan);

                    System.out.println();
                    return true;
                }
            }
        }else{
            System.out.println("No books found!");
        }
        return false;
    }

    public boolean searchMember(String fName, String lName){
        List<Member> members = memberFactory.getMembers(fName, lName);
        Member member = null;
        System.out.println(members.toString());

        if (members.size() > 1) {
            member = memberFactory.multipleMembers(members);
        } else {
            member = members.get(0);
        }

        if(member != null){
            displayMember(member);
            List<Book> bookResults = new ArrayList<>();
            for (BookLoan bookLoan : bookLoans){
                if (bookLoan.getMemberId() == member.getId()){
                   bookResults.add(bookFactory.getBookById(bookLoan.getBookId()));
                }
            }
            if(!bookResults.isEmpty()) {
                int bookCount = 0;
                System.out.printf("%7s\n", "|");
                System.out.printf("%7s %-9s %-30s %-35s %-5s %-17s %-17s %-10s\n", "|", "Loan ID", "Title", "Author", "Year", "Borrow Date", "Due Date", "Status");
                for (Book book : bookResults) {
                    bookCount++;

                    BookLoan loan = bookLoanFactory.getBookLoan(book.getId(), member.getId());

                    String warning = loan.isOverdue() ? "OVERDUE" : "On Loan";

                    System.out.printf("%7s %-9d %-30s %-35s %-5d %-17s %-17s %-10s\n",
                            "|",
                            book.getId(),
                            book.getTitle(),
                            Arrays.toString(book.getAuthor()),
                            book.getYear(),
                            loan.getBorrowDate().toString(),
                            loan.getDueDate().toString(),
                            warning

                    );
                }
                System.out.printf("%7s\n", "|");
                System.out.printf("%7s Total books: %d\n\n", "|", bookCount);
            }
            return true;
        }else{
            System.out.println("No member found");
        }
        return false;
    }

    public boolean returnBook(int id) {
        BookLoan bookLoan = bookLoanFactory.getBookLoanById(id);

        if (bookLoan != null) {

            if (bookLoan.isOverdue()) {

                System.out.printf("OVERDUE! Fine: £%.2f\n", bookLoan.getFine());

                if(yesNoDecision("Is the fine paid? ")){
                    bookLoans.remove(bookLoan);
                    bookLoanFactory.write(bookLoansFileName);

                    System.out.println("Book successfully returned");
                    return true;
                }else{
                    System.out.println("Book cannot be returned until fine is paid");
                }

            } else {
                bookLoans.remove(bookLoan);
                bookLoanFactory.write(bookLoansFileName);

                System.out.println("Book successfully returned");
                return true;
            }
        }else{
            System.out.println("This book hasn't been taken out, therefore cannot be returned!");
        }
        System.out.println();
        return false;
    }

    public boolean addNewBook(String title, String[] authors, int year, int qty){
        Book book = new Book(title, authors, year, qty);
        book.setDependencies(bookFactory);

        if(bookFactory.add(book)){
            bookFactory.write(booksFileName);

            System.out.println("Book successfully added to library: ");

            displayBook(book);

            System.out.println();
            return true;
        }
        return false;
    }

    public boolean addNewMember(String fName, String lName, LocalDate date){
        Member member = new Member(fName, lName, date);
        member.setDependencies(memberFactory);

        if(memberFactory.add(member)){
            memberFactory.write(membersFileName);

            System.out.println("Member successfully added: ");

            displayMember(member);

            System.out.println();
            return true;
        }
        return false;
    }

    public boolean changeQuantity(String query, int qty){
        List<Book> results = bookFactory.getBook(query);

        if(results.size() > 1) {
            Book book = bookFactory.multipleBooks(results);

            if(book != null){
                if(book.changeNumberCopies(qty)){

                    bookFactory.write(booksFileName);
                    displayBook(book);
                    System.out.println();

                    return true;
                }else{
                    System.out.println("Quantity invalid.");
                }
            }

        }else if(results.size() == 1){
            Book book = results.get(0);
            if(book.changeNumberCopies(qty)){
                bookFactory.write(booksFileName);

                System.out.println("Book quantity updated successfully!");
                displayBook(book);
                System.out.println();

                return true;
            }
        }else{
            System.out.println("No books found.");
            System.out.println();
        }
        return false;
    }

    public void searchBook(){
        Scanner in = new Scanner(System.in);
        System.out.println("Search Book");

        System.out.print(" |Query: ");
        String query = in.nextLine();
        System.out.println();

        if(!searchBook(query)){
            System.out.println("Could not complete search.");
        }
        if(yesNoDecision("Would you like to search again? ")){
            searchBook();
        }
    }

    public void searchMember(){
        Scanner in = new Scanner(System.in);
        System.out.println("Search Member");

        System.out.print(" |Full Name: ");
        String fName = in.nextLine();
        String lName = in.nextLine();
        System.out.println();

        if(!searchMember(fName, lName)){
            System.out.println("Could not complete search.");
        }
        if(yesNoDecision("Would you like to search again?")){
            searchMember();
        }
    }

    public void borrowBook(){
        Scanner in = new Scanner(System.in);
        System.out.println("Borrow Book");

        System.out.print(" |Book Title: ");
        String book = in.nextLine();
        System.out.println();

        System.out.print(" |Member Full Name: ");
        String fName = in.next();
        String lName = in.next();
        System.out.println();

        if(!borrowBook(book, fName, lName)){
            System.out.println("Could not borrow book.");
        }
        if(yesNoDecision("Would you like to borrow another?")){
            borrowBook();
        }
    }

    public void returnBook(){
        Scanner in = new Scanner(System.in);
        System.out.println("Return Book:");

        System.out.print(" |Book Loan ID: ");
        int query = in.nextInt();
        System.out.println();

        if(!returnBook(query)){
            System.out.println("Could not return book.");
        }
        if(yesNoDecision("Would you like to return another? ")){
            returnBook();
        }
    }

    public void addNewBook(){
        Scanner in = new Scanner(System.in);
        System.out.println("New Book");

        System.out.print(" |Title: ");
        String title = in.nextLine();
        System.out.println();

        System.out.print(" |Author (Separate multiple by comma): ");
        String authorsString = in.nextLine();
        String[] authors = authorsString.split(",");
        List<String> newAuthors = new ArrayList<>();
        for (String author : authors) {
            newAuthors.add(author.trim());
        }
        authors = newAuthors.toArray(authors);
        System.out.println();

        System.out.print(" |Year: ");
        int year = in.nextInt();
        System.out.println();

        System.out.print(" |Quantity: ");
        int qty = in.nextInt();
        System.out.println();

        if(!addNewBook(title, authors, year, qty)){
            System.out.println("Could not add book.");
        }
        if(yesNoDecision("Would you like to add another? ")){
            addNewBook();
        }
    }

    public void addNewMember(){
        Scanner in = new Scanner(System.in);
        System.out.println("New Member");

        System.out.print(" |First Name: ");
        String fName = in.nextLine();
        System.out.println();

        System.out.print(" |Last Name: ");
        String lName = in.nextLine();
        System.out.println();

        LocalDate dateJoined = LocalDate.now();
        System.out.printf(" |Date Joined: %s", dateJoined);

        if(!addNewMember(fName, lName, dateJoined)){
            System.out.println("Could not add member.");
        }
        if(yesNoDecision("Would you like to add another? ")) {
            addNewMember();
        }
    }

    public void changeQuantity(){
        Scanner in = new Scanner(System.in);
        System.out.println("Change Quantity");

        System.out.print(" |Book Title: ");
        String title = in.nextLine();
        System.out.println();

        System.out.print(" |Quantity Change: ");
        int qty = in.nextInt();
        System.out.println();

        if(!changeQuantity(title, qty)){
            System.out.println("Could not change quantity.");
        }
        if(yesNoDecision("Would you like to change another? ")) {
            changeQuantity();
        }
    }

    public void saveChanges(String booksFileName, String membersFileName, String bookLoansFileName){
        bookFactory.write(booksFileName);
        memberFactory.write(membersFileName);
        bookLoanFactory.write(bookLoansFileName);
    }

    public static boolean yesNoDecision(String message){
        System.out.println(message + "y/n");
        Scanner in = new Scanner(System.in);
        char userInput = in.nextLine().charAt(0);
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

    public void displayBook(Book book){
        System.out.printf("%-7s %-30s %-35s %-5s %-3s\n","ID", "Title", "Author(s)", "Year", "Number of Copies");
        System.out.printf("%-7d %-30s %-35s %-5d %-3d\n",
                book.getId(),
                book.getTitle(),
                Arrays.toString(book.getAuthor()),
                book.getYear(),
                book.getNumberCopies()
        );
    }
    public void displayBooks(List<Book> books){
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
    }
    public void displayMembers(List<Member> members){
        System.out.printf("%-7s %-12s %-12s %-15s\n","ID", "First Name", "Last Name", "Date Joined");
        for (Member member : members) {
            System.out.printf("%-7d %-12s %-12s %-15s\n",
                    member.getId(),
                    member.getFName(),
                    member.getLName(),
                    member.getDateJoin().toString()
            );
        }
    }
    public void displayMember(Member member){
        System.out.printf("%-7s %-12s %-12s %-15s\n","ID", "First Name", "Last Name", "Date Joined");
        System.out.printf("%-7d %-12s %-12s %-15s\n",
                member.getId(),
                member.getFName(),
                member.getLName(),
                member.getDateJoin().toString()
        );
    }
    public void displayBookLoans(List<BookLoan> bookLoans){
        System.out.printf("%-8s %-10s %-11s %-15s\n","ID", "Book ID", "Member ID", "Borrow Date");
        for (BookLoan bookLoan : bookLoans) {
            System.out.printf("%-8d %-10d %-11d %-15s\n",
                    bookLoan.getId(),
                    bookLoan.getBookId(),
                    bookLoan.getMemberId(),
                    bookLoan.getBorrowDate().toString()
            );
        }
    }
    public void displayBookLoan(BookLoan bookLoan){
        System.out.printf("%-8s %-10s %-11s %-15s\n","ID", "Book ID", "Member ID", "Borrow Date");
        System.out.printf("%-8d %-10d %-11d %-15s\n",
                bookLoan.getId(),
                bookLoan.getBookId(),
                bookLoan.getMemberId(),
                bookLoan.getBorrowDate().toString()
        );
    }
}
