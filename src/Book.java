import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Book {
    private int id;
    private String title;
    private String[] author;
    private int year;
    private int numberCopies;

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
    public static void write(String file){
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            for (Book book : Library.books) {
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
    public int getAvailable(){
        int result = 0;
        for (BookLoan bookLoan : Library.bookLoans) {
            if(bookLoan.getBookId() == getId()){
                result++;
            }
        }
        return getNumberCopies() - result;
    }
    public static List<Book> getBook(String query){
        query = query.toLowerCase();
        List<Book> search = new ArrayList<>();
        for (Book book: Library.books) {
            if (book.getTitle().toLowerCase().contains(query)) {
                search.add(book);
            }
        }
        return search;
    }
    public static Book getBookById(int id){
        for (Book book: Library.books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }
    public static Book multipleBooks(List<Book> books){
        System.out.printf("%-7s %-30s %-35s %-5s %-17s %-3s\n", "ID", "Title", "Author", "Year", "Number of Copies", "Available");
        for (Book book : books) {
            System.out.printf("%-7d %-30s %-35s %-5d %-17d %-3d\n",
                    book.getId(),
                    book.getTitle(),
                    Arrays.toString(book.getAuthor()),
                    book.getYear(),
                    book.getNumberCopies(),
                    book.getAvailable()
            );
        }
        System.out.printf("%d results found. Please enter ID of the one you want to change:", books.size());
        System.out.println();
        boolean found = false;
        while(!found){
            Scanner in = new Scanner(System.in);
            String id = in.next();
            Book book = Book.getBookById(Integer.parseInt(id));
            if(book != null){
                return book;
            }else{
                if(!Library.yesNoDecision("Wrong ID, would you like to try again?")){
                    found = true;
                }
            }
            System.out.println();
        }
        return null;
    }


    public boolean add(){
        if(getId() == -1) {
            int newId = Library.books.get(Library.books.size() - 1).getId() + 1;
            setId(newId);
        }
        if(getBookById(getId()) == null){
            if(getBook(this.getTitle()).isEmpty()){
                Library.books.add(this);
                return true;
            }else{
                return Library.yesNoDecision("Book title already exists! Would you like to continue?");
            }
        }else{
            System.out.println("Book ID already exists!");
            //TODO Change to exception
        }
        System.out.println();
        return false;
    }

    public boolean changeNumberCopies(int qty) {
        if(this.getAvailable() + qty >= 0){
            this.numberCopies += qty;
            return true;
        }
        return false;
    }
}
