import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Contains all functions involved in manipulating Book objects
 * Includes reading and writing to the storage text file.
 */
public class BookFactory {

    private List<Book> books;

    public MemberFactory memberFactory;
    public BookLoanFactory bookLoanFactory;


    /**
     * Used to set dependencies required for the factory to run.
     * Does things that can't be done upon construction due to the fact other factories have to be constructed first.
     *
     * @param books list of books to pass by reference to the factory so the functions can use it.
     * @param memberFactory factory with functions mainly used to manipulate members but can be
     *                      useful in other factories
     * @param bookLoanFactory factory with functions mainly used to manipulate book loans but can be
     *                        useful in other factories.
     */
    public void setDependencies(List<Book> books, MemberFactory memberFactory, BookLoanFactory bookLoanFactory){

        this.books = books;
        this.memberFactory = memberFactory;
        this.bookLoanFactory = bookLoanFactory;
    }

    /**
     * Getter for books list.
     *
     * @return full list of books.
     */
    public List<Book> getBooks() {
        return books;
    }

    /**
     * Reads from the given file and creates a list of books to be accessible by the rest of the program.
     *
     * @param file the file path to the file that needs to be read.
     * @return the new list of books created from the text file.
     */
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

    /**
     * Used to write the books list to the given text file.
     *
     * @param file the file to write to.
     */
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

    /**
     * Retrieves a set of books that the string query fully or partially matches (by title).
     *
     * @param query the string to fully or partially match to books.
     * @return the results found.
     */
    public List<Book> getBook(String query){
        query = query.toLowerCase();
        List<Book> results = new ArrayList<>();
        for (Book book: books) {
            if (book.getTitle().toLowerCase().contains(query)) {
                results.add(book);
            }
        }
        return results;
    }

    /**
     * Retrieves a single book by the given id.
     * Returns null if no book found
     *
     * @param id the id of the book to find.
     * @return the book with the id or null if no book exists with that id.
     */
    public Book getBookById(int id){
        for (Book book: books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    /**
     * Queries the user as to which book they want to use if a search has returned multiple books.
     * The user selects by ID.
     *
     * @param books the list of books to choose from.
     * @return the book chosen or null if a non-existant ID is chosen.
     */
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

    /**
     * Adds a new book instance to the book list.
     *
     * @param book the book instance to add.
     * @return boolean as to whether or not the function was successful.
     */
    public boolean add(Book book){
        if(books.size() == 0 && book.getId() == -1) {
            book.setId(100000);
        } else {
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
