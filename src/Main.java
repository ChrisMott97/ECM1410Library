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

        lib.write();
    }
}
