import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookLoan {
    private int id;
    private int bookId;
    private int memberId;
    private Date borrowDate;

    public BookLoan(int id, int bookId, int memberId, Date borrowDate){
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
    }

    public static List<BookLoan> read(String file){
        BufferedReader br;
        String line;
        List<BookLoan> bookLoans = new ArrayList<>();

        try {
            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);

            while((line = br.readLine()) != null){
                String[] bookLoan = line.split(",");
                Date borrowDate = null;

                try {
                    borrowDate = new SimpleDateFormat("yyyy-MM-dd").parse(bookLoan[3]);
                } catch (ParseException e){
                    e.printStackTrace();
                }

                bookLoans.add(new BookLoan(
                        Integer.parseInt(bookLoan[0]),
                        Integer.parseInt(bookLoan[1]),
                        Integer.parseInt(bookLoan[2]),
                        borrowDate
                ));
            }

            br.close();

        }catch (IOException e){
            e.printStackTrace();
        }

        return bookLoans;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }
}
