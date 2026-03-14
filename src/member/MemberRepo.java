package member;

import util.DatabaseConnection;

import java.sql.*;

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
                PreparedStatement memberCredTable = conn.prepareStatement("DELETE FROM" +
                                                                        "member_cred_table " +
                                                                        "WHERE Member_ID = ?");
                PreparedStatement memberTable = conn.prepareStatement("DELETE FROM" +
                                                                        "member_table " +
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

    public boolean viewAllMembers(){
        boolean hasRecords = false;
        MemberDashboard viewMemberUi = new MemberDashboard();
        try (Connection conn = DatabaseConnection.connectDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT mt.Member_ID, mt.Fullname, " +
                     "mc.Username, COUNT(bi.Member_ID) AS Book_Issued " +
                     "FROM member_table mt " +
                     "INNER JOIN member_cred_table mc " +
                     "ON mt.Member_ID = mc.Member_ID " +
                     "LEFT JOIN book_issued bi " +
                     "ON mt.Member_ID = bi.Member_ID " +
                     "GROUP BY mt.Member_ID, mt.Fullname, mc.Username")
        ) {

            while (rs.next()){
                viewMemberUi.printMemberDetails(rs.getLong("Member_ID"),rs.getString("Fullname"),rs.getString("Username"),rs.getString("Book_Issued"));
                hasRecords = true;
            }
        }catch (SQLException e){
            System.out.println(e);
        }
        if (hasRecords) return true;
        return false;
    }

    public boolean getMember(long memberID){
        MemberDashboard viewMemberUi = new MemberDashboard();
        boolean hasMember = false;
        try (
                Connection conn = DatabaseConnection.connectDB();
                PreparedStatement ps = conn.prepareStatement("SELECT mt.Member_ID, " +
                        "mt.Fullname, " +
                        "mc.Username, COUNT(bi.Member_ID) AS Book_Issued " +
                        "FROM member_table mt " +
                        "INNER JOIN member_cred_table mc " +
                        "ON mt.Member_ID = mc.Member_ID " +
                        "LEFT JOIN book_issued bi " +
                        "ON mt.Member_ID = bi.Member_ID " +
                        "WHERE mt.Member_ID = ? " +
                        "GROUP BY mt.Member_ID, mt.Fullname, mc.Username")
        ) {
            ps.setLong(1,memberID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                viewMemberUi.printMemberDetails(rs.getLong("Member_ID"), rs.getString("Fullname"), rs.getString("Username"), rs.getString("Book_Issued"));
                hasMember = true;
            }
        }catch (SQLException e){
            System.out.println(e);
        }
        if (hasMember) return true;
        return false;
    }
}
