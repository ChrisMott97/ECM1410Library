import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Member {
    private int id;
    private String fName;
    private String lName;
    private LocalDate dateJoin;

    public Member(int id, String fName, String lName, LocalDate dateJoin){
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.dateJoin = dateJoin;
    }

    public Member(String fName, String lName, LocalDate dateJoin){
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

                LocalDate date = LocalDate.parse(member[3]);

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

    public static void write(String file, List<Member> members){
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");

            for (Member member : members) {
                writer.printf("%d,%s,%s,%s\n",
                        member.getId(),
                        member.getFName(),
                        member.getLName(),
                        member.getDateJoin()
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
    public LocalDate getDateJoin(){
        return dateJoin;
    }
    public static Member getMember(String fName, String lName, List<Member> members){
        fName = fName.toLowerCase();
        lName = lName.toLowerCase();
        for (Member member: members) {
            if (member.getFName().toLowerCase().contains(fName) && member.getLName().toLowerCase().contains(lName)) {
                return member;
            }
        }
        return null;
    }

    public static Member getMemberById(int id, List<Member> members){
        for (Member member : members) {
            if(member.getId() == id){
                return member;
            }
        }
        return null;
    }

    public boolean addTo(List<Member> members){
        if(getId() == -1) {
            int newId = members.get(members.size() - 1).getId() + 1;
            setId(newId);
        }
        if(getMemberById(getId(), members) == null){
            if(getMember(getFName(), getLName(), members) == null){
                members.add(this);
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
