package member;

import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MemberRepo {

    protected boolean saveMember(long memberID, String fullname, String username, String password) {
        try (
                Connection conn = DatabaseConnection.connectDB();
                PreparedStatement memberCredTable = conn.prepareStatement("INSERT INTO member_cred_table " +
                        "(Member_ID, Username, Password) VALUES (?, ?, ?)");
                PreparedStatement memberTable = conn.prepareStatement("INSERT INTO member_table " +
                        "(Member_ID, Fullname) VALUES (?, ?)")
        ) {
            memberCredTable.setLong(1,memberID);
            memberCredTable.setString(2,username);
            memberCredTable.setString(3,password);

            memberTable.setLong(1,memberID);
            memberTable.setString(2, fullname);

            int rowsAffectedMemCred = memberCredTable.executeUpdate();
            int rowsAffectedMemTable = memberTable.executeUpdate();

            return rowsAffectedMemCred > 0 && rowsAffectedMemTable > 0;

        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    protected boolean deleteMember(long memberID){
        try (
                Connection conn = DatabaseConnection.connectDB();
                PreparedStatement memberCredTable = conn.prepareStatement("DELETE FROM member_cred_table " +
                        "WHERE Member_ID = ?");
                PreparedStatement memberTable = conn.prepareStatement("DELETE FROM member_table " +
                        "WHERE Member_ID = ?")
        ) {

            memberCredTable.setLong(1,memberID);
            memberTable.setLong(1,memberID);

            int rowsAffectedMemCred = memberCredTable.executeUpdate();
            int rowsAffectedMemTable = memberTable.executeUpdate();

            return rowsAffectedMemCred > 0 && rowsAffectedMemTable > 0;

        }catch (SQLException e){
            System.out.println(e);
        }
        return false;
    }
}
