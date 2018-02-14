import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Library {

    ArrayList<Book> books = new ArrayList<Book>();
    ArrayList<Member>  members = new ArrayList<Member>();
    ArrayList<BookLoan> bookloans = new ArrayList<BookLoan>();

    public Library(String booksFileName, String membersFileName, String bookLoansFileName){

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(booksFileName))) {
            stream.forEach(record -> books.add(new Book()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Stream<String> stream = Files.lines(Paths.get(membersFileName))) {
            stream.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Stream<String> stream = Files.lines(Paths.get(bookLoansFileName))) {
            stream.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
