package Books;

import java.io.Serializable;

public class Book implements Serializable {

    private int bookId;
    private String title;
    private String author;
    private int totalQty;
    private int availableQty;

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public int getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(int availableQty) {
        this.availableQty = availableQty;
    }

    public Book(int bookId, String title, String author, int totalQty){
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.totalQty = totalQty;
        this.availableQty = totalQty;
    }

}
