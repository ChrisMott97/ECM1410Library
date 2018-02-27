import java.util.*;

public class Library {

    List<Book> books;
    List<Member>  members;
    List<BookLoan> bookLoans;

    public Library(String booksFileName, String membersFileName, String bookLoansFileName){
        books = Book.read(booksFileName);
        members = Member.read(membersFileName);
        bookLoans = BookLoan.read(bookLoansFileName);
    }

    public void write(){
        BookLoan.write("data/bookloans.txt", bookLoans);
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
        List<Book> results = Book.getBook(query, books);
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
        List<Book> bookResult = Book.getBook(book, books);
        Member memberResult = Member.getMember(fName, lName, members);

        if(bookResult.size() == 1 && memberResult != null){
            BookLoan bookLoan = new BookLoan(bookResult.get(0).getId(), memberResult.getId(), new Date());
            if(bookLoan.addTo(bookLoans)){
                //BookLoan.write('data/bookloans.txt', bookLoans)

                System.out.println("Book successfully borrowed: ");
                System.out.printf("%-8s %-10s %-11s %-15s\n","ID", "Book ID", "Member ID", "Borrow Date");
                System.out.printf("%-8d %-10d %-11d %-15s\n",
                        bookLoan.getId(),
                        bookLoan.getBookId(),
                        bookLoan.getMemberId(),
                        bookLoan.getBorrowDate().toString()
                );
                System.out.println();
            }
        }
    }

    public void searchMember(String fName, String lName){
        Member member = Member.getMember(fName, lName, members);
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
                   bookResults.add(Book.getBookById(bookLoan.getBookId(), books));
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
        BookLoan bookLoan = BookLoan.getBookLoanById(id, bookLoans);

        Calendar cal = Calendar.getInstance();

        Date today = new Date();
        Date borrowDate = bookLoan.getBorrowDate();
        Date returnDate;

        cal.setTime(borrowDate);
        cal.add(Calendar.DATE, 30);
        returnDate = cal.getTime();

        if (bookLoan != null) {
            if (returnDate.after(today)) {
                System.out.println("YOU PAY!!");
            } else {
                System.out.println("YOU NO PAY!!");
            }
        }
    }

    public void addNewBook(String title, String[] authors, int year, int qty){
        Book book = new Book(title, authors, year, qty);
        if(book.addTo(books)){
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
}
