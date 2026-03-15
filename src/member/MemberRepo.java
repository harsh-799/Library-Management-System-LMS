package member;

import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;

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

    public Member updateMemberOnLogin(Member member){
        try (
                Connection conn = DatabaseConnection.connectDB();
                PreparedStatement ps = conn.prepareStatement("SELECT mt.Fullname, COUNT(bi.Member_ID) " +
                        "AS Book_Issued " +
                        "FROM member_table mt LEFT JOIN book_issued bi " +
                        "ON mt.Member_ID = bi.Member_ID " +
                        "WHERE mt.Member_ID = ? " +
                        "GROUP BY mt.Fullname");
                PreparedStatement ps1 = conn.prepareStatement("SELECT Book_ID from " +
                        "Book_Issued WHERE Member_ID = ?")
        ) {
            ps.setLong(1, member.getMemberId());
            ps1.setLong(1,member.getMemberId());

            try (
                    ResultSet rs = ps.executeQuery();
                    ResultSet rs1 = ps1.executeQuery();
            ) {
                rs.next(); // Since only one data will be there so no while loop, and it's 100% Sure that MemberId will be correct unless no body touched DB and chnaged memberID.
                member.setFullName(rs.getString("Fullname"));
                member.setTotalBooksIssued(rs.getInt("Book_Issued"));

                while (rs1.next()){
                    member.setMemberIssuedBookIds(rs1.getInt("Book_ID"));
                }
                return member;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public boolean updateMemberPassword(String newPassword, Member member){
        try (
                Connection conn = DatabaseConnection.connectDB();
                PreparedStatement ps = conn.prepareStatement("UPDATE member_cred_table SET " +
                        "Password = ? WHERE Member_ID = ?")
        ) {
            ps.setString(1,newPassword);
            ps.setLong(2,member.getMemberId());

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        }catch (SQLException e){
            System.out.println(e);
        }
        return false;
    }
}
