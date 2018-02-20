import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Book {
    private int id;
    private String title;
    private String[] author;
    private int year;
    private int numberCopies;
    private int available;

    public Book(int id, String title, String[] author, int year, int numberCopies){
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.numberCopies = numberCopies;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String[] getAuthor() {
        return author;
    }
    public int getYear() {
        return year;
    }
    public int getNumberCopies() {
        return numberCopies;
    }
    public int getAvailable() {
        return 0;
    }
    public static List<Book> read(String file){
        BufferedReader br;
        String line;
        List<Book> books = new ArrayList<>();

        try {
            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);

            while((line = br.readLine()) != null){
                String[] book = line.split(",");


                books.add(new Book(
                        Integer.parseInt(book[0]),
                        book[1],
                        book[2].split(":"),
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
