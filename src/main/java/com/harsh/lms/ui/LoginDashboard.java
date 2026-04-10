package com.harsh.lms.ui;

import java.util.Scanner;

import com.harsh.lms.model.Admin;
import com.harsh.lms.model.Member;
import com.harsh.lms.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginDashboard {
    private final Scanner sc = new Scanner(System.in);
    private final AuthService authService;
    private final AdminDashboard adminDashboard;
    private final MemberDashboard memberDashboard;

    @Autowired
    public LoginDashboard(AuthService authService, AdminDashboard adminDashboard, MemberDashboard memberDashboard) {
        this.authService = authService;
        this.adminDashboard = adminDashboard;
        this.memberDashboard = memberDashboard;
    }

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

    private void openDashboard(Object user){
        if (user instanceof Admin) {
            System.out.println("🛠 Logged in as Admin\n");
            adminDashboard.start((Admin) user);
        } else if (user instanceof Member) {
            System.out.println("👤 Logged in as Member\n");
            memberDashboard.start((Member) user);
        }
    }

    public void start() {
        System.out.println("\n=======================================");
        System.out.println("📚  LIBRARY MANAGEMENT SYSTEM  📚");
        System.out.println("=======================================\n");

        if (!authService.hasAdminRecords()) {
            Admin createdAdmin = createAdmin();
            authService.setAdminDetails(createdAdmin);
            System.out.println("Admin Added Successfully.");
        }

        while (true){
            String userCredentials[] = collectUserCredentials();
            Object user = authService.authenticateUser(userCredentials[0], userCredentials[1]);
            if (user != null){
                openDashboard(user);
                break;
            } else {
                System.out.println("❌ Invalid username or password.\n");
            }
        }

    }
}