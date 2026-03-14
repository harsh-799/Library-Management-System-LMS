package auth;

import admin.Admin;
import member.Member;
import util.DatabaseConnection;
import java.sql.*;

public class AuthRepo {

    public Admin getAdmin() {
        try (
                Connection conn = DatabaseConnection.connectDB();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM admin_table");
        ) {

            if (rs.next()) {
                Admin loggedInAdmin = new Admin();
                loggedInAdmin.setUsername(rs.getString("Username"));
                loggedInAdmin.setPassword(rs.getString("Password"));
                return loggedInAdmin;
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public boolean saveAdmin(Admin admin) {
        try (
                Connection conn = DatabaseConnection.connectDB();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO admin_table (Username, Password) VALUES (?, ?)");
        ) {

            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getPassword());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public Member authenticateMember(String username, String password) {
        try (
                Connection conn = DatabaseConnection.connectDB();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM member_cred_table WHERE Username=? AND Password=?")
        ) {

            ps.setString(1, username);
            ps.setString(2, password);

            try(ResultSet rs = ps.executeQuery()){

                if (rs.next()) {
                    Member loggedInMember = new Member();
                    loggedInMember.setMemberId(rs.getLong("Member_ID"));
                    loggedInMember.setUsername(rs.getString("Username"));
                    loggedInMember.setPassword(rs.getString("Password"));
                    return loggedInMember;
                }

            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
}

