package Member;

import java.util.ArrayList;
import java.util.ListIterator;

public class MemberOperations {

    private final MemberRepo repo = new MemberRepo();

    public Member deleteMember(long memberId){
        ArrayList<Member> list = repo.deserialize();

        if (list.isEmpty()){
            System.out.println("No Member records in the Library.");
            return null;
        }

        ListIterator<Member> it = list.listIterator();
        while(it.hasNext()){
            Member currMember = it.next();
            if (currMember.getMemberId() == memberId){
                it.remove();
                repo.serialize(list);
                return currMember;
            }
        }
        return null;
    }

    public void getAllMembers(){
        ArrayList<Member> list = repo.deserialize();

        if (list.isEmpty()){
            System.out.println("No Member records in the Library.");
            return;
        } else {
            for (Member val : list){
                System.out.println("MemberID: " + val.getMemberId());
                System.out.println("FullName: " + val.getFullName());
                System.out.println("userName: " + val.getUsername());
                System.out.println("Book Issued: " + val.getTotalBooksIssued());
                System.out.println();
            }
        }
    }

    public void printMemberDetails(Member val){
        System.out.println("MemberID: " + val.getMemberId());
        System.out.println("FullName: " + val.getFullName());
        System.out.println("userName: " + val.getUsername());
        System.out.println("Book Issued: " + val.getTotalBooksIssued());
    }

    public Member getMemberByID(long memberID){
        ArrayList<Member> list = repo.deserialize();

        if (list.isEmpty()){
            System.out.println("No records found..");
            return null;
        }

        for (Member val: list){
            if (val.getMemberId() == memberID){
                return val;
            }
        }

        System.out.println("No Member with MemberID " + memberID + " is found.");
        return null;
    }

    public boolean issueBookForMember(Member member, int bookId){
        ArrayList<Member> list = repo.deserialize();

        for (Member val: list){
            if (val.getMemberId() == member.getMemberId()){
                val.getIssuedBookIds().add(bookId);
                val.setTotalBooksIssued(val.getIssuedBookIds().size());
                repo.serialize(list);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Integer> getIssued(Member member){
        ArrayList<Member> list = repo.deserialize();
        ListIterator<Member> memberIterator = list.listIterator();
        while (memberIterator.hasNext()){
            Member currMember = memberIterator.next();
            if (currMember.getMemberId() == member.getMemberId()){
                return currMember.getIssuedBookIds();
            }
        }
        return new ArrayList<>();
    }

    public boolean returnBookForMember(Member member, int bookId){
        ArrayList<Member> list = repo.deserialize();

        for (Member val: list){
            if (val.getMemberId() == member.getMemberId()){
                if (val.getIssuedBookIds().remove(Integer.valueOf(bookId))){
                    val.setTotalBooksIssued(val.getIssuedBookIds().size());
                    repo.serialize(list);
                    return true;
                }
            }
        }
        return false;
    }

    public void addMember(Member m){
        ArrayList<Member> list = repo.deserialize();
        list.add(m);

        repo.serialize(list);

        System.out.println("✅ Member created successfully!\n Below are the credentials: ");
        System.out.println("username : " + m.getUsername());
        System.out.println("password : " + m.getPassword());
    }

    public void updatePassword(String newPassword,Member currentMember){
        ArrayList<Member> list = repo.deserialize();
        ListIterator<Member> it = list.listIterator();
        while (it.hasNext()){
            Member targetMember = it.next();
            if (targetMember.getMemberId() == currentMember.getMemberId()){
                targetMember.setPassword(newPassword);
                break;
            }
        }
        repo.serialize(list);
    }
}
