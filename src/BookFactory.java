import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BookFactory {

    private List<Book> books;

    public MemberFactory memberFactory;
    public BookLoanFactory bookLoanFactory;


    public void setDependencies(List<Book> books, MemberFactory memberFactory, BookLoanFactory bookLoanFactory){
        this.books = books;
        this.memberFactory = memberFactory;
        this.bookLoanFactory = bookLoanFactory;
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Book> read(String file){
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
            for (Book book : books) {
                book.setDependencies(this);
            }


            br.close();

        }catch (IOException e){
            e.printStackTrace();
        }

        return books;
    }
    public void write(String file){
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

    public List<Book> getBook(String query){
        query = query.toLowerCase();
        List<Book> search = new ArrayList<>();
        for (Book book: books) {
            if (book.getTitle().toLowerCase().contains(query)) {
                search.add(book);
            }
        }
        return search;
    }
    public Book getBookById(int id){
        for (Book book: books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }
    public Book multipleBooks(List<Book> books){
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
            Book book = getBookById(Integer.parseInt(id));
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

    public boolean add(Book book){
        if(book.getId() == -1) {
            int newId = books.get(books.size() - 1).getId() + 1;
            book.setId(newId);
        }
        if(getBookById(book.getId()) == null){
            if(getBook(book.getTitle()).isEmpty()){
                books.add(book);
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
}
