import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
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

    public Member(String fName, String lName, Date dateJoin){
        this.id = -1;
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

    public static void write(String file){
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (Member member : Library.members) {
                writer.printf("%d,%s,%s,%s\n",
                        member.getId(),
                        member.getFName(),
                        member.getLName(),
                        dateFormat.format(member.getDateJoin())
                );
            }
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //getters
    public int getId(){
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    public static Member getMember(String fName, String lName){
        fName = fName.toLowerCase();
        lName = lName.toLowerCase();
        for (Member member: Library.members) {
            if (member.getFName().toLowerCase().contains(fName) && member.getLName().toLowerCase().contains(lName)) {
                return member;
            }
        }
        return null;
    }

    public static Member getMemberById(int id){
        for (Member member : Library.members) {
            if(member.getId() == id){
                return member;
            }
        }
        return null;
    }

    public boolean addTo(){
        if(getId() == -1) {
            int newId = Library.members.get(Library.members.size() - 1).getId() + 1;
            setId(newId);
        }
        if(getMemberById(getId()) == null){
            if(getMember(getFName(), getLName()) == null){
                Library.members.add(this);
                return true;
            }else{
                System.out.println("Member already exists with this name!");
            }
        }else{
            System.out.println("Member already exists with this ID");
        }
        return false;
    }
}
