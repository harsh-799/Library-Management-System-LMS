package Books;

import java.util.ArrayList;
import java.util.ListIterator;

public class BookOperations {

    private final BookRepo repo = new BookRepo();

    public Book searchBook(int bookId){
        ArrayList<Book> bookLists = repo.deserializeData();
        if (bookLists.isEmpty()){
            System.out.println("Library is empty");
            return null;
        }

        for (Book book : bookLists){
            if (book.getBookId() == bookId){
                return book;
            }
        }
        System.out.println("No Book with Book ID: "+bookId + " is found.");
        return null;
    }

    public Book searchBookByTitle(String title){
        ArrayList<Book> bookLists = repo.deserializeData();
        if (bookLists.isEmpty()){
            System.out.println("Library is empty");
            return null;
        }

        for (Book book: bookLists){
            if (book.getTitle().equalsIgnoreCase(title)) return book;
        }
        System.out.println("No Book with Title: "+title + " is found.");
        return null;
    }

    public ArrayList<Book> searchBookByAuthor(String author){
        ArrayList<Book> bookLists = repo.deserializeData();
        if (bookLists.isEmpty()){
            System.out.println("Library is empty");
            return new ArrayList<>();
        }
        ArrayList<Book> searchResults = new ArrayList<>();
        for (Book book : bookLists){
            if (book.getAuthor().equalsIgnoreCase(author)) searchResults.add(book);
        }
        return searchResults;
    }

    public Book issueBook(int bookId){
        ArrayList<Book> bookLists = repo.deserializeData();
        if (bookLists.isEmpty()){
            System.out.println("Library is empty");
            return null;
        }

        ListIterator<Book> it = bookLists.listIterator();

        while (it.hasNext()){
            Book currBook = it.next();
            if (currBook.getBookId() == bookId){
                if (currBook.getAvailableQty() > 0){
                    currBook.setAvailableQty(currBook.getAvailableQty() - 1);
                    it.set(currBook);
                    repo.serializeData(bookLists);
                    return currBook;
                }
                System.out.println("All Books are issued already.");
                return null;
            }
        }
        System.out.println("❌ Book not found.");
        return null;
    }

    public Book returnBook(int bookId){
        ArrayList<Book> bookLists = repo.deserializeData();
        if (bookLists.isEmpty()){
            System.out.println("Library is empty");
            return null;
        }
        ListIterator<Book> it = bookLists.listIterator();

        while (it.hasNext()){
            Book currBook = it.next();
            if (currBook.getBookId() == bookId && currBook.getAvailableQty() < currBook.getTotalQty()) {
                currBook.setAvailableQty(currBook.getAvailableQty() + 1);
                it.set(currBook);
                repo.serializeData(bookLists);
                return currBook;
            }
        }

        System.out.println("❌ Invalid Book ID or book not found in DB.");
        return null;
    }

    public void viewAllBooksUser(){
        ArrayList<Book> bookLists = repo.deserializeData();
        if (bookLists.isEmpty()){
            System.out.println("Library is empty");
            return;
        }
        for (Book currBook : bookLists){
            System.out.println("Book ID: "+currBook.getBookId());
            System.out.println("Book Title: "+currBook.getTitle());
            System.out.println("Book Author: "+currBook.getAuthor());
            if (currBook.getAvailableQty() != 0) System.out.println("Status: Available ");
            else System.out.println("Status: Out of Stock");
            System.out.println("-----");
        }
    }

    public void viewAllBooksAdmin(){
        ArrayList<Book> bookLists = repo.deserializeData();
        if (bookLists.isEmpty()){
            System.out.println("Library is empty");
            return;
        }

        ListIterator<Book> it = bookLists.listIterator();

        while (it.hasNext()){
            Book currBook = it.next();
            System.out.println("Book ID: "+currBook.getBookId());
            System.out.println("Book Title: "+currBook.getTitle());
            System.out.println("Book Author: "+currBook.getAuthor());
            System.out.println("Book TotalQty: "+currBook.getTotalQty());
            System.out.println("Book AvailQty: "+currBook.getAvailableQty());
            System.out.println("------");
        }
    }

    public Book removeBook(int bookId){
        ArrayList<Book> bookLists = repo.deserializeData();
        if (bookLists.isEmpty()){
            System.out.println("Library is empty");
            return null;
        }
        ListIterator<Book> it = bookLists.listIterator();

        while (it.hasNext()){
            Book currBook = it.next();
            if (currBook.getBookId() == bookId){
                it.remove();
                System.out.println("Book removed from DB..");
                repo.serializeData(bookLists);
                return currBook;
            }
        }
        System.out.println("❌ No book found with that ID.");
        return null;
    }

    public void addNewBook(Book book){
        ArrayList<Book> bookLists = repo.deserializeData();
        for (Book currBook : bookLists){
            if (currBook.getBookId() == book.getBookId()){
                System.out.println("Book Already exists in DB");
                return;
            }
        }

        bookLists.add(book);
        repo.serializeData(bookLists);
        System.out.println("✅ Book added successfully!");
    }
}
