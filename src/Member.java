import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Member {
    private int id;
    private String fName;
    private String lName;
    private Date dateJoin;

    public Member(int id, String fName, String lName, Date dateJoin){
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.dateJoin = dateJoin;
    }

    public static List<Member> read(String file){
        BufferedReader br;
        String line;
        List<Member> members = new ArrayList<>();

        try {
            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);

            while((line = br.readLine()) != null){
                String[] member = line.split(",");
                Date date = null;

                try {
                     date = new SimpleDateFormat("yyyy-MM-dd").parse(member[3]);
                } catch (ParseException e){
                    e.printStackTrace();
                }

                members.add(new Member(
                        Integer.parseInt(member[0]),
                        member[1],
                        member[2],
                        date
                ));
            }

            br.close();

        }catch (IOException e){
            e.printStackTrace();
        }

        return members;
    }

    //getters
    public int getId(){
        return id;
    }
    public String getFName(){
        return fName;
    }
    public String getLName(){
        return lName;
    }
    public Date getDateJoin(){
        return dateJoin;
    }
}
