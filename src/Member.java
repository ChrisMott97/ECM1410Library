import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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

    public static void write(String file){
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            for (Member member : Library.members) {
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
    public static List<Member> getMembers(String fName, String lName){
        fName = fName.toLowerCase();
        lName = lName.toLowerCase();
        List<Member> members = new ArrayList<>();
        for (Member member: Library.members) {
            if (member.getFName().toLowerCase().contains(fName) && member.getLName().toLowerCase().contains(lName)) {
                members.add(member);
            }
        }
        return members;
    }
    public static Member multipleMembers(List<Member> members){
        System.out.printf("%-7s %-30s %-35s %-8s\n", "ID", "First Name", "Last Name", "Date Joined");
        for (Member member : members) {
            System.out.printf("%-7s %-30s %-35s %-8s\n",
                    member.getId(),
                    member.getFName(),
                    member.getLName(),
                    member.getDateJoin()
            );
        }
        System.out.printf("%d results found. Please enter ID of the one you want to change:", members.size());
        System.out.println();
        boolean found = false;
        while(!found){
            Scanner in = new Scanner(System.in);
            String id = in.next();
            Member member = Member.getMemberById(Integer.parseInt(id));
            if(member != null){
                return member;
            }else{
                if(!Library.yesNoDecision("Wrong ID, would you like to try again?")){
                    found = true;
                }
            }
            System.out.println();
        }
        return null;
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

    public boolean add(){
        if(getId() == -1) {
            int newId = Library.members.get(Library.members.size() - 1).getId() + 1;
            setId(newId);
        }
        if(getMemberById(getId()) == null){
            if(getMembers(getFName(), getLName()) == null){
                Library.members.add(this);
                return true;
            }else{
                System.out.println("Member already exists with this name!");
            }
        }else{
            System.out.println("Member already exists with this ID");
        }
        System.out.println();
        return false;
    }
}
