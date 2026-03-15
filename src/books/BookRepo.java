package books;

import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class BookRepo {

    private final BookDashboard dashboardUi = new BookDashboard();

    protected boolean saveBook(int bookID, String title, String author, int totalQty) {
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
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Book with BookID " + bookID + " already exists in DB.");
            }
        }
        return false;
    }

    public boolean deleteBook(int bookID) {
        try (Connection conn = DatabaseConnection.connectDB();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM book_table WHERE " +
                     "Book_ID = ?")
        ) {
            ps.setInt(1, bookID);
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public Book searchBookInDB(int bookID) {
        try (
                Connection conn = DatabaseConnection.connectDB();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM book_table " +
                        "WHERE Book_ID = ?")
        ) {
            ps.setInt(1, bookID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Book foundBook = new Book(rs.getInt("Book_ID"), rs.getString("Title"), rs.getString("Author"), rs.getInt("Total_QTY"));
                    foundBook.setAvailableQty(rs.getInt("Avail_Qty"));
                    return foundBook;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println(e.getSQLState());
            System.out.println(e.getErrorCode());
        }
        return null;
    }

    public Boolean fetchAllBooks(String role) {
        boolean hasBook = false;
        try (
                Connection conn = DatabaseConnection.connectDB();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM book_table");
        ) {
            while (rs.next()) {
                if (role.equals("admin")) {
                    dashboardUi.bookDetailsPrinterAdmin(rs.getInt("Book_ID"), rs.getString("Title"), rs.getString("Author"), rs.getInt("Total_Qty"), rs.getInt("Avail_Qty"));
                } else {
                    dashboardUi.bookDetailsPrinterMember(rs.getInt("Book_ID"), rs.getString("Title"), rs.getString("Author"), rs.getInt("Total_Qty"), rs.getInt("Avail_Qty"));
                }
                hasBook = true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        if (hasBook) return true;
        return false;
    }

    public ArrayList<Integer> getBooksByAuthor(String authorName) {
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
                while (rs.next()) {
                    authorBookCollection.add(rs.getInt("Book_ID"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return authorBookCollection;
    }

    public ArrayList<Integer> getBooksByBookTitle(String bookTitle) {
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
                while (rs.next()) {
                    bookIdsByTitle.add(rs.getInt("Book_ID"));
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return bookIdsByTitle;
    }

    public int createBookIssueEntry(int bookId, long memberId, LocalDate issuedDate, LocalDate returnDate) {
        try (
                Connection conn = DatabaseConnection.connectDB();
                PreparedStatement ps = conn.prepareStatement("SELECT bi.Book_Id, bi.Avail_Qty " +
                        "FROM book_table bi WHERE bi.book_Id = ?");
                PreparedStatement ps1 = conn.prepareStatement("INSERT INTO book_issued " +
                        "(Member_Id, Book_Id, Issued_Date, Return_Date) VALUES " +
                        "(?, ?, ?, ?)");
                PreparedStatement ps2 = conn.prepareStatement("UPDATE book_table SET Avail_Qty = " +
                        "Avail_Qty - 1 WHERE Book_Id = ?")
        ) {
            ps.setLong(1, bookId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int bookInStock = rs.getInt("Avail_Qty");

                    if (bookInStock != 0) {
                        ps1.setLong(1, memberId);
                        ps1.setInt(2, bookId);
                        ps1.setDate(3, Date.valueOf(issuedDate));
                        ps1.setDate(4, Date.valueOf(returnDate));
                        ps2.setInt(1, bookId);
                        int rowsAffected = ps1.executeUpdate();
                        int rowsAffectedBookTable = ps2.executeUpdate();

                        if (rowsAffected != 0 && rowsAffectedBookTable != 0) {
                            return 0;
                        }
                    } else {
                        return -1;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return 1;
    }

    public int createBookReturnEntry(int bookId, long memberId) {
        try (
                Connection conn = DatabaseConnection.connectDB();
                PreparedStatement ps = conn.prepareStatement("DELETE FROM book_issued " +
                        "WHERE Member_Id = ? AND Book_Id = ?");
                PreparedStatement ps1 = conn.prepareStatement("UPDATE book_table SET " +
                        "Avail_Qty = Avail_Qty + 1 WHERE Book_Id = ?")
        ) {
            ps.setLong(1, memberId);
            ps.setInt(2, bookId);
            ps1.setInt(1, bookId);

            int rowsAffected = ps.executeUpdate();
            int rowsAffectedUpdate = ps1.executeUpdate();

            if (rowsAffected != 0 && rowsAffectedUpdate != 0){
                return 0;
            }
        }catch (SQLException e){
            System.out.println(e);
        }
        return -1;
    }
}
