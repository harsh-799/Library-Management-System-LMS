package LoginSystem;

import java.io.*;
import java.util.Scanner;

import Auth.Admin;
import Auth.CredRepo;
import Auth.LoginSystem;

public class LoginDashboard {

    private static final String FILE_PATH = "Auth/Credentials.ser";

    public boolean adminExists(){
        File admin = new File(FILE_PATH);
        return admin.exists();
    }

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

        if (!adminExists()){
            Admin adminCredentials = createAdmin();
            new CredRepo().serialize(adminCredentials);
        }

        new LoginSystem().start();
    }
}
