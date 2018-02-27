import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    public Book(String title, String[] author, int year, int numberCopies){
        this.id = -1;
        this.title = title;
        this.author = author;
        this.year = year;
        this.numberCopies = numberCopies;
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
    public static void write(String file, List<Book> books){
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            for (Book book : books) {
                String authors = Arrays.toString(book.getAuthor());
                writer.printf("%d,%s,%s,%d,%d\n",
                        book.getId(),
                        book.getTitle(),
                        authors.substring(1, authors.length()-1).replaceAll(", ", ":"),
                        book.getYear(),
                        book.getNumberCopies()
                );
            }
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    public static List<Book> getBook(String query, List<Book> books){
        query = query.toLowerCase();
        List<Book> search = new ArrayList<>();
        for (Book book: books) {
            if (book.getTitle().toLowerCase().contains(query)) {
                search.add(book);
            }
        }
        return search;
    }
    public static Book getBookById(int id, List<Book> books){
        for (Book book: books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    public void addTo(List<Book> books){
        if(getId() == -1) {
            int newId = books.get(books.size() - 1).getId() + 1;
            setId(newId);
        }
        if(getBookById(getId(), books) == null){
            if(getBook(this.getTitle(), books).isEmpty()){
                books.add(this);
            }else{
                System.out.println("Book title already exists!");
                //TODO Add input to input
            }
        }else{
            System.out.println("Book ID already exists!");
            //TODO handle better
        }
    }
}
