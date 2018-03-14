import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Stores all basic information about a single Book.
 */
public class Book {
    private BookFactory bookFactory;
    private int id;
    private String title;
    private String[] author;
    private int year;
    private int numberCopies;

    /**
     * Constructs a new Book with given parameters only if it's being read from file (because the ID is known).
     *
     * @param id the id (primary key) of a book.
     * @param title the title of the book.
     * @param author the collection of authors of the book.
     * @param year the year the book was released.
     * @param numberCopies number of copies available to be loaned.
     */
    public Book(int id, String title, String[] author, int year, int numberCopies){
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.numberCopies = numberCopies;
    }

    /**
     * Constructs a new Book with given parameters with id = -1 to ensure it later gets given a proper id based on
     *  previous ids in the list.
     *
     * @param title the title of the book.
     * @param author the collection of authors of the book.
     * @param year the year the book was released.
     * @param numberCopies number of copies available to be loaned.
     */
    public Book(String title, String[] author, int year, int numberCopies){
        this.id = -1; // signify that the ID needs to be assigned based on previous IDs in the list.
        this.title = title;
        this.author = author;
        this.year = year;
        this.numberCopies = numberCopies;
    }

    /**
     * Sets the dependencies that some functions in the book may require.
     *
     * @param bookFactory the book factory that has all methods required for manipulating books.
     */
    public void setDependencies(BookFactory bookFactory){
        this.bookFactory = bookFactory;
    }

    /**
     * Getter for id.
     *
     * @return int id.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for id.
     *
     * @param id id that Book id is to be set to.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for title.
     *
     * @return String title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for author.
     *
     * @return String[] authors.
     */
    public String[] getAuthor() {
        return author;
    }

    /**
     * Getter for year.
     *
     * @return int year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Getter for number of copies.
     *
     * @return int number of copies.
     */
    public int getNumberCopies() {
        return numberCopies;
    }

    /**
     * Calculates the number of copies available to be loaned based on existing loans for this book.
     *
     * @return number available to be loaned.
     */
    public int getAvailable(){
        int result = 0;
        for (BookLoan bookLoan : bookFactory.bookLoanFactory.getBookLoans()) {
            if(bookLoan.getBookId() == getId()){
                result++;
            }
        }
        return getNumberCopies() - result;
    }

    /**
     * Changes the number of copies and the number available.
     *
     * @param qty amount to increase or decrease quantity by (negative values decrease).
     * @return boolean as to whether the function was successful.
     */
    public boolean changeNumberCopies(int qty) {
        if(this.getAvailable() + qty >= 0){
            this.numberCopies += qty;
            return true;
        }
        return false;
    }
}
