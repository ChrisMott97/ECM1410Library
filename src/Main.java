import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        Library lib = new Library("data/books.txt","data/members.txt", "data/bookloans.txt");

        lib.showAllBooks();
        lib.showAllMembers();
        lib.showAllBookLoans();

        lib.searchBook("java");
        lib.searchMember("Sarah","Hoopern");
        //lib.returnBook(300002); some kind of error with this shit idek
        lib.addNewBook("Da Buuk", new String[]{"Chris Mott"}, 2018, 1);

        lib.showAllBooks();
        lib.borrowBook("Da Buuk", "Sarah", "Hoopern");

        lib.borrowBook("Da Buuk", "Ors", "Barkanyi");

        lib.addNewMember("Orsh", "Barkanyi", LocalDate.now());

        lib.addNewBook("Puush", new String[]{"Ors Barkanyi"}, 2018, 1);

        lib.borrowBook("Puush", "John", "Cooper");
        lib.changeQuantity("Data Mining",1);

        //TODO Add all library functions with no arguments w/ input
    }
}
