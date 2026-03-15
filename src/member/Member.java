package member;

import java.util.ArrayList;

public class Member{

    private long memberId;
    private String fullName;
    private String username;
    private String password;
    private final String role = "member";
    private int totalBooksIssued;
    private ArrayList<Integer> memberIssuedBookIds;

    public Member() {
        this.memberIssuedBookIds = new ArrayList<>();
    }

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

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public ArrayList<Integer> getMemberIssuedBookIds() {
        return memberIssuedBookIds;
    }

    public void setMemberIssuedBookIds(int bookID) {
        this.memberIssuedBookIds.add(bookID);
    }

    public Member(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.memberId = generateId();
        this.totalBooksIssued = 0;
    }
}
