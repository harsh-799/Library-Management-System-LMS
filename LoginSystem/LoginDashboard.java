package LoginSystem;

import java.io.*;
import java.util.Scanner;

import Auth.Admin;
import Auth.LoginSystem;

public class LoginDashboard {

    public Admin createAdmin(){
        Scanner sc = new Scanner(System.in);
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

    public void start() {
        System.out.println("\n=======================================");
        System.out.println("📚  LIBRARY MANAGEMENT SYSTEM  📚");
        System.out.println("=======================================\n");

        new LoginSystem().start();
    }
}
