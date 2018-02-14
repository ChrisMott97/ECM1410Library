public class Book {
    private int id;
    private String name;
    private String author;
    private int year;
    private int qty;
    private int available;

    public Book(int id, String name, String author, int year, int qty){
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
        this.qty = qty;
        this.available = qty;
    }
}
