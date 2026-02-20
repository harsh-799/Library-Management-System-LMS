package Member;

import java.io.*;
import java.util.ArrayList;

public class Member implements Serializable{

    private static final long serialVersionUID = 1L;

    private long memberId;
    private String fullName;
    private String username;
    private String password;
    private String role;
    private int totalBooksIssued;
    private ArrayList<Integer> issuedBookIds;

    public long getMemberId() {
        return memberId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public int getTotalBooksIssued() {
        return totalBooksIssued;
    }

    public void setTotalBooksIssued(int totalBooksIssued) {
        this.totalBooksIssued = totalBooksIssued;
    }

    public ArrayList<Integer> getIssuedBookIds() {
        return issuedBookIds;
    }

    public void setIssuedBookIds(int id) {
        this.issuedBookIds.add(id);
    }

    private long generateId(){
        return Math.abs((fullName + username + password).hashCode());
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public Member(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.memberId = generateId();
        this.role = "Member";
        this.totalBooksIssued = 0;
        this.issuedBookIds = new ArrayList<>();
    }
}
