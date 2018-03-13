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
    private MemberFactory memberFactory;
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

    public void setDependencies(MemberFactory memberFactory){
        this.memberFactory = memberFactory;
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
}
