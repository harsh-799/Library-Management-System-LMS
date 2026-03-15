package member;

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
        if (!repo.viewAllMembers()){
            System.out.println("📚 Library has no members 😔");
        }
    }


    public void getMemberByID(long memberID){
        if (!repo.getMember(memberID)){
            System.out.println("❌ No member found with that ID.");
        }
    }

    public Member syncMemberOnLogin(Member member){
        Member syncedMember = repo.updateMemberOnLogin(member);

        if (syncedMember != null) return syncedMember;
        System.out.println("Failed to Sync..");
        return member;
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
        if (repo.updateMemberPassword(newPassword, currentMember)){
            System.out.println("✅ Password updated successfully!");
        } else {
            System.out.println("❌ Incorrect old password.");
        }
    }
}
