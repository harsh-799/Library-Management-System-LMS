package books;

import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;

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
                if (rs.next()) {
                    Book foundBook = new Book(rs.getInt("Book_ID"), rs.getString("Title"), rs.getString("Author"), rs.getInt("Total_QTY"));
                    foundBook.setAvailableQty(rs.getInt("Avail_Qty"));
                    return foundBook;
                }
            }
        }catch (SQLException e){
            System.out.println(e);
            System.out.println(e.getSQLState());
            System.out.println(e.getErrorCode());
        }
        return null;
    }

    public Boolean fetchAllBooks(String role){
        boolean hasBook = false;
        try (
                Connection conn = DatabaseConnection.connectDB();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM book_table");
        ) {
            while (rs.next()){
                if (role.equals("admin")){
                    dashboardUi.bookDetailsPrinterAdmin(rs.getInt("Book_ID"), rs.getString("Title"), rs.getString("Author"), rs.getInt("Total_Qty"), rs.getInt("Avail_Qty"));
                } else {
                    dashboardUi.bookDetailsPrinterMember(rs.getInt("Book_ID"), rs.getString("Title"), rs.getString("Author"), rs.getInt("Total_Qty"), rs.getInt("Avail_Qty"));
                }

                return true;
            }
        }catch (SQLException e){
            System.out.println(e);
        }
        if (hasBook) return true;
        return false;
    }

    public ArrayList<Integer> getBooksByAuthor(String authorName){
        ArrayList<Integer> authorBookCollection = new ArrayList<>();
        try (
                Connection conn = DatabaseConnection.connectDB();
                PreparedStatement ps = conn.prepareStatement("SELECT Book_id from book_table " +
                        "WHERE Author = ?")
        ) {
            ps.setString(1, authorName);
            try (
                    ResultSet rs = ps.executeQuery()
            ) {
                while (rs.next()){
                    authorBookCollection.add(rs.getInt("Book_ID"));
                }
            }
        } catch (SQLException e){
            System.out.println(e);
        }
        return authorBookCollection;
    }

    public ArrayList<Integer> getBooksByBookTitle(String bookTitle){
        ArrayList<Integer> bookIdsByTitle = new ArrayList<>();
        try (
                Connection conn = DatabaseConnection.connectDB();
                PreparedStatement ps = conn.prepareStatement("SELECT Book_id from book_table " +
                        "WHERE Title = ?")
        ) {
            ps.setString(1, bookTitle);
            try (
                    ResultSet rs = ps.executeQuery()
            ) {
                while (rs.next()){
                    bookIdsByTitle.add(rs.getInt("Book_ID"));
                }
            } catch (SQLException e){
                System.out.println(e);
            }
        } catch (SQLException e){
            System.out.println(e);
        }
        return bookIdsByTitle;
    }
}
