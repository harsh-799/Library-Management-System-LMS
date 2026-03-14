package auth;

import admin.AdminDashboard;
import member.Member;
import member.MemberDashboard;
import admin.Admin;
import login.LoginDashboard;
import util.DatabaseInitializer;

public class AuthService {

    private Admin loggedInAdmin;
    private Member loggedInMember;
    private DatabaseInitializer dbInitializer;
    private AuthRepo repo = new AuthRepo();

    public void setAdminDetails(Admin admin){
        if (repo.saveAdmin(admin)){
            System.out.println("Admin Added Successfully.");
        }
    }

    public String authenticateUser(String username, String password){
        // Checking the Admin Credentials
        loggedInAdmin = repo.getAdmin();

        if (loggedInAdmin != null && loggedInAdmin.getUsername().equals(username) && loggedInAdmin.getPassword().equals(password)){
            return "admin";
        }

        // Checking the Member Credentials
        loggedInMember = repo.authenticateMember(username, password);
        if (loggedInMember != null) return "member";

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

        LoginDashboard dashboardUI = new LoginDashboard();

        // For every Run, Create Essentials Table if it doesn't exists in DB.
        dbInitializer = new DatabaseInitializer();
        dbInitializer.initializeAllTables();

        // Checks, If there's any admin in the LMS from earlier.
        Admin adminCred = repo.getAdmin();

        if (adminCred == null){
            loggedInAdmin = dashboardUI.createAdmin();
            setAdminDetails(loggedInAdmin);
            loggedInAdmin = null; // After saving Admin Records Reset Admin to null.
        }

        while (true){
            String[] userCred = dashboardUI.collectUserCredentials();
            String role = authenticateUser(userCred[0], userCred[1]);
            if (!role.equals("invalid")){
                openDashboard(role);
                break;
            } else {
                System.out.println("❌ Invalid username or password.\n");
            }
        }
    }
}
