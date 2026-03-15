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

    public Book searchBookForIssuedBooks(int bookId){
        Book searchedBook = repo.searchBookInDB(bookId);

        if (searchedBook != null){
            return searchedBook;
        }
        System.out.println("❌ No book found with that ID.");
        return null;
    }

    public void searchBookByAuthor(String authorName){
        ArrayList<Integer> authorBookCollN = repo.getBooksByAuthor(authorName);
        if (!authorBookCollN.isEmpty()){
            for (Integer bookID: authorBookCollN) {
                Book foundBook = repo.searchBookInDB(bookID);
                dashboard.bookDetailsPrinterMember(foundBook.getBookId(), foundBook.getTitle(), foundBook.getAuthor(), foundBook.getTotalQty(), foundBook.getAvailableQty());
            }
            return;
        }
        System.out.println("🔍 No Book with Author Name: " + authorName + " is found 📚");
    }

    public void searchBookByTitle(String bookTitle){
        ArrayList<Integer> bookIdsByTitle = repo.getBooksByBookTitle(bookTitle);
        if (!bookIdsByTitle.isEmpty()){
            for (Integer bookID: bookIdsByTitle) {
                Book foundBook = repo.searchBookInDB(bookID);
                dashboard.bookDetailsPrinterMember(foundBook.getBookId(), foundBook.getTitle(), foundBook.getAuthor(), foundBook.getTotalQty(), foundBook.getAvailableQty());
            }
            return;
        }
        System.out.println("🔍 No Book with Book Title: " + bookTitle + " is found 📚");
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
