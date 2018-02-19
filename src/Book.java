import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Book {
    private int id;
    private String name;
    private String author;
    private int year;
    private int qty;
    private int available;

    public Book(int id, String name, String author, int year, int qty){
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
        this.qty = qty;
        this.available = qty;
    }

    public String getName() {
        return name;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public static List<Book> read(String file){
        BufferedReader br = null;
        String line;
        List<Book> books = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(file));

            while((line = br.readLine()) != null){
                String[] book = line.split(",");

                books.add(new Book(
                        Integer.parseInt(book[0]),
                        book[1],
                        book[2],
                        Integer.parseInt(book[3]),
                        Integer.parseInt(book[4]))
                );
            }

            br.close();
            
        }catch (IOException e){
            e.printStackTrace();
        }

        return books;
    }

}
