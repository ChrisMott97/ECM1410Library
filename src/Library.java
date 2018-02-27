import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Library {

    List<Book> books;
    List<Member>  members;
    List<BookLoan> bookLoans;

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

    public Book getBookById(int id){
        for (Book book: books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    public void searchBook(String title){
        title = title.toLowerCase();
        List<Book> search = new ArrayList<>();
        for (Book book: books) {
            if (book.getTitle().toLowerCase().contains(title)) {
                search.add(book);
            }
        }
        if(!search.isEmpty()){
            System.out.printf("%-7s %-30s %-35s %-5s %-3s\n","ID", "Title", "Author", "Year", "Number of Copies");
            for (Book book : search) {
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

    public void searchMember(String fName, String lName){
        fName = fName.toLowerCase();
        lName = lName.toLowerCase();
        List<Member> search = new ArrayList<>();
        for (Member member: members) {
            if (member.getFName().toLowerCase().contains(fName) && member.getLName().toLowerCase().contains(lName)) {
                search.add(member);
            }
        }

        if(!search.isEmpty()){
            System.out.printf("%-7s %-30s %-35s %-8s\n","ID", "First Name", "Last Name", "Date Joined");
            for (Member member : search) {
                System.out.printf("%-7s %-30s %-35s %-8s\n",
                        member.getId(),
                        member.getFName(),
                        member.getLName(),
                        member.getDateJoin()
                );
                List<Book> books = new ArrayList<>();
                for (BookLoan bookLoan : bookLoans){
                    if (bookLoan.getMemberId() == member.getId()){
                       books.add(getBookById(bookLoan.getBookId()));
                    }
                }
                if(!books.isEmpty()) {
                    System.out.printf("%7s\n","|");
                    System.out.printf("%7s %-9s %-30s %-35s %-5s %-3s\n","|","Loan ID", "Title", "Author", "Year", "Number of Copies");
                    for (Book book : books){
                        System.out.printf("%7s %-9s %-30s %-35s %-5s %-3s\n",
                                "|",
                                book.getId(),
                                book.getTitle(),
                                Arrays.toString(book.getAuthor()),
                                book.getYear(),
                                book.getNumberCopies()
                        );
                    }
                }
            }
        }else{
            System.out.println("No members found");
        }
    }
}
