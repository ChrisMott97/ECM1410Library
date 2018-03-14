import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Stores all basic information about a single Member.
 */
public class Member {
    private MemberFactory memberFactory;
    private int id;
    private String fName;
    private String lName;
    private LocalDate dateJoin;

    /**
     * Constructs a new Member with given parameters only if it's being read from a file (because the ID is known).
     *
     * @param id id (primary key) of the member.
     * @param fName first name of the member.
     * @param lName last name of the member.
     * @param dateJoin date that the member joined.
     */
    public Member(int id, String fName, String lName, LocalDate dateJoin){
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.dateJoin = dateJoin;
    }

    /**
     * Constructs a new Member with given parameters with id = -1 to ensure that it gets assigned a proper id once
     *  added to the list, based on previous ids.
     *
     * @param fName the first name of the member.
     * @param lName the last name of the member.
     * @param dateJoin date that the member joined.
     */
    public Member(String fName, String lName, LocalDate dateJoin){
        this.id = -1;
        this.fName = fName;
        this.lName = lName;
        this.dateJoin = dateJoin;
    }

    /**
     * Sets the dependencies that some functions in the member may require.
     *
     * @param memberFactory the member factory that has all methods required for manipulating members.
     */
    public void setDependencies(MemberFactory memberFactory){
        this.memberFactory = memberFactory;
    }

    /**
     * Getter for id.
     *
     * @return int id.
     */
    public int getId(){
        return id;
    }

    /**
     * Setter for id.
     *
     * @param id the id that member Id is to be set to.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for member first name.
     *
     * @return String first name.
     */
    public String getFName(){
        return fName;
    }

    /**
     * Getter for the member last name.
     *
     * @return String last name.
     */
    public String getLName(){
        return lName;
    }

    /**
     * Getter for the join date.
     *
     * @return LocalDate date joined.
     */
    public LocalDate getDateJoin(){
        return dateJoin;
    }
}
