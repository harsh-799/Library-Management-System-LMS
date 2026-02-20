package Auth;

import java.util.Scanner;
import Admin.AdminDashboard;
import User.Member;
import User.MemberDashboard;
import User.MemberRepo;
import java.util.ArrayList;

public class LoginSystem {

    Member loggedInMember;

    public String authenticateUser(){
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter username: ");
        String userName = sc.next();

        System.out.print("Enter password: ");
        String password = sc.next();

        Admin adminCred = new CredRepo().deserialize();
        ArrayList<Member> membersCred = new MemberRepo().deserialize();

        if (adminCred != null){
            if (adminCred.getUsername().equals(userName) && adminCred.getPassword().equals(password)){
                return "admin";
            }
            for (Member mem: membersCred){
                Member currMember = mem;
                if (currMember.username.equals(userName) && currMember.getPassword().equals(password)){
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
                new AdminDashboard().start();
                break;

            case "member":
                new MemberDashboard().start(loggedInMember);
                break;

            default:
                break;
        }
    }

    public void start() {
        String role = authenticateUser();
        if (!role.equals("invalid")){
            openDashboard(role);
        } else {
            System.out.println("Invalid credentials");
        }
    }
}
