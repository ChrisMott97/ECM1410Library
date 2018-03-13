import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        Library lib = new Library("data/books.txt","data/members.txt", "data/bookloans.txt");

//        lib.showAllBooks();
//        lib.showAllMembers();

//        lib.changeQuantity();
        lib.borrowBook("da buuk", "ors", "barkanyi");
        lib.addNewBook("der buuk", new String[]{"orts"}, 7, 5);
    }
}
