package LoginSystem;

import java.io.*;
import java.util.Scanner;

import Auth.Admin;
import Auth.CredRepo;
import Auth.LoginSystem;

public class LoginDashboard {

    public boolean adminExists(){
        File admin = new File("Auth/Credentials.ser");
        return admin.exists();
    }

    public Admin createAdmin(){
        Scanner sc = new Scanner(System.in);
        System.out.println("ADMIN NOT FOUND. \n🔃 Creating Admin Account");
        Admin admin = new Admin();
        System.out.print("Enter Username for Admin: ");
        admin.setUsername(sc.next());
        System.out.print("Enter Password for Admin: ");
        admin.setPassword(sc.next());

        System.out.println("Admin Profile Created Successfully..");

        return admin;
    }

    public void start() {
        System.out.println("--- Welcome to our Library Management System -----");

        if (!adminExists()){
            Admin adminCredentials = createAdmin();
            new CredRepo().serialize(adminCredentials);
        }

        new LoginSystem().start();
    }
}
