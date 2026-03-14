package books;

import util.DatabaseConnection;

import java.sql.*;

public class BookRepo {

    private final BookDashboard dashboardUi = new BookDashboard();

    protected boolean saveBook(int bookID, String title, String author, int totalQty){
        try (
                Connection conn = DatabaseConnection.connectDB();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO book_table " +
                        "(Book_ID, Title, Author, Avail_QTY, Total_QTY) VALUES " +
                        "(?, ?, ?, ?, ?)");
        ) {
            ps.setInt(1, bookID);
            ps.setString(2, title);
            ps.setString(3, author);
            ps.setInt(4, totalQty);
            ps.setInt(5, totalQty);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        }catch (SQLException e){
            if (e.getErrorCode() == 1062){
                System.out.println("Book with BookID "+bookID+ " already exists in DB.");
            }
        }
        return false;
    }

    public boolean deleteBook(int bookID){
        try (Connection conn = DatabaseConnection.connectDB();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM book_table WHERE " +
                    "Book_ID = ?")
        ) {
            ps.setInt(1,bookID);
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        }catch (SQLException e){
            System.out.println(e);
        }
        return false;
    }

    public Book searchBookInDB(int bookID){
        try (
                Connection conn = DatabaseConnection.connectDB();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM book_table " +
                        "WHERE Book_ID = ?")
        ) {
            ps.setInt(1, bookID);

            try (ResultSet rs = ps.executeQuery()){
                Book returnedBook = new Book(rs.getInt("Book_ID"), rs.getString("Title"), rs.getString("Author"), rs.getInt("Total_QTY"));

                return returnedBook;
            }
        }catch (SQLException e){
            System.out.println(e);
        }
        return null;
    }

    public Boolean fetchAllBooks(){
        boolean hasBook = false;
        try (
                Connection conn = DatabaseConnection.connectDB();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM book_table");
        ) {
            while (rs.next()){
                dashboardUi.bookDetailsPrinterAdmin(rs.getInt("Book_ID"), rs.getString("Title"), rs.getString("Author"), rs.getInt("Total_Qty"), rs.getInt("Avail_Qty"));
                hasBook = true;
            }
        }catch (SQLException e){
            System.out.println(e);
        }
        if (hasBook) return true;
        return false;
    }
}
