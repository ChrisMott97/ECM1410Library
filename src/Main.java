import java.util.List;

public class Main {

    public static void main(String[] args) {
        Library lib = new Library("data/books.txt","data/members.txt",
                "data/bookloans.txt");
        List<Book> books = lib.showAllBooks();
        for (Book book :
                books) {
            System.out.println(book.getName());
        }
    }
}
