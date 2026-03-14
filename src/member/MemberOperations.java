package member;

import java.util.ArrayList;

public class MemberOperations {

    private final MemberRepo repo = new MemberRepo();

    public Member deleteMember(long memberId){
        return null;
    }

    public void getAllMembers(){

    }

    public void printMemberDetails(Member val){
        System.out.println("MemberID: " + val.getMemberId());
        System.out.println("FullName: " + val.getFullName());
        System.out.println("userName: " + val.getUsername());
        System.out.println("Book Issued: " + val.getTotalBooksIssued());
    }

    public Member getMemberByID(long memberID){
       return null;
    }

    public boolean issueBookForMember(Member member, int bookId){
        return false;
    }

    public ArrayList<Integer> getIssued(Member member){
        return null;
    }

    public boolean returnBookForMember(Member member, int bookId){
        return false;
    }

    public void addMember(Member m){
    }

    public void updatePassword(String newPassword,Member currentMember){

    }
}
