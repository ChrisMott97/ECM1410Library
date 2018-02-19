import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Library {

    List<Book> books = new ArrayList<>();
    List<Member>  members = new ArrayList<>();
    List<BookLoan> bookloans = new ArrayList<>();

    public Library(String booksFileName, String membersFileName, String bookLoansFileName){
        books = Book.read(booksFileName);
    }
    public List<Book> showAllBooks(){
        return books;
    }
}
