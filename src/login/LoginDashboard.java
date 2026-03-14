package login;

import java.util.Scanner;

import admin.Admin;
import auth.AuthService;

public class LoginDashboard {
    private final Scanner sc = new Scanner(System.in);

    public Admin createAdmin(){
        System.out.println("⚠ No Admin Found!");
        System.out.println("🔐 Creating Admin Account...\n");
        Admin admin = new Admin();
        System.out.print("👤 Enter Admin Username: ");
        admin.setUsername(sc.next());
        System.out.print("🔑 Enter Admin Password: ");
        admin.setPassword(sc.next());

        System.out.println("✅ Admin Profile Created Successfully!\n");

        return admin;
    }

    public String[] collectUserCredentials(){
        System.out.println("🔐 Please Login to Continue\n");
        System.out.print("👤 Username: ");
        String userName = sc.next();

        System.out.print("🔑 Password: ");
        String password = sc.next();

        return new String[]{userName, password};
    }

    public void start() {
        System.out.println("\n=======================================");
        System.out.println("📚  LIBRARY MANAGEMENT SYSTEM  📚");
        System.out.println("=======================================\n");

        new AuthService().start();
    }
}
