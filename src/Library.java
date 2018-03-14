import java.time.LocalDate;
import java.util.*;

/**
 * Library management system that maintains books, members, and bookloans.
 *
 * @author Chris Mott
 * @author Örs Barkanyi
 */
public class Library {

    private List<Book> books;
    private List<Member> members;
    private List<BookLoan> bookLoans;

    private String booksFileName;
    private String membersFileName;
    private String bookLoansFileName;

    private BookFactory bookFactory;
    private MemberFactory memberFactory;
    private BookLoanFactory bookLoanFactory;

    /**
     * Constructor for Library that sets up filepath variables, local storage variables for all books, members and
     *  loans, and factories.
     *
     * @param booksFileName location of the .txt file that stores the books.
     * @param membersFileName location of the .txt file that stores the members.
     * @param bookLoansFileName location of the .txt file that stores the bookloans.
     */
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

    /**
     * Prints all books in a table.
     */
    public void showAllBooks(){
        displayBooks(books);
        System.out.println();
    }

    /**
     * Prints all members in a table.
     */
    public void showAllMembers(){
        displayMembers(members);
        System.out.println();
    }

    /**
     * Prints all book loans in a table.
     */
    public void showAllBookLoans(){
        displayBookLoans(bookLoans);
        System.out.println();
    }

    /**
     * Searches for a book based on its title and returns all full and partial matches.
     *
     * @param query the full or partial title of the book to search for.
     * @return whether the search was successful or not.
     */
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

    /**
     * Used to allow a member to borrow a book.
     *
     * @param book the full or partial match for the book to be borrowed.
     * @param fName the first name of the member that's borrowing.
     * @param lName the last name of the member that's borrowing.
     * @return
     */
    public boolean borrowBook(String book, String fName, String lName){
        List<Book> bookResults = bookFactory.getBook(book);
        List<Member> memberResults = memberFactory.getMembers(fName, lName);

        if(memberResults.isEmpty()) {
            System.out.println("Member does not exist.");

            return false;
        }
        if(bookResults.size() == 1){
            if(memberResults.size() == 1){
                //If search returned a single member and a single book.
                BookLoan bookLoan = new BookLoan(bookResults.get(0).getId(), memberResults.get(0).getId(), LocalDate.now());
                bookLoan.setDependencies(bookLoanFactory);

                if (bookLoanFactory.add(bookLoan)) {
                    System.out.println("Book Successfully Added!");
                    displayBookLoan(bookLoan);
                    System.out.println();

                    return true;
                }
            }else{
                //If search returned a single book but multiple members.
                Member memberResult = memberFactory.multipleMembers(memberResults);

                if(memberResult != null){
                    BookLoan bookLoan = new BookLoan(bookResults.get(0).getId(), memberResults.get(0).getId(), LocalDate.now());
                    bookLoan.setDependencies(bookLoanFactory);

                    if (bookLoanFactory.add(bookLoan)) {
                        System.out.println("Book Successfully Added!");
                        displayBookLoan(bookLoan);
                        System.out.println();

                        return true;
                    }
                }
            }
        }else if(bookResults.size() > 1){
            Book bookResult = bookFactory.multipleBooks(bookResults);

            if(bookResult != null){
                if(memberResults.size() == 1){
                    //If search returned a single member but multiple books
                    BookLoan bookLoan = new BookLoan(bookResults.get(0).getId(), memberResults.get(0).getId(), LocalDate.now());
                    bookLoan.setDependencies(bookLoanFactory);

                    if (bookLoanFactory.add(bookLoan)) {
                        System.out.println("Book Successfully Added!");
                        displayBookLoan(bookLoan);
                        System.out.println();

                        return true;
                    }
                }else{
                    //If search returned multiple members and multiple books.
                    Member memberResult = memberFactory.multipleMembers(memberResults);

                    if(memberResult != null){
                        BookLoan bookLoan = new BookLoan(bookResults.get(0).getId(), memberResults.get(0).getId(), LocalDate.now());
                        bookLoan.setDependencies(bookLoanFactory);

                        if (bookLoanFactory.add(bookLoan)) {
                            System.out.println("Book Successfully Added!");
                            displayBookLoan(bookLoan);
                            System.out.println();

                            return true;
                        }
                    }
                }
            }
        }else{
            System.out.println("No books found!");
        }

        return false;
    }

    /**
     * Searches for a member by first name and last name.
     *
     * @param fName the members first name.
     * @param lName the members last name.
     * @return whether the function was successful or not.
     */
    public boolean searchMember(String fName, String lName){
        List<Member> members = memberFactory.getMembers(fName, lName);

        if(!members.isEmpty()){
            for (Member member : members) {
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
                    //Print subheadings for the member's book loans.
                    System.out.printf("%7s %-9s %-9s %-30s %-35s %-5s %-17s %-17s %-10s\n", "|", "Loan ID", "Book ID", "Title", "Author", "Year", "Borrow Date", "Due Date", "Status");
                    for (Book book : bookResults) {
                        bookCount++;
                        BookLoan loan = bookLoanFactory.getBookLoan(book.getId(), member.getId());
                        String warning = loan.isOverdue() ? "OVERDUE" : "On Loan";

                        //Print data for the member's book loans.
                        System.out.printf("%7s %-9d %-9d %-30s %-35s %-5d %-17s %-17s %-10s\n",
                                "|",
                                loan.getId(),
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
            }

            return true;
        }else{
            System.out.println("No member found");
        }

        return false;
    }

    /**
     * Returns a book and subsequently deletes a book loan with a given id.
     *
     * @param id the id of the book loan to be removed.
     * @return boolean that dictates whether or not the function was successful.
     */
    public boolean returnBook(int id) {
        BookLoan bookLoan = bookLoanFactory.getBookLoanById(id);

        if (bookLoan != null) {

            if (bookLoan.isOverdue()) {

                System.out.printf("OVERDUE! Fine: £%.2f\n", bookLoan.getFine());

                if(yesNoDecision("Is the fine paid? ")){
                    bookLoans.remove(bookLoan);
                    System.out.println("Book successfully returned");

                    return true;
                }else{
                    System.out.println("Book cannot be returned until fine is paid");
                }

            } else {
                bookLoans.remove(bookLoan);
                System.out.println("Book successfully returned");

                return true;
            }
        }else{
            System.out.println("This book hasn't been taken out, therefore cannot be returned!");
        }
        System.out.println();

        return false;
    }

    /**
     * Creates a new book with the given properties and adds it to the library.
     *
     * @param title the title of the new book.
     * @param authors the authors of the new book.
     * @param year the year the new book was released.
     * @param qty the amount of books available to be taken out.
     * @return boolean that dictates whether or not the function was successful.
     */
    public boolean addNewBook(String title, String[] authors, int year, int qty){
        Book book = new Book(title, authors, year, qty);
        book.setDependencies(bookFactory);

        if(bookFactory.add(book)){
            System.out.println("Book successfully added to library: ");
            displayBook(book);
            System.out.println();

            return true;
        }

        return false;
    }

    /**
     * Creates a new member with the given properties and adds them to the library.
     *
     * @param fName the first name of the member to be added.
     * @param lName the last name of the member to be added.
     * @param date the date the member was added.
     * @return boolean that dictates whether or not the function was successful.
     */
    public boolean addNewMember(String fName, String lName, LocalDate date){
        Member member = new Member(fName, lName, date);
        member.setDependencies(memberFactory);

        if(memberFactory.add(member)){
            System.out.println("Member successfully added: ");
            displayMember(member);
            System.out.println();

            return true;
        }

        return false;
    }

    /**
     * Changes the quantity of a book that's found by a given search query.
     * If multiple books are found then the user is asked to choose one by ID from the given list.
     *
     * @param query the string to be partially or fully matched.
     * @param qty the quantity increased or decrease (negative value).
     * @return boolean that dictates whether or not the function was successful.
     */
    public boolean changeQuantity(String query, int qty){
        List<Book> results = bookFactory.getBook(query);

        if(results.size() > 1) {
            Book book = bookFactory.multipleBooks(results);

            if(book != null){
                if(book.changeNumberCopies(qty)){
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

    /**
     * The interactive version of {@link #searchBook(String)}
     * Takes in input and parses it so it's in the form to be passed with the non-interactive version.
     */
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

    /**
     * The interactive version of {@link #searchMember(String, String)}
     * Takes in input and parses it so it's in the form to be passed with the non-interactive version.
     */
    public void searchMember(){
        Scanner in = new Scanner(System.in);
        System.out.println("Search Member");

        System.out.print(" |Full Name: ");
        String fName = in.next();
        String lName = in.next();
        System.out.println();

        if(!searchMember(fName, lName)){
            System.out.println("Could not complete search.");
        }
        if(yesNoDecision("Would you like to search again?")){
            searchMember();
        }
    }

    /**
     * The interactive version of {@link #borrowBook(String, String, String)} )}
     * Takes in input and parses it so it's in the form to be passed with the non-interactive version.
     */
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

    /**
     * The interactive version of {@link #returnBook(int)}
     * Takes in input and parses it so it's in the form to be passed with the non-interactive version.
     */
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

    /**
     * The interactive version of {@link #addNewBook(String, String[], int, int)}
     * Takes in input and parses it so it's in the form to be passed with the non-interactive version.
     */
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

    /**
     * The interactive version of {@link #addNewMember(String, String, LocalDate)}
     * Takes in input and parses it so it's in the form to be passed with the non-interactive version.
     */
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
        System.out.printf(" |Date Joined: %s \n\n", dateJoined);

        if(!addNewMember(fName, lName, dateJoined)){
            System.out.println("Could not add member.");
        }
        if(yesNoDecision("Would you like to add another? ")) {
            addNewMember();
        }
    }

    /**
     * The interactive version of {@link #changeQuantity(String, int)}
     * Takes in input and parses it so it's in the form to be passed with the non-interactive version.
     */
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

    /**
     * Used to write all lists to their respective text files.
     *
     * @param booksFileName the file to write the books list to.
     * @param membersFileName the file to write the members list to.
     * @param bookLoansFileName the file to write the book loans list to.
     */
    public void saveChanges(String booksFileName, String membersFileName, String bookLoansFileName){
        bookFactory.write(booksFileName);
        memberFactory.write(membersFileName);
        bookLoanFactory.write(bookLoansFileName);
    }

    /**
     * Utility function that is used to question a user with a given message.
     * The answer must always be a variant of yes/no, which respectively returns true or false.
     * If invalid input then function gets called recursively until correct input.
     *
     * @param message the question to ask the user.
     * @return boolean as to whether the user said yes or no.
     */
    public static boolean yesNoDecision(String message){
        System.out.println(message + "y/n");
        Scanner in = new Scanner(System.in);
        String userStringInput = in.nextLine();
        char userInput;
        if(!userStringInput.isEmpty()){
            userInput = userStringInput.charAt(0);
        }else{
            return yesNoDecision(message);
        }
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

    /**
     * Utility function to format the output of a single book in a table.
     *
     * @param book the book object to format and output.
     */
    public void displayBook(Book book){
        System.out.printf("%-7s %-30s %-35s %-5s %-17s %-3s\n","ID", "Title", "Author(s)", "Year", "Number of Copies", "Available");
        System.out.printf("%-7d %-30s %-35s %-5d %-17d %-3d\n",
                book.getId(),
                book.getTitle(),
                Arrays.toString(book.getAuthor()),
                book.getYear(),
                book.getNumberCopies(),
                book.getAvailable()
        );
    }

    /**
     * Utility function to format the output of a list of books in a table.
     *
     * @param books the list of books to format and output.
     */
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

    /**
     * Utility function to format the output of a list of members in a table.
     *
     * @param members the list of members to format and output.
     */
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

    /**
     * Utility function to format the output of a single member in a table.
     *
     * @param member the member object to format and output.
     */
    public void displayMember(Member member){
        System.out.printf("%-7s %-12s %-12s %-15s\n","ID", "First Name", "Last Name", "Date Joined");
        System.out.printf("%-7d %-12s %-12s %-15s\n",
                member.getId(),
                member.getFName(),
                member.getLName(),
                member.getDateJoin().toString()
        );
    }

    /**
     * Utility function to format the output of a list of book loans in a table.
     *
     * @param bookLoans the list of book loans to format and output.
     */
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

    /**
     * Utility function to format and out a single book loan object in a table.
     *
     * @param bookLoan the single book loan object to be formatted and outputted.
     */
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
