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
 *
 */
public class Member {
    private MemberFactory memberFactory;
    private int id;
    private String fName;
    private String lName;
    private LocalDate dateJoin;

    /**
     * @param id
     * @param fName
     * @param lName
     * @param dateJoin
     */
    public Member(int id, String fName, String lName, LocalDate dateJoin){
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.dateJoin = dateJoin;
    }

    /**
     * @param fName
     * @param lName
     * @param dateJoin
     */
    public Member(String fName, String lName, LocalDate dateJoin){
        this.id = -1;
        this.fName = fName;
        this.lName = lName;
        this.dateJoin = dateJoin;
    }

    /**
     * @param memberFactory
     */
    public void setDependencies(MemberFactory memberFactory){
        this.memberFactory = memberFactory;
    }

    /**
     * @return
     */
    public int getId(){
        return id;
    }

    /**
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return
     */
    public String getFName(){
        return fName;
    }

    /**
     * @return
     */
    public String getLName(){
        return lName;
    }

    /**
     * @return
     */
    public LocalDate getDateJoin(){
        return dateJoin;
    }
}
