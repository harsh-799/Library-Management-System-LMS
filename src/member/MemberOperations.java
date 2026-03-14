package member;

import java.util.ArrayList;

public class MemberOperations {

    private final MemberRepo repo = new MemberRepo();

    public void removeMember(long memberId){
        if (repo.deleteMember(memberId)){
            System.out.println("\uD83D\uDDD1 Member with MemberID:  " + memberId+ " is deleted successfully.");
        } else {
            System.out.println("❌ No member found with that ID.");
        }
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
        if (repo.saveMember(m.getMemberId(), m.getFullName(), m.getUsername(), m.getPassword())){
            System.out.println("✅ Member created successfully!\n Below are the credentials: ");
            System.out.println("username : " + m.getUsername());
            System.out.println("password : " + m.getPassword());
        } else {
            System.out.println("There might be some issues in Saving the Member Data");
        }
    }

    public void updatePassword(String newPassword,Member currentMember){

    }
}
