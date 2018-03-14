import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Contains all functions involved in manipulating Member objects.
 * Includes reading and writing to the storage text file.
 */
public class MemberFactory {
    private List<Member> members;

    public BookFactory bookFactory;
    public BookLoanFactory bookLoanFactory;


    /**
     * Used to set dependencies that the factory requires.
     * Does things that can't be done upon construction due to the fact other factories have to be constructed first.
     *
     * @param members list of members to pass by reference to the factory so the functions can use it.
     * @param bookFactory factory with functions mainly used to manipulate members but can be useful in other factories.
     * @param bookLoanFactory factory with functions mainly used to manipulate members but can be useful in
     *                        other factories.
     */
    public void setDependencies(List<Member> members, BookFactory bookFactory, BookLoanFactory bookLoanFactory){
        this.bookFactory = bookFactory;
        this.members = members;
        this.bookLoanFactory = bookLoanFactory;
    }

    /**
     * Getter for members list.
     *
     * @return the full list of members.
     */
    public List<Member> getMembers() {
        return members;
    }

    /**
     * Reads from the given file and creates a list of members to be accessible by the rest of the program.
     *
     * @param file the file path to the file that needs to be read.
     * @return the new list of members created from the text file.
     */
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

    /**
     * Used to write the members list to the given text file.
     *
     * @param file the file to write to.
     */
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

    /**
     * Used to find members given a first and last name.
     * Can return multiple if multiple people with the same name.
     *
     * @param fName first name of member to be found.
     * @param lName last name of the member to found.
     * @return the list of member(s) found.
     */
    public List<Member> getMembers(String fName, String lName){
        fName = fName.toLowerCase();
        lName = lName.toLowerCase();
        List<Member> results = new ArrayList<>();
        for (Member member: members) {
            if (member.getFName().toLowerCase().contains(fName) && member.getLName().toLowerCase().contains(lName)) {
                results.add(member);
            }
        }
        return results;
    }

    /**
     * Used if multiple members need to be reduced to a single member.
     * User picks an ID to narrow the search.
     *
     * @param members the list of members to choose between.
     * @return the member that's been chosen or null if none chosen.
     */
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

    /**
     * Used to find a member by their ID
     *
     * @param id the id to find by.
     * @return the member found or null if no member found.
     */
    public Member getMemberById(int id){
        for (Member member : members) {
            if(member.getId() == id){
                return member;
            }
        }
        return null;
    }

    /**
     * Adds a new instance of Member to the members list.
     *
     * @param member the member instance to add.
     * @return boolean as to whether or not the function was successful.
     */
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
