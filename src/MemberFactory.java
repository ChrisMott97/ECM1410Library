import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MemberFactory {
    private List<Member> members;

    public BookFactory bookFactory;
    public BookLoanFactory bookLoanFactory;


    public void setDependencies(List<Member> members, BookFactory bookFactory, BookLoanFactory bookLoanFactory){
        this.bookFactory = bookFactory;
        this.members = members;
        this.bookLoanFactory = bookLoanFactory;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Member> read(String file){
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
            for (Member member : members) {
                member.setDependencies(this);
            }

            br.close();

        }catch (IOException e){
            e.printStackTrace();
        }

        return members;
    }
    public void write(String file){
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

    public List<Member> getMembers(String fName, String lName){
        fName = fName.toLowerCase();
        lName = lName.toLowerCase();
        List<Member> members = new ArrayList<>();
        for (Member member: members) {
            if (member.getFName().toLowerCase().contains(fName) && member.getLName().toLowerCase().contains(lName)) {
                members.add(member);
            }
        }
        return members;
    }
    public Member multipleMembers(List<Member> members){
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
            Member member = getMemberById(Integer.parseInt(id));
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
    public Member getMember(String fName, String lName){
        fName = fName.toLowerCase();
        lName = lName.toLowerCase();
        for (Member member: members) {
            if (member.getFName().toLowerCase().contains(fName) && member.getLName().toLowerCase().contains(lName)) {
                return member;
            }
        }
        return null;
    }
    public Member getMemberById(int id){
        for (Member member : members) {
            if(member.getId() == id){
                return member;
            }
        }
        return null;
    }
    public boolean add(Member member){
        if(members.size() == 0 && member.getId() == -1) {
            member.setId(200000);
        } else {
            int newId = members.get(members.size() - 1).getId() + 1;
            member.setId(newId);
        }
        if(getMemberById(member.getId()) == null){
            if(getMembers(member.getFName(), member.getLName()).isEmpty()){
                members.add(member);
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
