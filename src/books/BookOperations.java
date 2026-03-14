package books;

import java.util.ArrayList;

public class BookOperations {

    private final BookRepo repo = new BookRepo();
    private final BookDashboard dashboard = new BookDashboard();

    public void searchBook(int bookId){
        Book searchedBook = repo.searchBookInDB(bookId);

        if (searchedBook != null){
            dashboard.bookDetailsPrinterMember(searchedBook.getBookId(), searchedBook.getTitle(), searchedBook.getAuthor(), searchedBook.getTotalQty(), searchedBook.getAvailableQty());
            return;
        }
        System.out.println("❌ No book found with that ID.");
    }

    public Book searchBookByTitle(String title){
        return null;
    }

    public ArrayList<Book> searchBookByAuthor(String author){
        return null;
    }

    public Book issueBook(int bookId){
        return null;
    }

    public Book returnBook(int bookId){
        return null;
    }

    public void viewAllBooks(String role){
        if (!repo.fetchAllBooks(role)){
            System.out.println("📚 Library is empty — no books found ❌");
        }
    }

    public void removeBook(int bookId){
        if (repo.deleteBook(bookId)){
            System.out.println("Book with BookID "+bookId+" removed from DB..");
        } else {
            System.out.println("❌ No book found with that ID.");
        }
    }

    public void addNewBook(Book book){
        if (repo.saveBook(book.getBookId(), book.getTitle(), book.getAuthor(), book.getTotalQty())){
            System.out.println("✅ Book added successfully!");
        }
    }
}
