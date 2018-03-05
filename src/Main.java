import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Library lib = new Library("data/books.txt","data/members.txt", "data/bookloans.txt");

        lib.showAllBooks();
        lib.showAllMembers();
        lib.showAllBookLoans();

        lib.searchBook("java");
        lib.searchMember("Sarah","Hoopern");
        lib.returnBook(300002);
        lib.addNewBook("Da Buuk", new String[]{"Chris Mott"}, 2018, 1);

        lib.showAllBooks();
        lib.borrowBook("Da Buuk", "Sarah", "Hoopern");

        lib.addNewMember("Ors", "Barkanyi", new Date());

        lib.addNewBook("Puush", new String[]{"Ors Barkanyi"}, 2018, 1);

        lib.borrowBook("Puush", "John", "Cooper");

        //TODO Add all library functions with no arguments w/ input
    }
}
