import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Library {

    List<Book> books = new ArrayList<>();
    List<Member>  members = new ArrayList<>();
    List<BookLoan> bookloans = new ArrayList<>();

    public Library(String booksFileName, String membersFileName, String bookLoansFileName){
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(booksFileName));

            while((line = br.readLine()) != null){
                String[] book = line.split(",");
                books.add(new Book(
                        Integer.parseInt(book[0]),
                        book[1],
                        book[2],
                        Integer.parseInt(book[3]),
                        Integer.parseInt(book[4])));
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (br != null){
                try{
                    br.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public List<Book> showAllBooks(){
        return books;
    }
}
