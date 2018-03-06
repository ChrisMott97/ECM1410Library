import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        Library lib = new Library("data/books.txt","data/members.txt", "data/bookloans.txt");

        lib.showAllBooks();
        lib.showAllMembers();
        lib.showAllBookLoans();

        lib.borrowBook();
    }
}
