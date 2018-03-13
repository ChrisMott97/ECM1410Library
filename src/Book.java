import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 */
public class Book {
    private BookFactory bookFactory;
    private int id;
    private String title;
    private String[] author;
    private int year;
    private int numberCopies;

    /**
     * @param id
     * @param title
     * @param author
     * @param year
     * @param numberCopies
     */
    public Book(int id, String title, String[] author, int year, int numberCopies){
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.numberCopies = numberCopies;
    }

    /**
     * @param title
     * @param author
     * @param year
     * @param numberCopies
     */
    public Book(String title, String[] author, int year, int numberCopies){
        this.id = -1;
        this.title = title;
        this.author = author;
        this.year = year;
        this.numberCopies = numberCopies;
    }

    /**
     * @param bookFactory
     */
    public void setDependencies(BookFactory bookFactory){
        this.bookFactory = bookFactory;
    }

    /**
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return
     */
    public String[] getAuthor() {
        return author;
    }

    /**
     * @return
     */
    public int getYear() {
        return year;
    }

    /**
     * @return
     */
    public int getNumberCopies() {
        return numberCopies;
    }

    /**
     * @return
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
     * @param qty
     * @return
     */
    public boolean changeNumberCopies(int qty) {
        if(this.getAvailable() + qty >= 0){
            this.numberCopies += qty;
            return true;
        }
        return false;
    }
}
