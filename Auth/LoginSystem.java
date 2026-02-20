package Auth;

import java.util.Scanner;
import Admin.AdminDashboard;
import Member.Member;
import Member.MemberDashboard;
import Member.MemberRepo;
import java.util.ArrayList;

public class LoginSystem {

    Member loggedInMember;

    public String authenticateUser(){
        Scanner sc = new Scanner(System.in);

        System.out.println("🔐 Please Login to Continue\n");
        System.out.print("👤 Username: ");
        String userName = sc.next();

        System.out.print("🔑 Password: ");
        String password = sc.next();

        Admin adminCred = new CredRepo().deserialize();
        ArrayList<Member> membersCred = new MemberRepo().deserialize();

        if (adminCred != null){
            if (adminCred.getUsername().equals(userName) && adminCred.getPassword().equals(password)){
                return "admin";
            }
            for (Member mem: membersCred){
                Member currMember = mem;
                if (currMember.getUsername().equals(userName) && currMember.getPassword().equals(password)){
                    loggedInMember = currMember;
                    return "member";
                }
            }
        }
        return "invalid";
    }

    public void openDashboard(String role){
        switch (role){
            case "admin":
                System.out.println("🛠 Logged in as Admin\n");
                new AdminDashboard().start();
                break;

            case "member":
                System.out.println("👤 Logged in as Member\n");
                new MemberDashboard().start(loggedInMember);
                break;

            default:
                break;
        }
    }

    public void start() {
        while (true){
            String role = authenticateUser();
            if (!role.equals("invalid")){
                openDashboard(role);
                break;
            } else {
                System.out.println("❌ Invalid username or password.\n");
            }
        }

    }
}
